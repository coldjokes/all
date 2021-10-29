package com.cnbaosi.modbus.hopper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.enums.HopperWork;
import com.cnbaosi.modbus.ModbusServerConfig;
import com.cnbaosi.modbus.ModbusUtil;
import com.cnbaosi.modbus.enums.AddrType;

import net.wimpi.modbus.ModbusException;

/**
 * @description 
 * @author guozhidong
 *
 */
public abstract class HopperPlcOp {
	private static final Logger logger = LoggerFactory.getLogger(HopperPlcOp.class);
	
	// 料斗操作
	private HopperWork hopperWork;
	// 最高层层级
	private int topLevel;
	// 超时时间
	private int timeout = 10;
	
	public void setHopperWork(HopperWork hopperWork) {
		this.hopperWork = hopperWork;
	}

	public void setTopLevel(int topLevel) {
		this.topLevel = topLevel;
	}

	/**
	 * @description 料斗操作启动
	 * @param host PLC Host
	 * @param port PLC 端口
	 * @param params 动态参数,params[0],超时时间,默认10s
	 */
	public void start(String host, int port, int... params) {
		if (params != null && params.length > 0) {
			this.timeout = params[0];
		}
		try {
			ModbusUtil.getInstance(new ModbusServerConfig(host, port));
			switch (this.hopperWork) {
			case UP:
				ModbusUtil.writeModbusValue(1, AddrType.TargetLvl, this.topLevel);
				break;
			case DOWN:
				ModbusUtil.writeModbusValue(1, AddrType.TargetLvl, 1);
				break;
			case RESET:
				ModbusUtil.writeModbusValue(1, AddrType.TargetLvl, 11);
				break;
			default:
				break;
			}
			this.monitor();
		} catch (Exception e) {
			logger.error("modbus通讯异常:" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 反馈监视
	 */
	private void monitor() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				int result;
				while (true) {
					try {
						result = ModbusUtil.readModbusValue(1, AddrType.TargetLvl);
						if (result == 0 || System.currentTimeMillis() - start > timeout * 1000) {
							if (result != 0) {
								receiveMessage(hopperWork.getDesc() + "失败或超时");
							} else {
								receiveMessage(hopperWork.getDesc() + "成功");
							}
							break;
						}
					} catch (ModbusException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * @description 接收消息
	 * @param message 消息内容
	 */
	public abstract void receiveMessage(String message);
}