package com.cnbaosi.workspace.spring;

import java.util.List;
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
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description 微型柜
 * @author guozhidong
 *
 */
public abstract class MiniCabinet extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(MiniCabinet.class);
	// 取料行队列
	private BlockingQueue<Level> levelQueue = new LinkedBlockingQueue<>();
	// 锁
	private Lock lock = new ReentrantLock();
	// 弹簧锁
	private Condition conditionLattice = lock.newCondition();
	// 重试次数
	private int retryTime = 0;
	// 行记录
	private Level level;
	// 货道Id
	private String staId;
	// 领取记录Id
	private String recordId;
	
	public MiniCabinet(Byte groupBoardNo, Byte mainBoardNo, List<Level> levelList) {
		super.groupBoardNo = groupBoardNo;
		super.mainBoardNo = mainBoardNo;
		for (Level level : levelList) {
			try {
				levelQueue.put(level);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void start() {
		logger.info("微型柜启动了");
		try {
			logger.info("读取微型柜门状态");
			super.readSingle(this.mainBoardNo, 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 01 关闭 00 开启
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		Message message = new Message(data);
		if (instructionNo == BoardInstruction.READSINGLE.getInstructionWord()) { // 读状态
			// 57 4b 4c 59 0b 00 83 00 01 01 81
			if (data[7] == 0x00 && data[9] == 0x00) { //  data[7] 状态成功,data[9]关门,代表关闭状态
				// 验证门是否全部关上,如全部关上了,则进行下一步
				logger.info("门处于关闭状态,启动弹簧消费队列");
				new Thread(new LatticeQueueConsume()).start();
			} else { // 门非关闭状态
				logger.info("门处于非关闭状态,发送关门指令");
				message.setType(ReturnMsgType.NO_CLOSE);
				receiveMessage(message);
				// 设置门已打开,已校验,允许其他门正常开启后,提示正常开启门
				StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setCheck(true);
				StorageMediumPicker.mainDoorMap.get(groupBoardNo).get(boardNo).setOpen(true);
				// 检验开启状态
				super.checkOpenStatus(data);
			}
		} else if (instructionNo == BoardInstruction.OPENSINGLE.getInstructionWord()) { // 开门
			// 57 4B 4C 59 0B 00 82 00 01 01 80
			if (data[7] == 0x00 && data[9] == 0x01) { // data[7] 状态成功,data[9]开门,代表开门成功
				message.setType(ReturnMsgType.OPEN_SUCC);
				StorageMediumPicker.mainDoorMap.get(super.groupBoardNo).get(boardNo).setOpen(true);
				receiveMessage(message);
				// 检验开启状态
				super.checkOpenStatus(data);
			} else { // 开门失败
				logger.info("开门失败");
				// 失败要重试
				if (retryTime < 3) {
					retryTime++;
					logger.info("开门重试("+retryTime+")");
					try {
						super.openSingle(boardNo, 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} 
				message.setType(ReturnMsgType.OPEN_FAIL);
				message.setCustomMsg("开门失败" + retryTime + "次,即将重试");
				receiveMessage(message);
			}
		} else if (instructionNo == BoardInstruction.REVOLVE.getInstructionWord()) { // 弹簧转
			// 57 4B 4C 59 0A 00 90 00 00 93
			// 完成或异常,均设置成完成
			Map<String, RecordStatus> statusMap;
			Boolean flag = false;
			Map<String, Lattice> latticeMap = level.getLatticeGroupMap().get(mainBoardNo);
			if (data[8] == 0x00) { // 当前弹簧全部取完
				for (Entry<String, Lattice> entry : latticeMap.entrySet()) {
					if (!staId.equals(entry.getKey())) {
						continue;
					}
					statusMap = entry.getValue().getStatusMap();
					for (Entry<String, RecordStatus> status : statusMap.entrySet()) {
						if (status.getValue().getIsFinish() != null && status.getValue().getIsFinish()) {
							continue;
						}
						if (recordId != null && recordId.equals(status.getKey())) {
							notice(recordId, staId, data[7] == 0x00);
							if (data[7] != 0x00) {
								message = new Message(data);
								message.setType(ReturnMsgType.MOTORERR);
								message.setCustomMsg(level.getRowNo() + "-" + entry.getValue().getColNo() + "]" + message.getType().getDesc());
								receiveMessage(message);
							}
							status.getValue().setIsFinish(true);
							lock.lock();
							try {
								conditionLattice.signal();
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
			} else { // 当前弹簧未取完
				for (Entry<String, Lattice> lattice : latticeMap.entrySet()) {
					if (!staId.equals(lattice.getKey())) {
						continue;
					}
					for (Entry<String, RecordStatus> status: lattice.getValue().getStatusMap().entrySet()) {
						if (status.getValue().getIsFinish() != null && status.getValue().getIsFinish()) {
							continue;
						}
						if (recordId != null && recordId.equals(status.getKey())) {
							notice(status.getKey(), staId, data[7] == 0x00);
							if (data[7] != 0x00) {
								message = new Message(data);
								message.setType(ReturnMsgType.MOTORERR);
								message.setCustomMsg(level.getRowNo() + "-" + lattice.getValue().getColNo() + "]" + message.getType().getDesc());
								receiveMessage(message);
							}
							flag = true;
						}
						if (flag) {
							break;
						}
					}
					if (flag) {
						break;
					}
				}
			}
		}
	}

	/**
	 * @description 取料通讯
	 * @param recordId 借出记录Id
	 * @param staId    弹簧Id
	 * @param params   参数,params[0] true为正常,false为异常
	 */
	public abstract void notice(String recordId, String staId, Boolean... params);
	
	/**
	 * @description 格子消费
	 * @author guozhidong
	 */
	class LatticeQueueConsume implements Runnable {
		@Override
		public void run() {
			lock.lock();
			try {
				Map<String, RecordStatus> statusMap;
				while (!levelQueue.isEmpty()) {
					level = levelQueue.take();
					for (Entry<String, Lattice> entry : level.getLatticeGroupMap().get(mainBoardNo).entrySet()) {
						statusMap = entry.getValue().getStatusMap();
						for (Entry<String, RecordStatus> status : statusMap.entrySet()) {
							staId = entry.getKey();
							recordId = status.getKey();
							revolve(mainBoardNo, entry.getValue().getMotorIndex(), status.getValue().getAmount());
							conditionLattice.await();
						}
					}
				}
				logger.info("开门锁延时1s");
				Thread.sleep(1000);
				// 开锁
				openSingle(mainBoardNo, 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}
}