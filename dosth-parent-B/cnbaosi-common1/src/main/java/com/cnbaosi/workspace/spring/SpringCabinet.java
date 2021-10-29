package com.cnbaosi.workspace.spring;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.util.SerialTool;
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description 弹簧柜
 * @author guozhidong
 *
 */
public abstract class SpringCabinet extends StorageMedium {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringCabinet.class);
	
	// 主柜门集合, key 栈号 value 门
	private Map<Byte, Door> doorMap = null;
	// 重试次数
	private int retryTime = 0;	
	// 门开启状态
	private Boolean doorOpenFlag = true;
	// 取料队列
	private BlockingQueue<Level> levelQueue = new LinkedBlockingQueue<>();
	// 当前层级对象
	private Level level;
	// 伺服驱动器恢复锁
	private Boolean servorReset = false;	
	
	private Lock lock = new ReentrantLock();
	// 伺服驱动器锁
	private Condition servor = lock.newCondition();
	// 马达锁
	private Condition motorA = lock.newCondition();
	private Condition motorB = lock.newCondition();
	// 一取料口索引
	private String latticeIdA;
	private String recordIdA;
	// 另一取料口索引
	private String latticeIdB;
	private String recordIdB;

	/**
	 * @description 弹簧柜对象
	 * @param groupBoardNo 分组栈号
	 * @param mainBoardNo 主控制栈号
	 * @param doorMap 门集合
	 * @param levelQueue 行取料队列
	 */
	public SpringCabinet(Byte groupBoardNo, Byte mainBoardNo, Map<Byte, Door> doorMap, BlockingQueue<Level> levelQueue) {
		super.groupBoardNo = groupBoardNo;
		super.mainBoardNo = mainBoardNo;
		this.doorMap = doorMap;
		this.levelQueue = levelQueue;
	}

	public Map<Byte, Door> getDoorMap() {
		return this.doorMap;
	}

	/**
	 * @description 行运行
	 * @author guozhidong
	 *
	 */
	class LevelWork {
		private ExecutorService service = Executors.newCachedThreadPool();
		// 按栈号分组集合
		private Map<Byte, Map<String, Lattice>> latticeGroupMap = level.getLatticeGroupMap();
		// 栈号列表
		private List<Byte> list = latticeGroupMap.keySet().stream().collect(Collectors.toList());
		
		// 启动取料
		public void work() {
			try {
				// 伺服驱动器A
				service.submit(new Runnable() {
					@Override
					public void run() {
						lock.lock();
						try {
							byte boardNo;
							Map<String, Lattice> lattice;
							// 封装第一组取料信息
							if (list != null && list.size() > 0) {
								boardNo = list.get(0);
								doorMap.get(boardNo).setOpen(false);
								StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setOpen(false);
								lattice = latticeGroupMap.get(boardNo);
								for (Entry<String, Lattice> entry : lattice.entrySet()) {
									for (Entry<String, RecordStatus> status : entry.getValue().getStatusMap().entrySet()) {
										motorA.await();
										latticeIdA = entry.getKey();
										recordIdA = status.getKey();
										putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.REVOLVE, 
												new byte[] { (byte) entry.getValue().getMotorIndex(),  (byte) status.getValue().getAmount() }));
									}
								}
							}
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
							byte boardNo;
							Map<String, Lattice> lattice;
							if (list != null && list.size() > 1) {
								boardNo = list.get(1);
								doorMap.get(boardNo).setOpen(false);
								lattice = latticeGroupMap.get(boardNo);
								for (Entry<String, Lattice> entry : lattice.entrySet()) {
									for (Entry<String, RecordStatus> status : entry.getValue().getStatusMap().entrySet()) {
										motorB.await();
										latticeIdB = entry.getKey();
										recordIdB = status.getKey();
										putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.REVOLVE, 
												new byte[] { (byte) entry.getValue().getMotorIndex(),  (byte) status.getValue().getAmount() }));
									}
								}
							}
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
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						lock.lock();
						try {
							motorA.signal();
							motorB.signal();
						} finally {
							lock.unlock();
						}
					}
				});
			} finally {
				service.shutdown();
			}
		}
	}
	
	@Override
	public void start() {
		logger.info("弹簧柜启动了");
		try {
			for (Entry<Byte, Door> entry : doorMap.entrySet()) {
				super.readStatus(entry.getKey());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					while (!levelQueue.isEmpty()) {
						servor.await();
						level = levelQueue.take();
						Thread.sleep(TIME_WAIT);
						servor(mainBoardNo, level.getRowHeight());
					}
					servor.await();
					// 去门口
					level = null;
					Thread.sleep(TIME_WAIT);
					servor(mainBoardNo, doorMap.get(mainBoardNo).getDoorHeight());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}).start();
	}

	@Override
	public void getListenerData(byte[] data) {
		// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
		// 01 关闭 00 开启
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		Message message = null;
		try {
			if (instructionNo == BoardInstruction.READSTATUS.getInstructionWord()) { // 读状态
				if (data[10] == 0xFF && data[11] == 0xFF) {
					logger.info("料斗不在正位,发送料斗复位指令");
					this.servorReset = true;
				}
				this.doorOpenFlag = false;
				if (data[12] == 0x01) { // 关闭状态
					// 重置重试次数为0
					this.retryTime= 0;
					doorMap.get(boardNo).setClosed(true);
					// 验证门是否全部关上,如全部关上了,则进行下一步
					if (this.doorStatusCheck()) {
						if (this.servorReset) { // 需要复位
							this.servorRest(mainBoardNo);
						} else { // 不需要复位
							lock.lock();
							try {
								servor.signal();
							} finally {
								lock.unlock();
							}
						}
					}
				} else { // 门非关闭状态
					logger.info("门处于非关闭状态,发送关门指令");
					this.closeDoor(boardNo);
				}
			} else if (instructionNo == BoardInstruction.CLOSEDOOR.getInstructionWord()) { // 开关门
				// 57 4B 4C 59 09 00 92 00 92
				if (this.doorOpenFlag != null && this.doorOpenFlag) { // 开门操作
					message = new Message(data);
					if (data[7] == 0x00) {
						message.setType(ReturnMsgType.OPEN_SUCC);
						message.setCustomMsg(doorMap.get(boardNo).getDoorName() + message.getType().getDesc());
						StorageMediumPicker.mainDoorMap.get(super.groupBoardNo).get(boardNo).setOpen(true);
						receiveMessage(message);
						// 检验开启状态
						super.checkOpenStatus(data);
					} else {
						logger.info("开门失败");
					}
				} else { // 关门操作
					if (data[7] == 0x00) {
						logger.info("关门正常,发送料斗第一取料位置层");
						this.doorMap.get(boardNo).setClosed(true);
						this.retryTime = 0;
						// 验证门是否全部关上,如全部关上了,则进行下一步
						if (this.doorStatusCheck()) {
							if (this.servorReset) { // 需要复位
								super.servorRest(mainBoardNo);
							} else { // 不需要复位
								lock.lock();
								try {
									servor.signal();
								} finally {
									lock.unlock();
								}
							}
						}
					} else {
						message = new Message(data);
						message.setType(ReturnMsgType.CLOSED_FAIL);
						if (data[7] == 0x02) {
							logger.info("防夹手报警");
							message.setType(ReturnMsgType.CLIP_HAND_ALARM);
						}
						if (this.retryTime < 3) { // 重试retryTime次
							if (this.retryTime > 1) {
								logger.info("重试关门,等待30s");
								message.setCustomMsg("重试关门,等待30s");
								Thread.sleep(30 * 1000);
							}
							this.closeDoor(boardNo);
						}
						receiveMessage(message);
						this.retryTime++;
					}
				}
			} else if (instructionNo == BoardInstruction.SERVORESET.getInstructionWord()) { // 伺服驱动器复位
				// 57 4B 4C 59 09 00 95 00 XOR
				if (data[7] == 0x00) { // 伺服驱动器成功
					lock.lock();
					try {
						servor.signal();
					} finally {
						lock.unlock();
					}
				} else { // 伺服驱动器失败或伺服报警
					message = new Message(data);
					if (data[7] == 0x03) { // 伺服ALM
						message.setType(ReturnMsgType.SERVOR_REST_ALM);
					} else { // 其他归类为失败
						message.setType(ReturnMsgType.SERVOR_RESET_FAIL);
					}
					message.setCustomMsg(doorMap.get(boardNo).getMediumName() + message.getType().getDesc());
					receiveMessage(message);
					// 设置门已打开,已校验,允许其他门正常开启后,提示正常开启门
					StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setCheck(true);
					StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setOpen(true);
					// 检验开启状态
					super.checkOpenStatus(data);
				}
			} else if (instructionNo == BoardInstruction.SERVOCONTROL.getInstructionWord()) { // 伺服驱动器控制
				// 57 4B 4C 59 09 00 94 00 XOR
				if (data[7] == 0x00) {
					if (levelQueue.isEmpty() && level == null) { // 到门口了
						logger.info("到门口了");
						doorOpenFlag = true;
						Thread.sleep(1000);
						for (Entry<Byte, Door> entry : doorMap.entrySet()) {
							if (entry.getValue().getOpen() != null && !entry.getValue().getOpen()) {
								this.openDoor(entry.getKey());
							}
						}
					} else { // 到层高度
						logger.info("已运行到" + this.level.getRowNo() + "层");
						new LevelWork().work();
					}
				} else {
					message = new Message(data);
					if (data[7] == 0x03) { // 伺服ALM
						message.setType(ReturnMsgType.SERVOR_FAIL);
					} else if (data[7] == 0x01) { // 上限位报警
						message.setType(ReturnMsgType.SERVOR_TOP_ALM);
					} else if (data[7] == 0x02) { // 下限位报警
						message.setType(ReturnMsgType.SERVOR_BOTTOM_ALM);
					} else { // 归类为伺服故障
						message.setType(ReturnMsgType.SERVOR_FAIL);
					}
					message.setCustomMsg(doorMap.get(boardNo).getMediumName() + message.getType().getDesc());
					receiveMessage(message);
					// 设置门已打开,已校验,允许其他门正常开启后,提示正常开启门
					StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setCheck(true);
					StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setOpen(true);
					// 检验开启状态
					super.checkOpenStatus(data);
				}
			} else if (instructionNo == BoardInstruction.REVOLVE.getInstructionWord()) { // 电机转
				// 57 4B 4C 59 0A 00 90 00 05 XOR
				// 层格子对象
				Map<Byte, Map<String, Lattice>> groupMap = level.getLatticeGroupMap();
				Map<String, Lattice> latticeMap = groupMap.get(boardNo);
				StorageMediumPicker.mainDoorMap.get(super.groupBoardNo).get(boardNo).setCheck(true);
				boolean flag = false;
				String staId;
				if (data[8] == 0) { // 当前货道取完
					// 领取完成,设置完成状态
					for(Entry<String, Lattice> lattice : latticeMap.entrySet()) {
						staId = null;
						if ((latticeIdA != null && latticeIdA.equals(lattice.getKey()) || (latticeIdB != null && latticeIdB.equals(lattice.getKey())))) {
							staId = lattice.getKey(); 
						}
						if (staId == null) {
							continue;
						}
						for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
							if (status.getValue().getIsFinish() != null && status.getValue().getIsFinish()) {
								continue;
							}
							if ((recordIdA != null && recordIdA.equals(status.getKey()) || (recordIdB != null && recordIdB.equals(status.getKey())))) {
								notice(status.getKey(), staId, data[7] == 0x00);
								if (data[7] != 0x00) {
									message = new Message(data);
									message.setType(ReturnMsgType.MOTORERR);
									message.setCustomMsg(doorMap.get(boardNo).getMediumName() + "[" + level.getRowNo() + "-" + lattice.getValue().getColNo() + "]" + message.getType().getDesc());
									receiveMessage(message);
								}
								status.getValue().setIsFinish(true);
								flag = true;
								break;
							}
						}
						if (flag) {
							break;
						}
					}
					Boolean checkFlag = this.latticeStatusCheck();
					if (checkFlag) {
						// 当前行取料完成,行对象置空
						level = null;
						lock.lock();
						try {
							servor.signal();
						} finally {
							lock.unlock();
						}
					} else {
						flag = false;
						for (Entry<String, Lattice> lattice : latticeMap.entrySet()) {
							staId = null;
							if ((latticeIdA != null && latticeIdA.equals(lattice.getKey()) || (latticeIdB != null && latticeIdB.equals(lattice.getKey())))) {
								staId = lattice.getKey(); 
							}
							if (staId == null) {
								continue;
							}
							for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
								if (recordIdA != null && recordIdA.equals(status.getKey()) || recordIdB != null && recordIdB.equals(status.getKey())) {
									lock.lock();
									try {
										if (latticeIdA != null && staId.equals(latticeIdA)) {
											motorA.signal();
										}
										if (latticeIdB != null && staId.equals(latticeIdB)) {
											motorB.signal();
										}
									} finally {
										lock.unlock();
									}
									flag = true;
									break;
								}
							}
							if (flag) {
								break;
							}
						}
					}
				} else { // 未取完
					boolean latticeFlag = data[7] == 0x00;
					for (Entry<String, Lattice> lattice : latticeMap.entrySet()) {
						staId = null;
						if ((latticeIdA != null && latticeIdA.equals(lattice.getKey()) || (latticeIdB != null && latticeIdB.equals(lattice.getKey())))) {
							staId = lattice.getKey(); 
						}
						if (staId == null) {
							continue;
						}
						for (Entry<String, RecordStatus> status: lattice.getValue().getStatusMap().entrySet()) {
							if (status.getValue().getIsFinish() != null && status.getValue().getIsFinish()) {
								continue;
							}
							if (recordIdA != null && recordIdA.equals(status.getKey()) || recordIdB != null && recordIdB.equals(status.getKey())) {
								notice(status.getKey(), staId, latticeFlag);
								if (!latticeFlag) {
									message = new Message(data);
									message.setType(ReturnMsgType.MOTORERR);
									message.setCustomMsg(doorMap.get(boardNo).getMediumName() + "[" + level.getRowNo() + "-" + lattice.getValue().getColNo() + "]" + message.getType().getDesc());
									receiveMessage(message);
								}
								flag = true;
								break;
							}
						}
						if (flag) {
							break;
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 监测门状态
	 */
	private Boolean doorStatusCheck() {
		Boolean checkFlag = true;
		for (Entry<Byte, Door> entry : doorMap.entrySet()) {
			if (this.doorOpenFlag != null && this.doorOpenFlag) { // 开门检测
				checkFlag = entry.getValue().getOpen();
			} else {
				checkFlag = entry.getValue().getClosed();
			}
			if (!checkFlag) {
				break;
			}
		}
		return checkFlag;
	}
	
	/**
	 * @description 校验格子完成状态
	 */
	private Boolean latticeStatusCheck() {
		Boolean checkFlag = true;
		Map<Byte, Map<String, Lattice>> groupMap = level.getLatticeGroupMap();
		for(Entry<Byte, Map<String, Lattice>> latticeMap : groupMap.entrySet()) {
			for (Entry<String, Lattice> lattice : latticeMap.getValue().entrySet())	 {
				for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
					checkFlag = status.getValue().getIsFinish();
					if (!checkFlag) {
						break;
					}
				}
				if (!checkFlag) {
					break;
				}
			}
			if (!checkFlag) {
				break;
			}
		}
		return checkFlag;
	}

	/**
	 * @description 取料通讯
	 * @param recordId 借出记录Id
	 * @param staId    弹簧Id
	 * @param params   参数,params[0] true为正常,false为异常
	 */
	public abstract void notice(String recordId, String staId, Boolean... params);
}