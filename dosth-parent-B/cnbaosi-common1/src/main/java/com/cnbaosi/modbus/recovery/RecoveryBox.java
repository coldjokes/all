package com.cnbaosi.modbus.recovery;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.exception.BscException;
import com.cnbaosi.modbus.ModbusServerConfig;
import com.cnbaosi.modbus.ModbusUtil;
import com.cnbaosi.modbus.enums.AddrType;

import net.wimpi.modbus.ModbusException;

/**
 * @description 回收柜
 * @author guozhidong
 *
 */
public abstract class RecoveryBox {
	
	private static final Logger logger = LoggerFactory.getLogger(RecoveryBox.class);
	
	// 回收口操作
	private EnumRecoveryOp op;
	
	/**
	 * @description 回收柜操作
	 * @param host 回收柜通讯Host
	 * @param port 回收柜通讯Port
	 * @param op 回收口操作
	 */
	public void op(String host, int port, EnumRecoveryOp op) {
		try {
			ModbusUtil.getInstance(new ModbusServerConfig(host, port));
			this.op = op;
			logger.info("写入" + op.getDesc());
			ModbusUtil.writeModbusValue(1, AddrType.ScanResult, op.getCode());
			readStatus(host, port);
		} catch (ModbusException e) {
			e.printStackTrace();
			throw new BscException(op.getDesc() + "异常:" + e.getMessage());
		}
	}
	
	/**
	 * @description 读取回收柜状态
	 * @param host 回收柜通讯Host
	 * @param port 回收柜通讯Port
	 */
	private void readStatus(String host, int port) {
		long start = System.currentTimeMillis();
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<Integer> future;
		Integer result;
		Message message = new Message(null, null);
		try {
			while (true) {
				future = service.submit(new Callable<Integer>() {
					@Override
					public Integer call() throws Exception {
						ModbusUtil.getInstance(new ModbusServerConfig(host, port));
						return ModbusUtil.readModbusValue(1, AddrType.ScanResult);
					}
				});
				result = future.get();
				logger.info("读取" + op.getDesc() + "值:" + result);
				if (result == 0) {
					message.setCustomMsg(op.getDesc() + "成功");
					message.setType(ReturnMsgType.RECOVERY_SUCC);
					receiveMessage(message);
					logger.warn(message.getCustomMsg());
					break;
				} else {
					Thread.sleep(500);
				}
				if (System.currentTimeMillis() - start > 30 * 1000) {
					message.setCustomMsg(op.getDesc() + "超时");
					message.setType(ReturnMsgType.TIME_OUT);
					receiveMessage(message);
					logger.warn(message.getCustomMsg());
					break;
				}
			} 
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			logger.error("读取取料口状态异常:" + e.getMessage());
		} finally {
			service.shutdown();
		}
	}
	
	/**
	 * @description 接收消息
	 * @param message 消息内容
	 * @return
	 */
	public abstract void receiveMessage(Message message);
}