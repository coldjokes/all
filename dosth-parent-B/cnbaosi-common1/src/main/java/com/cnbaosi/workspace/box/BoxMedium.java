package com.cnbaosi.workspace.box;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.determinant.StorageMediumPicker;
import com.cnbaosi.dto.IndexPair;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.LockBoardMedium;

/**
 * @description 储物柜
 * @author guozhidong
 *
 */
public abstract class BoxMedium extends LockBoardMedium {

	private static final Logger logger = LoggerFactory.getLogger(BoxMedium.class);

	// 键值对列表
	private List<IndexPair> indexPairList;
	// 盒子客户端
	private int boxIndex;
	
	private Lock lock = new ReentrantLock();
	// 控制下一个锁
	private Condition next = lock.newCondition();

	/**
	 * @description 储物柜对象
	 * @param groupBoardNo 分组栈号
	 * @param boardNo 储物柜栈号
	 * @param indexPairList 键值对列表
	 */
	public BoxMedium(Byte groupBoardNo, Byte boardNo, List<IndexPair> indexPairList) {
		super(boardNo);
		super.groupBoardNo = groupBoardNo;
		this.indexPairList = indexPairList;
	}

	public List<IndexPair> getIndexPairList() {
		return this.indexPairList;
	}

	@Override
	public void start() {
		lock.lock();
		try {
			for (IndexPair indexPair : indexPairList) {
				this.boxIndex = indexPair.getBoxIndex();
				logger.info("开启" + this.boxIndex + "号抽屉");
				super.openSingle(indexPair.getLockIndex());
				next.await();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 57 4B 4C 59 0B 03 82 00 01 00 82
		lock.lock();
		try {
			Message message = new Message(data);
			if (data[7] == 0x00) { // 打开成功
				message.setType(ReturnMsgType.OPEN_SUCC);
				message.setCustomMsg("第" + this.boxIndex + "号抽屉已打开");
			} else {
				message.setType(ReturnMsgType.OPEN_FAIL);
				message.setCustomMsg("第" + this.boxIndex + "号抽屉打开失败");
			}
			message.setHexString(String.valueOf(this.boxIndex));
			receiveMessage(message);
			// 设置当前储物柜已经校验
			StorageMediumPicker.boxStatusMap.get(super.getGroupBoardNo()).put(this.boxIndex, true);
			super.checkOpenStatus(data);
			next.signal();
		} finally {
			lock.unlock();
		}
	}
}