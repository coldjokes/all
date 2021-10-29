package com.cnbaosi.workspace.recovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.RecoveryAction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.util.SerialTool;
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description 回收柜
 * @author guozhidong
 *
 */
public abstract class Recovery extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(Recovery.class);
	// 翻转口栈号
	private Byte boardNo;
	// 翻转动作
	private RecoveryAction action;
	
	public Recovery(Byte boardNo, RecoveryAction action) {
		super.mainBoardNo = boardNo;
		this.boardNo = boardNo;
		this.action = action;
	}
	
	@Override
	public void start() {
		try {
			logger.info("发送回收口翻转指令:" + this.boardNo + ",动作:" + this.action.getDesc());
			this.recoveryDirection(this.action);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 0x57 0x4B 0x4C 0x59 0x09 0x00 0x93	0x00	XOR
		Message message = new Message(data);
		if (data[7] == 0x00) {
			message.setType(ReturnMsgType.RECOVERY_SUCC);
		} else {
			message.setType(ReturnMsgType.RECOVERY_SUCC);
		}
		receiveMessage(message);
	}

	/**
	 * @description 回收口翻转
	 * @param action 翻转动作
	 * @throws InterruptedException
	 */
	private void recoveryDirection(RecoveryAction action) throws InterruptedException {
		this.putOrderBytes(SerialTool.createSendMsg(this.boardNo, BoardInstruction.REVERSAL, new byte[] { (byte) action.getCode() }));
	}
}