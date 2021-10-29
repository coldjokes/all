package com.cnbaosi.workspace.tmp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.ElecLock;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.util.SoundPlayUtil;
import com.cnbaosi.workspace.LockBoardMedium;

/**
 * @description 暂存柜
 * @author guozhidong
 *
 */
public abstract class TmpMedium extends LockBoardMedium {
	private static final Logger logger = LoggerFactory.getLogger(TmpMedium.class);
	// 电磁锁
	private ElecLock elecLock;
	
	public TmpMedium(Byte boardNo, ElecLock elecLock) {
		super(boardNo);
		this.elecLock = elecLock;
	}

	@Override
	public void start() {
		try {
			logger.info("开启" + this.elecLock.getBoxIndex() + "号抽屉");
			super.openSingle(this.elecLock.getLockIndex());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 57 4B 4C 59 0B 03 82 00 01 00 82
		Message message = new Message(data);
		if (data[7] == 0x00) { // 打开成功
			message.setType(ReturnMsgType.OPEN_SUCC);
			SoundPlayUtil.play(this.elecLock.getSoundPath());
			message.setCustomMsg("第" +this.elecLock.getBoxIndex() + "号抽屉已打开");
		} else {
			message.setType(ReturnMsgType.OPEN_FAIL);
			message.setCustomMsg("第" + this.elecLock.getBoxIndex() + "号抽屉打开失败");
		}
		message.setHexString(String.valueOf(this.elecLock.getBoxIndex()));
		receiveMessage(message);
	}
}