package com.cnbaosi.workspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.util.SerialTool;

/**
 * @description 锁控板介质
 * @author guozhidong
 *
 */
public abstract class LockBoardMedium extends StorageMedium {
	
	private static final Logger logger = LoggerFactory.getLogger(LockBoardMedium.class);
	
	public LockBoardMedium(Byte boardNo) {
		super.mainBoardNo = boardNo;
	}
	
	/**
	 * @description 打开暂存柜
	 * @param lockIndex 针脚号
	 * @throws InterruptedException
	 */
	protected void openSingle(int lockIndex) throws InterruptedException {
		logger.info("发送锁控板针脚号:" + lockIndex);
		super.putOrderBytes(SerialTool.createSendMsg(super.mainBoardNo, BoardInstruction.OPENSINGLE, new byte[] { (byte) lockIndex }));
	}
}