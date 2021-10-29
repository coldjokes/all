package com.cnbaosi.workspace.door;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.ReturnMsgType;

/**
 * @description 微型柜门检测
 * @author guozhidong
 *
 */
public abstract class VDoorCheck extends VDoorReader {

	public VDoorCheck(Byte boardNo) {
		super(boardNo);
	}

	@Override
	protected void next(byte[] data) {
		// 57 4B 4C 59 0B 00 83 00 01 01 81
		Message message = new Message(data);
		if (data[7] == 0x00) {
			if (data[9] == 0x00) { // 状态成功且关门状态
				message.setType(ReturnMsgType.CLOSED);
			} else {
				message.setType(ReturnMsgType.OPENED);
			}
		} else {
			message.setType(ReturnMsgType.READ_FAIL);
		}
		receiveMessage(message);
	}
}