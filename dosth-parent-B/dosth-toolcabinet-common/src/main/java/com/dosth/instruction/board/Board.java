package com.dosth.instruction.board;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.CabinetInstruction;
import com.dosth.instruction.board.enums.Instruction;
import com.dosth.util.SerialTool;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * 
 * @description 行列式板控制
 * @author guozhidong
 *
 */
public abstract class Board extends CabinetInstruction {

	private static final Logger logger = LoggerFactory.getLogger(Board.class);

	// 料斗速度
	public static byte speed = 0x01;
	
	// 门的高度
	public static final int doorHeight = 380;

	/**
	 * 板子列数量
	 */
	public static final int boardColNum = 12;

	/**
	 * @description 板号
	 */
	protected static final byte BOARDNO = 0x00;
	/**
	 * @description 串口对象
	 */
	public static SerialPort serialPort;
	/**
	 * @description 串口
	 */
	private static String portName;
	/**
	 * @description 波特率
	 */
	private static int baudrate;

	/**
	 * @description 指令起始符
	 */
	protected byte[] START = new byte[] { 0x57, 0x4B, 0x4C, 0x59 };

	/**
	 * @description 成功标识
	 */
	public static final byte[] OK = new byte[] { 0x4F, 0x4B };

	private Byte[] data;


	public static Instruction instruction;
	
	public Board(Instruction instruction) {
		Board.instruction = instruction;
	}

	/**
	 * @description 成功
	 * @return
	 */
	protected byte succ() {
		return 0x00;
	}

	/**
	 * @description 设置返送数据
	 * @param data 向串口待发送数据
	 */
	public void setData(Byte[] data) {
		this.data = data;
	}

	/**
	 * @description 失败
	 * @return
	 */
	protected byte fail() {
		return 0x01;
	}

	/**
	 * @description 异常
	 * @return
	 */
	protected byte exc() {
		return 0;
	}

	/**
	 * @description 创建发送数据报文
	 * @param data
	 * @return
	 */
	private byte[] createSendMsg() {
		byte[] bytes = null;
		if (data == null) {
			bytes = new byte[START.length + 3 + 1];
		} else {
			bytes = new byte[START.length + 3 + data.length + 1];
		}
		for (int i = 0; i < START.length; i++) {
			bytes[i] = START[i];
		}
		bytes[4] = Board.instruction.getSendFrameLength();
		bytes[5] = BOARDNO;
		bytes[6] = Board.instruction.getInstructionWord();
		if (data != null && data.length > 0) {
			for (int i = 0; i < data.length; i++) {
				bytes[START.length + 3 + i] = data[i];
			}
		}
		bytes[bytes.length - 1] = getXor(bytes);
		return bytes;
	}

	/**
	 * @description 加总异或校验码生成
	 * @param datas
	 * @return
	 */
	private static byte getXor(byte[] datas) {
		byte temp = datas[0];
		for (int i = 1; i < datas.length; i++) {
			temp ^= datas[i];
		}
		return temp;
	}
	
//	public abstract byte[] receive();

	/**
	 * @description 串口发送消息反馈
	 * @throws Exception
	 */
	public static void receive() throws Exception {
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
					if (Board.serialPort == null) {
						logger.error("串口对象为空！监听失败！");
					} else {
						// 读取串口数据
						byte[] data;
						try {
							data = SerialTool.readFromPort(serialPort);
							if (data.length == 2) { // 发送信息返回信号
								if (data[0] == OK[0] && data[1] == OK[1]) {
									logger.info("发送" + Board.instruction.getDesc() + "信号成功");
								} else {
									logger.error("发送" + Board.instruction.getDesc() + "信号失败");
								}
							} else {
								logger.info(String.valueOf(data.length));
								switch (Board.instruction) {
								case LIGHTELECTROCONTROL:
									logger.info("灯和电磁铁的控制>>" + bytesToString(data));
									break;
								case LIGHTELECTROSTATUS:
									logger.info("检测灯和电磁铁的数据>>" + bytesToString(data));
									if (data[11] == 0xFF && data[12] == 0xFF) {
										logger.info("位置跑偏,需复位");
									} else {
										logger.info("位置在原位,无需复位,直接启动料斗");
									}
									break;
								case SERVOCONTROL:
									logger.info("伺服电机状态>>" + bytesToString(data));
									byte status;
									switch (data.length) {
									case 9:
										status = data[7];
										if (status == 0x00) {
											logger.info("料斗到达指定位置");
										} else {
											logger.info("料斗未能到达指定位置");
										}
										break;
									case 11:
										status = data[9];
										if (status == 0x00) {
											logger.info("料斗到达指定位置");
										} else {
											logger.info("料斗未能到达指定位置");
										}
										break;
									default:
										logger.info("伺服点击返回信号异常");
										break;
									}
									break;
								case SERVORESET:
									logger.info("伺服器复位>>" + bytesToString(data));
									break;
								case REVOLVE:
									logger.info("电机旋转>>" + bytesToString(data));
									break;
								default:
									logger.info("接收未标明["+instruction.getDesc()+"]数据>>" + bytesToString(data));
									break;
								}
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
	 * @description 串口启动
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @throws Exception
	 */
	public static void startSerialPort(String portName, int baudrate) throws Exception {
		Board.portName = portName;
		Board.baudrate = baudrate;
		Board.serialPort = SerialTool.openPort(Board.portName, Board.baudrate);
		Board.serialPort.notifyOnDataAvailable(true);
		Board.serialPort.notifyOnBreakInterrupt(true);
	}
	
	/**
	 * @description 串口关闭
	 */
	public static void close() {
		if (Board.serialPort != null) {
			Board.serialPort.close();
		}
	}

	/**
	 * @description IC读卡器重启
	 * @throws Exception
	 */
	public static void restart() throws Exception {
		if (SerialTool.checkPort(Board.portName)) {
			SerialTool.closePort(Board.serialPort);
		}
		startSerialPort(Board.portName, Board.baudrate);
	}

	/**
	 * @description 发送数据
	 * @throws Exception
	 */
	protected void sendData() throws Exception {
		byte[] messages = this.createSendMsg();
		logger.info("发送的数据：" + bytesToString(messages));
		SerialTool.sendToPort(Board.serialPort, messages);
	}

	/**
	 * @description 二进制数据转换成字符串
	 * 
	 * @param bytes 二进制数组
	 * @return
	 */
	public static String bytesToString(byte[] bytes) {
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
}