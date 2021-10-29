package com.cnbaosi.modbus.monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.constant.EnumDoor;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.modbus.ModbusServerConfig;
import com.cnbaosi.modbus.ModbusUtil;
import com.cnbaosi.modbus.enums.AddrType;

import net.wimpi.modbus.ModbusException;

/**
 * @description 门操作
 * @author guozhidong
 *
 */
public abstract class DoorOp {

	private static final Logger logger = LoggerFactory.getLogger(DoorOp.class);

	private Lock lock = new ReentrantLock();
	// 左门锁
	private Condition conditionLeft = lock.newCondition();
	// 右门锁
	private Condition conditionRight = lock.newCondition();

	private String host; // modbus通讯host
	private int port; // modbus通讯port
	private Boolean openFlag; // 开门标识 true 为开门;false 为关门

	private EnumDoor door; // 门枚举

	private int leftFlag = -1; // 初始为-1,异常为0,正常为1
	private int rightFlag = -1; // 初始为-1,异常为0,正常为1

	public DoorOp(String host, int port, Boolean openFlag) {
		this.host = host;
		this.port = port;
		this.openFlag = openFlag;
	}

	/**
	 * @description 启动开门或关门
	 * @param door 门类型
	 */
	public void start(EnumDoor door) {
		if (openFlag != null && openFlag) {
			ModbusUtil.getInstance(new ModbusServerConfig(host, port));
			Message message = new Message(null, null);
			try {
				int result = ModbusUtil.readModbusValue(1, AddrType.PointRising); // 料斗上升状态
				if (result == 0) {
					result = ModbusUtil.readModbusValue(1, AddrType.PointDrop); // 料斗下降状态
					if (result == 0) {
						result = ModbusUtil.readModbusValue(1, AddrType.ResetHopper); // 料斗复位状态
						if (result == 0) {
							// 料斗实际位置低位
							int lowAddress = ModbusUtil.readModbusValue(1, AddrType.CurPosition);
							// 料斗实际位置高位
							int hightAddress = ModbusUtil.readModbusValue(1, AddrType.CurPositionHigh);
							// 位置系数
							int key = ModbusUtil.readModbusValue(1, AddrType.PositionCoefficient);
							// 取料口低位
							int lowReclaimer = ModbusUtil.readModbusValue(1, AddrType.DoorCoordinate);
							// 取料口高位
							int hightReclaimer = ModbusUtil.readModbusValue(1, AddrType.DoorCoordinateHigh);
							// 取料口位置
							int reclaimerAddress = hightReclaimer * 65536 + lowReclaimer;
							// 料斗实际位置
							int hopperAddress = (hightAddress * 65536 + lowAddress) / key;
							// 判断料斗是否在取料口位置
							if (hopperAddress > 250 && hopperAddress < 800) {
								if (hopperAddress != reclaimerAddress) {
									message.setCustomMsg("料斗未在取料口位置");
									message.setType(ReturnMsgType.OPEN_FAIL);
									receiveMessage(message);
								} else {
									opDoor(door);
								}
							} else {
								opDoor(door);
							}
						} else {
							message.setCustomMsg("料斗复位中");
							message.setType(ReturnMsgType.OPEN_FAIL);
							receiveMessage(message);
						}
					} else {
						message.setCustomMsg("料斗下降中");
						message.setType(ReturnMsgType.OPEN_FAIL);
						receiveMessage(message);
					}
				} else {
					message.setCustomMsg("料斗上升中");
					message.setType(ReturnMsgType.OPEN_FAIL);
					receiveMessage(message);
				}
			} catch (ModbusException e) {
				e.printStackTrace();
			}
		} else {
			opDoor(door);
		}
	}

	/**
	 * @description 操作门
	 * @param door 门类型
	 */
	private void opDoor(EnumDoor door) {
		this.door = door;
		ModbusUtil.getInstance(new ModbusServerConfig(host, port));
		try {
			logger.info("发送开门指令");
			if (EnumDoor.LEFT.equals(door)) {
				if (openFlag != null && openFlag) {
					ModbusUtil.writeModbusValue(1, AddrType.OpenLeftDoor, 1);
				} else {
					ModbusUtil.writeModbusValue(1, AddrType.CloseLeftDoor, 1);
				}
			} else {
				if (openFlag != null && openFlag) {
					ModbusUtil.writeModbusValue(1, AddrType.OpenLeftDoor, 1);
					ModbusUtil.writeModbusValue(1, AddrType.OpenRightDoor, 1);
				} else {
					ModbusUtil.writeModbusValue(1, AddrType.CloseLeftDoor, 1);
					ModbusUtil.writeModbusValue(1, AddrType.CloseRightDoor, 1);
				}
			}
			readDoorStatus();
		} catch (ModbusException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 读取门状态
	 */
	private void readDoorStatus() {
		ExecutorService service = Executors.newFixedThreadPool(2);
		try {
			service.submit(new Runnable() {
				@Override
				public void run() {
					readLeftDoorStatus();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					readRightDoorStatus();
				}
			});
		} finally {
			service.shutdown();
		}
	}

	/**
	 * @description 读取左边门状态
	 */
	private void readLeftDoorStatus() {
		long start = System.currentTimeMillis();
		Integer result;
		try {
			Message message = new Message(null, null);
			while (true) {
				lock.lock();
				try {
					if (rightFlag == -1) {
						conditionRight.signal();
						conditionLeft.await();
					}
					ModbusUtil.getInstance(new ModbusServerConfig(host, port));
					if (openFlag != null && openFlag) {
						result = ModbusUtil.readModbusValue(1, AddrType.OpenLeftDoor);
					} else {
						result = ModbusUtil.readModbusValue(1, AddrType.CloseLeftDoor);
					}
					logger.info("读取左门状态值:" + result);
					if (result == 0) {
						logger.info("左门操作成功");
						leftFlag = 1;
						if (rightFlag == -1) {
							conditionRight.signal();
						} else {
							if (rightFlag == 0) {
								message.setCustomMsg("右侧" + (openFlag != null && openFlag ? "开门" : "关门") + "失败");
								message.setType(ReturnMsgType.OPEN_FAIL);
								receiveMessage(message);
							} else {
								message.setCustomMsg((openFlag != null && openFlag ? "开门" : "关门") + "成功");
								message.setType(ReturnMsgType.OPEN_SUCC);
								receiveMessage(message);
							}
						}
						break;
					} else {
						Thread.sleep(200);
					}
					if (System.currentTimeMillis() - start > 10 * 1000) {
						logger.warn("左门响应超时");
						leftFlag = 0;
						break;
					}
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException | ModbusException e) {
			e.printStackTrace();
			logger.error("左门监测错误:" + e.getMessage());
		}
	}

	/**
	 * @description 读取右边门状态
	 */
	private void readRightDoorStatus() {
		long start = System.currentTimeMillis();
		Integer result;
		Message message = new Message(null, null);
		try {
			while (true) {
				lock.lock();
				try {
					if (leftFlag == -1) {
						conditionLeft.signal();
						conditionRight.await();
					}
					if (door != null && EnumDoor.LEFT.equals(door)) { // 操作左边门,直接回复右门成功
						rightFlag = 1;
						if (leftFlag == -1) {
							conditionLeft.signal();
						} else {
							if (leftFlag == 0) {
								message.setCustomMsg("左侧" + (openFlag != null && openFlag ? "开门" : "关门") + "失败");
								message.setType(ReturnMsgType.OPEN_FAIL);
								receiveMessage(message);
							} else {
								message.setCustomMsg((openFlag != null && openFlag ? "开门" : "关门") + "成功");
								message.setType(ReturnMsgType.OPEN_SUCC);
								receiveMessage(message);
							}
						}
						break;
					}
					ModbusUtil.getInstance(new ModbusServerConfig(host, port));
					if (openFlag != null && openFlag) {
						result = ModbusUtil.readModbusValue(1, AddrType.OpenRightDoor);
					} else {
						result = ModbusUtil.readModbusValue(1, AddrType.CloseRightDoor);
					}
					logger.info("读取右门状态值:" + result);
					if (result == 0) {
						logger.info("右门操作成功");
						rightFlag = 1;
						if (leftFlag == -1) {
							conditionLeft.signal();
						} else {
							if (leftFlag == 0) {
								message.setCustomMsg("左侧" + (openFlag != null && openFlag ? "开门" : "关门") + "失败");
								message.setType(ReturnMsgType.OPEN_FAIL);
								receiveMessage(message);
							} else {
								message.setCustomMsg((openFlag != null && openFlag ? "开门" : "关门") + "成功");
								message.setType(ReturnMsgType.OPEN_SUCC);
								receiveMessage(message);
							}
						}
						break;
					} else {
						Thread.sleep(200);
					}
					if (System.currentTimeMillis() - start > 10 * 1000) {
						logger.warn("右门响应超时");
						rightFlag = 0;
						break;
					}
				} finally {
					lock.unlock();
				}
			}
		} catch (InterruptedException | ModbusException e) {
			e.printStackTrace();
			logger.error("右门监测错误:" + e.getMessage());
		}
	}
	
	/**
	 * @description 接收消息
	 * @param message 消息内容
	 * @return
	 */
	public abstract void receiveMessage(Message message);
}