package com.dosth.instruction.board.util;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.enums.Instruction;
import com.dosth.instruction.board.enums.Step;
import com.dosth.instruction.board.op.CloseDoor;
import com.dosth.instruction.board.op.LightElectroStatus;
import com.dosth.instruction.board.op.OffLight;
import com.dosth.instruction.board.op.OnLight;
import com.dosth.instruction.board.op.OpenDoor;
import com.dosth.instruction.board.op.Revolve;
import com.dosth.instruction.board.op.ServoControl;
import com.dosth.instruction.board.op.ServoReset;
import com.dosth.instruction.board.op.ToDoor;
import com.dosth.pojo.Col;
import com.dosth.pojo.Row;
import com.dosth.util.SerialTool;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * @description 借出工具类
 * @Author guozhidong
 */
public class BorrowUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(BorrowUtil.class);
	
	private static BorrowUtil borrowUtil;
	
	private BorrowUtil() {}
	
	public static BorrowUtil getInstance() {
		if (borrowUtil == null) {
			borrowUtil = new BorrowUtil();
		}
		return borrowUtil;
	}
	
	// 操作步骤起始位为初始化
	public static volatile Step step = Step.INIT;
	
	/**
	 * @description 行列式板子锁
	 */
	protected final Lock lock = new ReentrantLock();
	
	/**
	 * @description 状态锁
	 */
	protected final Condition conditionStatus = lock.newCondition();
	/**
	 * @description 伺服器复位锁
	 */
	protected final Condition condtionServoRest = lock.newCondition();
	/**
	 * @description 料斗锁
	 */
	public final Condition conditionHope = lock.newCondition();
	/**
	 * @description 弹簧锁
	 */
	protected final Condition conditionSlave = lock.newCondition();
	/**
	 * @description 门锁
	 */
	protected final Condition conditionToDoor = lock.newCondition();
	
	protected final Condition conditionOnLight = lock.newCondition();
	
	protected final Condition conditionOpenDoor = lock.newCondition();
	
	protected final Condition conditionCloseDoor = lock.newCondition();
	
	protected final Condition conditionOffLight = lock.newCondition();
	/**
	 * @description 完成锁
	 */
	protected final Condition conditionFinish = lock.newCondition();
	
	protected BlockingQueue<Row> queue;
	
	private volatile Integer rowNo;
	
	protected BlockingQueue<Col> colQueue;
	
	public void setQueue(BlockingQueue<Row> queue) {
		this.queue = queue;
	}

	/**
	 * @description 读取灯和电磁铁状态
	 */
	public void readStatus() {
		lock.lock();
		try {
			while (step.equals(Step.INIT) || step.equals(Step.READSTATUS) || step.equals(Step.CLOSEDOOR)) {
				if (step.equals(Step.CLOSEDOOR)) {
					break;
				}
				if (!step.equals(Step.READSTATUS)) {
					conditionStatus.await();
				}
				new LightElectroStatus().readStatus();
				step = Step.WAITTING;
			}
		} catch (Exception e) {
			logger.error("发送读取灯和电磁铁状态指令失败");
			e.printStackTrace();
		} finally {
			logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!读状态释放锁");
			lock.unlock();
		}
	}
	
	/**
	 * @description 伺服器复位
	 */
	public void reset() {
		lock.lock();
		try {
			while (step.equals(Step.INIT) || step.equals(Step.HOPE)) {
				if (!step.equals(Step.SERVORESET) && !step.equals(Step.HOPE)) {
					condtionServoRest.await();
				}
				if (step.equals(Step.HOPE)) {
					conditionHope.signal();
					break;
				}
				new ServoReset().reset();
				step = Step.WAITTING;
			}
		} catch (Exception e) {
			logger.error("发送伺服器复位指令失败");
			e.printStackTrace();
		} finally {
			logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@伺服器复位释放锁");
			lock.unlock();
		}
	}
	
	/**
	 * @description 去层级
	 */
	public void toLevel() {
		lock.lock();
		try {
			while (!queue.isEmpty() || step.equals(Step.LEVEL)) {
				if (!step.equals(Step.HOPE) && !step.equals(Step.LEVEL)) {
					conditionHope.await();
				}
				if (queue.isEmpty()) {
					logger.info("所有行都取完,到门口去");
					step = Step.TODOOR;
					logger.info("到门口去");
					conditionToDoor.signal();
					logger.info("取料料斗静止");
					conditionHope.await();
				}
				Row row = queue.take();
				if (row != null) {
					rowNo = row.getRowNo();
					colQueue = row.getColQueue();
					new ServoControl().toHeight(row.getLevelHeight());
					step = Step.WAITTING;
					conditionHope.await();
				}
			}
		} catch (Exception e) {
			logger.error("发送伺服器复位指令失败");
			e.printStackTrace();
		} finally {
			logger.info("层线程释放了############################################");
			step = Step.TODOOR;
			conditionSlave.signal();
			lock.unlock();
		}
	}
	
	/**
	 * @description 去弹簧
	 */
	public void toSlave() {
		lock.lock();
		try {
			while (!queue.isEmpty() || step.equals(Step.LEVEL)) {
				if (!step.equals(Step.LEVEL)) {
					conditionSlave.await();
				}
				if (colQueue.isEmpty()) {
					logger.info("当前行已取完,转到料斗状态");
					step = Step.HOPE;
					logger.info("料斗线程启动");
					conditionHope.signal();
					logger.info("电机线程等待");
					conditionSlave.await();
				}
				if (step.equals(Step.TODOOR)) {
					logger.info("料斗去门了");
					conditionToDoor.signal();
					break;
				}
				Revolve revolve = new Revolve();
				Col col = colQueue.take();
				if (col != null) {
					revolve.out((rowNo - 1) * Board.boardColNum + col.getBoardColNo(), col.getNum());
					step = Step.WAITTING;
					conditionSlave.await();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			logger.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$弹簧线程释放");
			lock.unlock();
		}
	}
	
	/**
	 * @description 到门
	 */
	public void toDoor() {
		lock.lock();
		try {
			if (!step.equals(Step.TODOOR)) {
				conditionToDoor.await();
			}
			new ToDoor().start();
			step = Step.WAITTING;
		} catch (Exception e) {
			logger.error("发送伺服器复位指令失败");
			e.printStackTrace();
		} finally {
			logger.info("%%%%%%%%%%%%%%%%%%%%%%到门锁释放");
			lock.unlock();
		}
	}
	
	/**
	 * @description 取料口开灯
	 */
	public void onLight() {
		lock.lock();
		try {
			if (!step.equals(Step.ONLIGHT)) {
				conditionOnLight.await();
			}
			new OnLight().start();
			step = Step.WAITTING;
		} catch (InterruptedException e) {
			logger.error("发送取料口开灯指令失败");
			e.printStackTrace();
		} finally {
			logger.info("开灯锁释放~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
			lock.unlock();
		}
	}

	/**
	 * @description 开门
	 */
	public void openDoor() {
		lock.lock();
		try {
			if (!step.equals(Step.OPENDOOR)) {
				conditionOpenDoor.await();
			}
			new OpenDoor().start();
			step = Step.WAITTING;
		} catch (Exception e) {
			logger.error("发送开门指令失败");
			e.printStackTrace();
		} finally {
			logger.info("开门锁释放………………………………………………………………………………………………………………");
			lock.unlock();
		}
	}
	
	/**
	 * @description 关门
	 */
	private void closeDoor() {
		lock.lock();
		try {
			if (!step.equals(Step.CLOSEDOOR)) {
				conditionCloseDoor.await();
			}
			new CloseDoor().start();
			step = Step.WAITTING;
		} catch (Exception e) {
			logger.error("发送关门指令失败");
			e.printStackTrace();
		} finally {
			logger.info("关门锁释放&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
			lock.unlock();
		}
	}
	
	/**
	 * @description 取料口关灯
	 */
	private void offLight() {
		lock.lock();
		try {
			if (!step.equals(Step.OFFLIGHT)) {
				conditionOffLight.await();
			}
			new OffLight().start();
			step = Step.WAITTING;
		} catch (InterruptedException e) {
			logger.error("发送取料口关灯指令失败");
			e.printStackTrace();
		}  finally {
			logger.info("关灯锁释放*********************************************************");
			lock.unlock();
		}
	}
	
	private void finish() {
		lock.lock();
		try {
			conditionFinish.await();
			Board.serialPort.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * @description 串口发送消息反馈
	 * @throws Exception
	 */
	public void receive() throws Exception {
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
					logger.info("串口监听到动静了~~~~~~~~~~~~~~~~~~~~~~~~");
					if (Board.serialPort == null) {
						logger.error("串口对象为空！监听失败！");
					} else {
						lock.lock();
						// 读取串口数据
						byte[] data;
						try {
							data = SerialTool.readFromPort(Board.serialPort);
							if (data != null) {
								if (data.length == 2) { // 发送信息返回信号
									if (data[0] == Board.OK[0] && data[1] == Board.OK[1]) {
										logger.info("发送" + Board.instruction.getDesc() + "信号成功");
									} else {
										logger.error("发送" + Board.instruction.getDesc() + "信号失败");
									}
								} else {
									switch (Board.instruction) {
									case LIGHTELECTROSTATUS:
										if (data[12] == 0xFF && data[13] == 0xFF) {
											logger.error("位置跑偏,需复位");
											step = Step.SERVORESET;
											condtionServoRest.signal();
										} else {
											logger.info("@@@@@位置在原位,无需复位,直接启动料斗");
											if (data[14] == 0x00) {
												logger.info("门未归位");
												step = Step.READSTATUS;
												conditionStatus.signal();
											} else { // 没有异常,停止伺服器线程
												step = Step.CLOSEDOOR;
												conditionCloseDoor.signal();
											}
										}
										break;
									case SERVOCONTROL:
										byte status;
										switch (data.length) {
											case 9:
												status = data[7];
												if (status == 0x00) {
													logger.info("料斗到达指定位置1");
													conditionSlave.signal();
												} else {
													logger.info("料斗未能到达指定位置");
												}
												break;
											case 11:
												status = data[9];
												if (status == 0x00) {
													logger.info("料斗到达指定位置2");
													conditionSlave.signal();
												} else {
													logger.info("料斗未能到达指定位置");
												}
												break;
											default:
												logger.info("伺服电机返回信号异常" + bytesToString(data));
												break;
										}
										break;
									case SERVORESET:
										logger.info("伺服器复位>>" + bytesToString(data));
										if (data.length == 16) {
											if (data[0] == Board.OK[0] && data[1] == Board.OK[1]) {
												if (data[11] != 0xFF && data[12] != 0xFF) {
													logger.info("位置在原位,无需复位,直接启动料斗");
													step = Step.HOPE;
													conditionHope.signal();
												}
											}
										}
										break;
									case REVOLVE:
										logger.info("电机旋转>>》》》" + bytesToString(data));
										if (data.length == Instruction.REVOLVE.getReceiveFrameLength()) {
											if (data[7] == 0x00) {
												logger.info("当前弹簧料已取完,继续下一个弹簧");
												step = Step.LEVEL;
												conditionSlave.signal();
											} else {
												logger.info("当前弹簧料未取完,继续下一个弹簧");
											}
										}
										break;
									case TODOOR:
										logger.info("到门口,先开灯>>" + bytesToString(data));
										if (data[7] == 0x00) {
											step = Step.ONLIGHT;
											conditionOnLight.signal();
										}
										break;
									case ONLIGHT:
										logger.info("开灯后,再开门>>" + bytesToString(data));
										if (data[9] == 0x00) {
											logger.info("开灯成功");
											step = Step.OPENDOOR;
											conditionOpenDoor.signal();
										}
										break;
									case OPENDOOR:
										logger.info("开门>>" + bytesToString(data));
										
										conditionFinish.signal();
										
										break;
									case CLOSEDOOR:
										logger.info("关门后,再关灯>>" + bytesToString(data));
										if (data[9] == 0x00) {
											logger.info("关门成功,去关灯");
											step = Step.OFFLIGHT;
											conditionOffLight.signal();
										} else {
											logger.info("门没有关上");
										}
										break;
									case OFFLIGHT:
										logger.info("关灯，料斗动>>" + bytesToString(data));
										if (data[9] == 0x00) {
											logger.info("关灯成功,停止伺服器,去取料");
											step = Step.HOPE;
											condtionServoRest.signal();
										}
										break;
									default:
										logger.info("未罗列信号》》" + Board.instruction + ">>" + bytesToString(data));
										break;
									}
									logger.info("消息接收截至~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
								}
							} else {
								logger.info("没有等到数据，一直等");
							}
						} catch (Exception e) {
							logger.error("读取串口数据失败:" + e.getMessage());
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
	 * @description 借出工具类启动
	 * @param portName 串口名称
	 * @param baudrate 波特率
	 */
	public void start(String portName, Integer baudrate) {
		try {
			Board.startSerialPort(portName, baudrate);
			borrowUtil.receive();
			
			ExecutorService service = Executors.newCachedThreadPool();
			
			service.submit(new Runnable() {
				@Override
				public void run() {
					readStatus();
				}
			});
			
			service.submit(new Runnable() {
				@Override
				public void run() {
					reset();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					toLevel();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					toSlave();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					toDoor();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					onLight();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					openDoor();
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
					closeDoor();
				}
			});
			service.submit(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			});
			
			service.submit(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.lock();
					try {
						logger.info("马上都状态");
						conditionStatus.signal();
					} finally {
						lock.unlock();
					}
				}
			});
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(3000);
						service.shutdown(); 
						while (!service.awaitTermination(1, TimeUnit.SECONDS)) {  
							logger.info("线程池没有关闭");  
						}  
						logger.info("线程池已经关闭");
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						Board.close();
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}