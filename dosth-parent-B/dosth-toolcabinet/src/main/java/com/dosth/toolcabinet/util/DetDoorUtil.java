package com.dosth.toolcabinet.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.constant.EnumDoor;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.op.CloseDoor;
import com.dosth.instruction.board.op.OffLight;
import com.dosth.instruction.board.op.OnLight;
import com.dosth.instruction.board.op.OpenDoor;
import com.dosth.util.SerialTool;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 行列式门操作
 * @author chenlei
 *
 */
public abstract class DetDoorUtil {

	private static final Logger logger = LoggerFactory.getLogger(DetDoorUtil.class);
	
	// 超时时间,默认1s
	private int timeout = 5;
	// 接收标识
	private boolean receiveFlag = false;

	private Boolean openFlag; // 开门标识 true 为开门;false 为关门

	private EnumDoor door; // 门枚举

	public DetDoorUtil(Boolean openFlag) {
		this.openFlag = openFlag;
	}
	
	/**
	 * @description 串口发送消息反馈
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @param params 动态参数,params[0]为超时时间
	 * @throws Exception
	 */
	public void startListener(String portName, int baudrate, int... params) throws Exception {
		if (params != null && params.length > 0) {
			timeout = params[0];
		}

		Board.startSerialPort(portName, baudrate);
		Board.serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				switch (serialPortEvent.getEventType()) {
				case SerialPortEvent.BI: // 10 通讯中断
					logger.warn("与串口设备通讯中断");
					break;
				case SerialPortEvent.OE: // 7 溢位（溢出）错误
				case SerialPortEvent.FE: // 9 帧错误
				case SerialPortEvent.PE: // 8 奇偶校验错误
				case SerialPortEvent.CD: // 6 载波检测
				case SerialPortEvent.CTS: // 3 清除待发送数据
				case SerialPortEvent.DSR: // 4 待发送数据准备好了
				case SerialPortEvent.RI: // 5 振铃指示
				case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
					break;
				case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据
					logger.error("串口监听到动静了~~~~~~~~~~~~~~~~~~~~~~~~");
					if (Board.serialPort == null) {
						logger.error("串口对象为空！监听失败！");
					} else {
						// 读取串口数据
						byte[] data;
						try {
							Message message = null;
							data = SerialTool.readFromPort(Board.serialPort);
							if (data != null) {
								message = new Message(data, bytesToString(data));
								switch (Board.instruction) {
								case OPENDOOR:
									logger.info("开门>>" + bytesToString(data));
									if (data[9] == 0x00) {
										receiveFlag = true;
										message.setType(ReturnMsgType.OPEN_SUCC);
									} else {
										receiveFlag = true;
										logger.info("开门失败");
										message.setType(ReturnMsgType.OPEN_FAIL);
									}
									break;
								case CLOSEDOOR:
									logger.info("关门>>" + bytesToString(data));
									if (data[9] == 0x00) {
										receiveFlag = true;
										logger.info("关门成功");
										message.setType(ReturnMsgType.CLOSED_SUCC);
									} else {
										receiveFlag = true;
										logger.info("关门失败！请重试...");
										message.setType(ReturnMsgType.CLOSED_FAIL);
									}
									break;
								default:
									break;
								}
								receiveMessage(message);
							}
						} catch (Exception e) {
							logger.error("读取串口数据失败:" + e.getMessage());
							e.printStackTrace();
						} 
					}
					break;
				}
			}
		});
	}

	/**
	 * @description 启动开门或关门
	 * @param door 门类型
	 */
	public void start(EnumDoor door) {
		this.door = door;
		opDoor();
	}

	/**
	 * @description 操作门
	 */
	private void opDoor() {
		logger.info("发送开门指令");
		if (EnumDoor.LEFT.equals(door)) {
			if (openFlag != null && openFlag) {
				try {
					new OpenDoor().start();
					Thread.sleep(200);
					new OnLight().start();
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					new CloseDoor().start();
					Thread.sleep(200);
					new OffLight().start();
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else { // 右边或全部
			if (openFlag != null && openFlag) {
			} else {
			}
		}
		readDoorStatus();
	}

	/**
	 * @description 读取门状态
	 */
	private void readDoorStatus() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				try {
					while (true) {
						if (System.currentTimeMillis() - start > timeout * 1000 || receiveFlag) {
							if (receiveFlag) {
								logger.info("已经等待到监听结果");
							} else {
								logger.info("没有等待到监听结果");
							}
							break;
						} else {
							logger.info("等待监听结果");
	                        try {
	                            Thread.sleep(500);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }
						}
					}
				} finally {
					closePort();
				}
			}
		}).start();
	}

	/**
	 * @description 关闭端口
	 */
	private void closePort() {
		try {
			logger.info("关闭行列式回转口串口");
			SerialTool.closePort(Board.serialPort);
		} catch (Exception e) {
			logger.error("串口关闭异常:" + e.getMessage());
		}
	}
	
	/**
	 * @description 二进制数据转换成字符串
	 * 
	 * @param bytes 二进制数组
	 * @return
	 */
	private static String bytesToString(byte[] bytes) {
		StringBuilder receive = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String a = Integer.toHexString(bytes[i] & 0xFF);
			if (a.length() < 2) {
				receive.append(0);
			}
			receive.append(a);
			if ((i + 1) % 1 == 0) {
				receive.append(" ");
			}
		}
		return receive.toString();
	}
	
	/**
	 * @description 接收信息
	 * @param message
	 */
	public abstract void receiveMessage(Message message);
}