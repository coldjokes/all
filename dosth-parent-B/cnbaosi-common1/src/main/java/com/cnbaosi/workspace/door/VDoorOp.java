package com.cnbaosi.workspace.door;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;

/**
 * @description 微型柜门操作(开锁)
 * @author guozhidong
 *
 */
public abstract class VDoorOp extends VDoorReader {

	public VDoorOp(Byte boardNo) {
		super(boardNo);
	}

	@Override
	protected void next(byte[] data) {
		// 57 4B 4C 59 0B 00 83 00 01 01 81
		Message message = new Message(data);
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		if (instructionNo == BoardInstruction.READSINGLE.getInstructionWord()) { // 读取电磁锁
			if (data[7] == 0x00) { // 状态成功
				if (data[9] == 0x01) { // 开门状态
					message.setType(ReturnMsgType.OPENED);
				} else { // 门处于关闭状态,发送开锁指令
					message.setType(ReturnMsgType.UNRECEIVED);
					try {
						super.openSingle(boardNo, 1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				message.setType(ReturnMsgType.READ_FAIL);
			}
		} else if (instructionNo == BoardInstruction.OPENSINGLE.getInstructionWord()) { // 打开电磁锁
			// 57 4B 4C 59 0B 00 82 00 01 01 80
			if (data[7] == 0x00) {
				if (data[9] == 0x00) { // 状态成功且开门状态
					message.setType(ReturnMsgType.OPEN_SUCC);
				} else { // 门处于关闭状态,发送开锁指令
					message.setType(ReturnMsgType.OPEN_FAIL);
				}
			} else {
				message.setType(ReturnMsgType.OPEN_FAIL);
			}
		}
		receiveMessage(message);
	}
}