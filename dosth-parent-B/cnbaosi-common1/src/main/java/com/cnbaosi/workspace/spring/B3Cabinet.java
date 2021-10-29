package com.cnbaosi.workspace.spring;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.util.SerialTool;
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description B3售卖型柜子
 * @author guozhidong
 *
 */
public abstract class B3Cabinet extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(B3Cabinet.class);

	// 取料队列
	private BlockingQueue<Level> levelQueue = new LinkedBlockingQueue<>();
	// 当前层级对象
	private Level level;

	private Lock lock = new ReentrantLock();
	// 伺服驱动器锁
	private Condition servor = lock.newCondition();
	// 马达锁
	private Condition motor = lock.newCondition();
	// 一取料口索引
//	private String latticeId;
	private String recordId;

	/**
	 * @description 弹簧柜对象
	 * @param groupBoardNo 分组栈号
	 * @param mainBoardNo  主控制栈号
	 * @param levelQueue   行取料队列
	 */
	public B3Cabinet(Byte groupBoardNo, Byte mainBoardNo, BlockingQueue<Level> levelQueue) {
		super.groupBoardNo = groupBoardNo;
		super.mainBoardNo = mainBoardNo;
		this.levelQueue = levelQueue;
	}

	@Override
	public void start() {
		logger.info("B3柜启动了");
		try {
			super.readStatus(this.mainBoardNo);
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
						servor(mainBoardNo, level.getRowHeight());
					}
					servor.await();
					// 去门口
					level = null;
					servor(mainBoardNo, StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(mainBoardNo).getDoorHeight());
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
					super.servorRest(mainBoardNo);
				} else {
					lock.lock();
					try {
						servor.signal();
					} finally {
						lock.unlock();
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
						// 设置门已打开,已校验,允许其他门正常开启后,提示正常开启门
						StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setCheck(true);
						StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setOpen(true);
						// 检验开启状态
						super.checkOpenStatus(data);
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
				if (data[8] == 0) { // 当前货道取完
					// 领取完成,设置完成状态
					for(Entry<String, Lattice> lattice : latticeMap.entrySet()) {
						for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
							if (recordId != null && recordId.equals(status.getKey())) {
								notice(status.getKey(), lattice.getKey(), data[7] == 0x00);
								if (data[7] != 0x00) {
									message = new Message(data);
									message.setType(ReturnMsgType.MOTORERR);
									message.setCustomMsg("[" + level.getRowNo() + "-" + lattice.getValue().getColNo() + "]" + message.getType().getDesc());
									receiveMessage(message);
								}
								status.getValue().setIsFinish(true);
								recordId = null;
								break;
							}
						}
						if (recordId == null) {
							break;
						}
					}
					Boolean checkFlag = this.latticeStatusCheck();
					if (checkFlag) {
						// 当前行取料完成,行对象置空
						level = null;
						lock.lock();
						try {
							logger.info("当前行已取完,执行下一行");
							servor.signal();
						} finally {
							lock.unlock();
						}
					} else {
						lock.lock();
						try {
							motor.signal();
						} finally {
							lock.unlock();
						}
					}
				} else { // 未取完
					boolean latticeFlag = data[7] == 0x00;
					for (Entry<String, Lattice> lattice : latticeMap.entrySet()) {
						for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
							if (recordId != null && recordId.equals(status.getKey())) {
								notice(status.getKey(), lattice.getKey(), latticeFlag);
								if (!latticeFlag) {
									message = new Message(data);
									message.setType(ReturnMsgType.MOTORERR);
									message.setCustomMsg("[" + level.getRowNo() + "-" + lattice.getValue().getColNo() + "]" + message.getType().getDesc());
									receiveMessage(message);
								}
								break;
							}
						}
					}
				}
			} 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	 * @description 行运行
	 * @author guozhidong
	 *
	 */
	class LevelWork {
		// 按栈号分组集合
		private Map<Byte, Map<String, Lattice>> latticeGroupMap = level.getLatticeGroupMap();
		// 启动取料
		public void work() {
			// 伺服驱动器
			new Thread(new Runnable() {
				@Override
				public void run() {
					lock.lock();
					try {
						Thread.sleep(300);
						byte boardNo;
						Map<String, Lattice> row;
						for (Entry<Byte, Map<String, Lattice>> entry : latticeGroupMap.entrySet()) {
							boardNo = entry.getKey();
							row = entry.getValue();
							for (Entry<String, Lattice> lattice : row.entrySet()) {
								for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
									motor.await();
									recordId = status.getKey();
									putOrderBytes(SerialTool.createSendMsg(boardNo, BoardInstruction.REVOLVE,
											new byte[] { (byte) lattice.getValue().getMotorIndex(), (byte) status.getValue().getAmount() }));
								}
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} finally {
						lock.unlock();
					}
				}
			}).start();
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					lock.lock();
					try {
						motor.signal();
					} finally {
						lock.unlock();
					}
				}
			}).start();
		}
	}

	/**
	 * @description 取料通讯
	 * @param recordId 借出记录Id
	 * @param staId    弹簧Id
	 * @param params   参数,params[0] true为正常,false为异常
	 */
	public abstract void notice(String recordId, String staId, Boolean... params);
}