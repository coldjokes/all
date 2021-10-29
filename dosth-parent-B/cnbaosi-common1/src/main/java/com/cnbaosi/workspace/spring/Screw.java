package com.cnbaosi.workspace.spring;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description 弹簧
 * @author guozhidong
 *
 */
public abstract class Screw  extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(Screw.class);
	// 当前层级对象
	private Level level;
	// 按栈号分组集合
	private Map<Byte, Map<String, Lattice>> latticeGroupMap;
	// 领取记录Id
	private String recordId;

	private Lock lock = new ReentrantLock();
	// 伺服驱动器锁
	private Condition servor = lock.newCondition();
	// 马达锁
	private Condition motor = lock.newCondition();
	
	public Screw(Byte groupBoardNo, Byte mainBoardNo, BlockingQueue<Level> levelQueue) {
		super.groupBoardNo = groupBoardNo;
		super.mainBoardNo = mainBoardNo;
		new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					byte boardNo;
					Map<String, Lattice> row;
					while (!levelQueue.isEmpty()) {
						servor.await();
						level = levelQueue.take();
						latticeGroupMap = level.getLatticeGroupMap();
						for (Entry<Byte, Map<String, Lattice>> entry : latticeGroupMap.entrySet()) {
							boardNo = entry.getKey();
							row = entry.getValue();
							for (Entry<String, Lattice> lattice : row.entrySet()) {
								for (Entry<String, RecordStatus> status : lattice.getValue().getStatusMap().entrySet()) {
									motor.await();
									recordId = status.getKey();
									revolve(boardNo, lattice.getValue().getMotorIndex(), status.getValue().getAmount());
								}
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
		}) .start();
	}

	@Override
	public void start() {
		logger.info("启动弹簧");
		lock.lock();
		try {
			servor.signal();
		} finally {
			lock.unlock();
		}
		try {
			logger.info("延时300ms后,启动弹簧");
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

	@Override
	public void getListenerData(byte[] data) {
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		Message message = null;
		if (instructionNo == BoardInstruction.REVOLVE.getInstructionWord()) { // 电机转
			// 57 4B 4C 59 0A 00 90 00 05 XOR
			// 层格子对象
			Map<Byte, Map<String, Lattice>> groupMap = level.getLatticeGroupMap();
			Map<String, Lattice> latticeMap = groupMap.get(boardNo);
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