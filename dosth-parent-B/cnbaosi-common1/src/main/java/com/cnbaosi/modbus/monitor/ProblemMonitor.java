package com.cnbaosi.modbus.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.enums.TipLevel;
import com.cnbaosi.modbus.ModbusServerConfig;
import com.cnbaosi.modbus.ModbusUtil;
import com.cnbaosi.modbus.dto.MonitorData;
import com.cnbaosi.modbus.enums.AddrType;

import net.wimpi.modbus.ModbusException;

/**
 * @description 故障监控
 * @author guozhidong
 *
 */
public abstract class ProblemMonitor implements ModbusMonitor {

	private static final Logger logger = LoggerFactory.getLogger(ProblemMonitor.class);

	private List<AddrType> typeList;

	/**
	 * @description 添加监控地址类型
	 * @param type 地址类型
	 */
	public void addAddrType(AddrType type) {
		if (typeList == null) {
			typeList = new ArrayList<>();
		}
		if (!typeList.contains(type)) {
			typeList.add(type);
		}
	}

	/**
	 * @description 启动监控
	 * @param host Modbus-host
	 * @param port Modbus-port
	 */
	public void startMonitor(String host, int port) {
		ExecutorService service = Executors.newCachedThreadPool();
		Future<MonitorData> future;
		MonitorData data;
		try {
			ModbusUtil.getInstance(new ModbusServerConfig(host, port));
			for (AddrType type : typeList) {
				try {
					future = service.submit(new Callable<MonitorData>() {
						@Override
						public MonitorData call() throws Exception {
							return new MonitorData(type, ModbusUtil.readModbusValue(1, type));
						}
					});
					data = future.get();
					logger.info("获取" + data.getType().getDesc() + ":" + data.getResult());
					if (data.getResult().intValue() != data.getType().getRegularValue()) {
						switch (data.getType()) {
						case ServoAlarm:
						case DoorCheck:
						case LeftDoorOpenFail:
						case RightDoorOpenFail:
						case LeftDoorCloseFail:
						case RightDoorCloseFail:
						case DoorSignal:
						case LeftDoorSignal:
						case RightDoorSignal:
							data.setLevel(TipLevel.ERR);
							break;
						case TopBoundAlarm:
						case BottomBoundAlarm:
							data.setLevel(TipLevel.WARN);
							break;
						default:
							break;
						}
						receiveMonitorData(data);
					}
				} catch (InterruptedException e) {
					logger.error("故障监测线程执行中断异常:" + e.getMessage());
					e.printStackTrace();
				} catch (ExecutionException e) {
					logger.error("故障监测线程执行异常:" + e.getMessage());
					e.printStackTrace();
				}
			}
		} finally {
			ModbusUtil.close();
			service.shutdown();
		}
	}

	/**
	 * @description 恢复门故障
	 * @param host Modbus-host
	 * @param port Modbus-port
	 */
	public static void resetDoorErr(String host, int port) {
		try {
			logger.info("恢复门故障");
			ModbusUtil.getInstance(new ModbusServerConfig(host, port));
			ModbusUtil.writeModbusValue(1, AddrType.DoorOpenFailClear, 0);
			Thread.sleep(200);
			ModbusUtil.writeModbusValue(1, AddrType.DoorCloseFailClear, 0);
			Thread.sleep(200);
			ModbusUtil.writeModbusValue(1, AddrType.RightDoorOpenFailClear, 0);
			Thread.sleep(200);
			ModbusUtil.writeModbusValue(1, AddrType.RightDoorCloseFailClear, 0);
		} catch (ModbusException | InterruptedException e) {
			logger.error("Modbus协议异常:" + e.getMessage());
			e.printStackTrace();
		} finally {
			ModbusUtil.close();
		}
	}
}