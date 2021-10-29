package com.dosth.instruction.board.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.op.CloseDoor;
import com.dosth.instruction.board.op.LightElectroStatus;
import com.dosth.instruction.board.op.OffLight;
import com.dosth.util.SerialTool;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public abstract class DetACloseDoor {
	private static final Logger logger = LoggerFactory.getLogger(DetACloseDoor.class);

	// 读取状态
	private boolean closeFlag = true;
	// 锁
	private Lock lock = new ReentrantLock();
	// 关闭锁
	private Condition close = lock.newCondition();
	// 读取锁
	private Condition read = lock.newCondition();
	// 关灯锁
	private Condition light = lock.newCondition();
	// 串口锁
	private Condition comm = lock.newCondition();

	/**
	 * @description 启动方法
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 * @throws Exception
	 */
	public void start(String portName, int baudrate) throws Exception {
		receive(portName, baudrate);
		ExecutorService service = Executors.newCachedThreadPool();
		try {
			service.submit(new Runnable() {
				@Override
				public void run() {
					closeDoor();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					readStatus();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					offLight();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					lock.lock();
					try {
						Thread.sleep(1000);
						logger.info("激活关门线程");
						read.signal();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					lock.lock();
					try {
						comm.await();
						logger.info("关闭串口");
						Board.close();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}
				}
			});
		} finally {
			service.shutdown();
			System.err.println("串口关闭,线程池关闭");
		}
	}

	/**
	 * @description 关门操作
	 */
	private void closeDoor() {
		lock.lock();
		try {
			close.await();
			if (closeFlag) {
				logger.info("关门操作");
				new CloseDoor().start();
			} else {
				light.signal();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info("关门线程释放");
			lock.unlock();
		}
	}

	/**
	 * @description 读取灯和电磁铁状态
	 */
	private void readStatus() {
		lock.lock();
		try {
			read.await();
			logger.info("读状态");
			new LightElectroStatus().readStatus();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.info("读状态释放锁");
			lock.unlock();
		}
	}

	/**
	 * @description 关灯操作
	 */
	private void offLight() {
		lock.lock();
		try {
			light.await();
			logger.info("关灯");
			if (closeFlag) {
				new OffLight().start();
			} else {
				comm.signal();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	/**
	 * @description 接收串口消息
	 * @param portName
	 * @param baudrate
	 * @throws Exception
	 */
	private void receive(String portName, int baudrate) throws Exception {
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
					System.err.println("与串口设备通讯中断");
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
						lock.lock();
						// 读取串口数据
						byte[] data;
						try {
							data = SerialTool.readFromPort(Board.serialPort);
							if (data != null) {
								switch (Board.instruction) {
								case CLOSEDOOR:
									light.signal();
									break;
								case LIGHTELECTROSTATUS:
									int doorStatusIndex = 12;
									if ((data.length > 14)) {
										doorStatusIndex += 2;
									}
									if (data[doorStatusIndex] == 0x00) {
										logger.info("门未归位");
										receiveMessage("门未归位");
										closeFlag = false;
									} else { // 没有异常,停止伺服器线程
										logger.info("门已关上");
										receiveMessage("门已关上");
									}
									close.signal();
									break;
								case OFFLIGHT: // 关灯
									int lightIndex = 7;
									if (data.length > 9) {
										lightIndex += 2;
									}
									if (data[lightIndex] == 1) {
										receiveMessage("关灯失败");
									}
									comm.signal();
									break;
								default:
									break;
								}
							}
						} catch (Exception e) {
							System.err.println("读取串口数据失败:" + e.getMessage());
							e.printStackTrace();
						} finally {
							lock.unlock();
						}
					}
					break;
				}
			}
		});
	}

	/**
	 * @description 接收消息
	 * @param message
	 */
	public abstract void receiveMessage(String message);
}