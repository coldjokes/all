package com.cnbaosi.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.CabinetConstant;
import com.cnbaosi.common.DeterminantConstant;
import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.StorageMedium;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 消息处理中心
 * @author guozhidong
 *
 */
public abstract class MessageConsume {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageConsume.class);
	
	private static SerialPort serialPort;
	
	// 发送接收锁
	private static Lock sendReceLock = new ReentrantLock();
	// 发送锁
	private static Condition send = sendReceLock.newCondition();
	// 是否回合
	private volatile Map<Byte, Boolean> isRound = new ConcurrentHashMap<>();
	// 信号发送正常
	private volatile boolean ok;
	// 待发送消息
	private static byte[] messages;
	
	/**
	 * @description 启动监听
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @throws Exception
	 */
	public void startListener(String portName, int baudrate) throws Exception {
		serialPort = SerialTool.openPort(portName, baudrate);
		serialPort.addEventListener(new SerialPortEventListener() {
			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				switch (serialPortEvent.getEventType()) {
				case SerialPortEvent.BI: // 10 通讯中断
					logger.error("与串口设备通讯中断");
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
					if (serialPort == null) {
						logger.error("串口对象为空！监听失败！");
					} else {
						synchronized (serialPort) {
							// 读取串口数据
							try {
								byte[] data = SerialTool.readFromPort(serialPort);
								if (data != null) {
									StorageMedium.putMessageBytes(data);
								}
							} catch (Exception e) {
								logger.error(e.getMessage());
								e.printStackTrace();
							}
						}
					}
					break;
				}
			}
		});
	}
	
	/**
	 * @description 激活发送锁
	 */
	class MessageStatusCheck implements Runnable {
		
		private long startTimeStamp;
		
		public MessageStatusCheck(long startTimeStamp) {
			this.startTimeStamp = startTimeStamp;
		}

		@Override
		public void run() {
			int retryTime = 0;
			ok = false;
			while (true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (ok) { // 接收到成功标识,启动发送锁
					sendReceLock.lock();
					try {
						send.signal();
					} finally {
						sendReceLock.unlock();
					}
					System.err.println("发送正常了,退出了");
					break;
				} else {
					try {
						if (System.currentTimeMillis() - startTimeStamp > 1000) { // 1S后仍接收到成功标识再发送一次
							if (retryTime > 2) {
								logger.error("指令" + SerialTool.bytesToHexString(messages) + "重发" + retryTime + "次无效,退出");
								Message message = new Message(messages);
								message.setCustomMsg("发送并重试失败,请联系管理员!");
								message.setType(ReturnMsgType.TIME_OUT);
								receiveMessage(message);
								sendReceLock.lock();
								try {
									isRound.put(messages[5], true);
									send.signal();
								} finally {
									sendReceLock.unlock();
								}
								break;
							}
							logger.info("重试之前再验证一次");
							if (ok) {
								logger.info("重试之前验证通过");
								sendReceLock.lock();
								try {
									isRound.put(messages[5], true);
									send.signal();
								} finally {
									sendReceLock.unlock();
								}
								break;
							}
							retryTime ++;
							logger.error("超时没有返回信号(" + retryTime + "),重发指令");
							synchronized (serialPort) {
								SerialTool.sendToPort(serialPort, messages);
							}
							logger.info("超时没有返回信号,重发指令["+SerialTool.bytesToHexString(messages)+"],退出了");
							startTimeStamp = System.currentTimeMillis();
							continue;
						} else {
							logger.info("有效期内");
							if (ok) {
								logger.info("有效期内,已接收到信号");
								sendReceLock.lock();
								try {
									isRound.put(messages[5], true);
									send.signal();
								} finally {
									sendReceLock.unlock();
								}
								break;
							} else {
								logger.info("有效期内,未接收到信号,循环去");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * @description 消息消费启动类
	 */
	public void start() {
		// 消息发送
		new Thread(new Runnable() {
			@Override
			public void run() {
				sendReceLock.lock();
				try {
					int boardNoIndex = 0;
					while (true) {
						System.err.println("next sender ######################");
						messages = StorageMedium.orderQueue.take();
						if (messages[0] == DeterminantConstant.TRO_START[0] && messages[1] == DeterminantConstant.TRO_START[1] &&messages[2] == DeterminantConstant.TRO_START[2]) {
							boardNoIndex = 4;
						} else if (messages[0] == DeterminantConstant.START[0] && messages[1] == DeterminantConstant.START[1] &&messages[2] == DeterminantConstant.START[2]&&messages[3] == DeterminantConstant.START[3]) {
							boardNoIndex = 5;
						}
						Boolean flag = isRound.get(messages[boardNoIndex]);
						if (flag == null || flag) {
							Thread.sleep(200);
							logger.info("发送信息>>>>>" + SerialTool.bytesToHexString(messages));
							synchronized (serialPort) {
								SerialTool.sendToPort(serialPort, messages);
							}
							System.err.println("waitting ~~~~~~~~~~~");
							isRound.put(messages[5], false);
							CabinetConstant.busyFlag = true;
							ok = false;
							new Thread(new MessageStatusCheck(System.currentTimeMillis())).start();
							send.await();
						} else {
							logger.warn("当前栈号未回合,忽略消息:" + SerialTool.bytesToHexString(messages));
							Message message = new Message(messages);
							message.setType(ReturnMsgType.BUSY);
							CabinetConstant.busyFlag = true;
							message.setCustomMsg("当前栈号正在处理消息:" + SerialTool.bytesToHexString(messages));
							receiveMessage(message);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sendReceLock.unlock();
				}
			}
		}).start();
		
		// 消息消费
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte b0;
				byte b1;
				byte b2;
				byte b3;
				byte b4;
				byte boardNo;
				byte instructionWord;
				byte[] bytes;
				while (true) {
					try {
						b0 = StorageMedium.messageQueue.take(); // O或57
						b1 = StorageMedium.messageQueue.take(); // K或4B
						while (true) {
							if (b0 == DeterminantConstant.OK[0] && b1 == DeterminantConstant.OK[1]) { // OK跳出
								break;
							}
							if ((b0 == DeterminantConstant.TRO_START[0] && b1 == DeterminantConstant.TRO_START[1]) 
									|| (b0 == DeterminantConstant.START[0] && b1 == DeterminantConstant.START[1])) { // 42 53 或 57 4B跳出
								break;
							}
							// 其他延后一位
							b0 = b1;
							b1 = StorageMedium.messageQueue.take();
						}
						if (b0 == DeterminantConstant.OK[0] && b1 == DeterminantConstant.OK[1]) {
							logger.info("接收文为" + SerialTool.bytesToHexString(new byte[] {b0, b1}) + ",发送继续");
							ok = true;
							continue;
						} 
						b2 = StorageMedium.messageQueue.take(); // 4C
						// 可控抽屉柜
						if (b0 == DeterminantConstant.TRO_START[0] && b1 == DeterminantConstant.TRO_START[1] && b2 == DeterminantConstant.TRO_START[2]) {
							b3 = StorageMedium.messageQueue.take(); // length
							boardNo = StorageMedium.messageQueue.take(); // boardNo
							instructionWord = StorageMedium.messageQueue.take(); // 指令集
							bytes = new byte[b3];
							bytes[0] = b0;
							bytes[1] = b1;
							bytes[2] = b2;
							bytes[3] = b3;
							bytes[4] = boardNo;
							bytes[5] = instructionWord;
							for (int index = 6; index < b3; index++) {
								bytes[index] = StorageMedium.messageQueue.take();
							}
						} else { // 其他485协议
							b3 = StorageMedium.messageQueue.take(); // 59
							if (b0 != DeterminantConstant.START[0] || b1 != DeterminantConstant.START[1] || b2 != DeterminantConstant.START[2] || b3 != DeterminantConstant.START[3]) {
								logger.error("非法消息:" + SerialTool.bytesToHexString(new byte[] {b0, b1, b2, b3}));
								Message message = new Message(new byte[] {b0, b1, b2, b3});
								message.setCustomMsg("主控板返回异常代码" + SerialTool.bytesToHexString(new byte[] {b0, b1, b2, b3}) + ",请联系管理员!");
								message.setType(ReturnMsgType.ERR_CODE);
								receiveMessage(message);
								StorageMedium.orderQueue.clear();
								StorageMedium.messageQueue.clear();
								System.err.println("不重发,等重试····································································");
	//							ok = true;
								continue;
							}
							b4 = StorageMedium.messageQueue.take(); // length
							boardNo = StorageMedium.messageQueue.take(); // boardNo
							instructionWord = StorageMedium.messageQueue.take(); // 指令集
							if (instructionWord == BoardInstruction.READSINGLE.getInstructionWord()) {
								logger.info("接收文为锁控板读锁协议反馈,发送继续");
								ok = true;
							}
							// 锁控板控制，通知下一线程
							if (instructionWord == BoardInstruction.OPENSINGLE.getInstructionWord()) {
								logger.info("接收文为锁控板开锁协议反馈,发送继续");
								ok = true;
							}
							bytes = new byte[b4];
							bytes[0] = b0;
							bytes[1] = b1;
							bytes[2] = b2;
							bytes[3] = b3;
							bytes[4] = b4;
							bytes[5] = boardNo;
							bytes[6] = instructionWord;
							for (int index = 7; index < b4; index++) {
								bytes[index] = StorageMedium.messageQueue.take();
							}
						}
						isRound.put(boardNo, true);
						CabinetConstant.busyFlag = false;
						// 根据当前栈号获取映射栈号
						StorageMedium storageMedium = null;
						if (StorageMediumPicker.boardNoMapping.get(boardNo) != null) {
							boardNo = StorageMediumPicker.boardNoMapping.get(boardNo);
						}
						storageMedium = StorageMediumPicker.storageMap.get(boardNo);
						storageMedium.getListenerData(bytes);
					} catch (Exception e) {
						logger.error("消息处理异常:" + e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	/**
	 * @description 接收消息
	 * @param message 消息
	 */
	public abstract void receiveMessage(Message message);
}