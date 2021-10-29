package com.cnbaosi.workspace.door;

import java.util.Map;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 门检测
 * @author guozhidong
 *
 */
public abstract class DoorCheck extends DoorReader {

	public DoorCheck(Byte boardNo, Map<Byte, Door> doorMap) {
		super(boardNo, doorMap);
	}

	@Override
	protected void next(byte[] data) {
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		Message message = new Message(data);
		if (instructionNo == BoardInstruction.READSTATUS.getInstructionWord()) { //  读状态
			// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
			if (data[12] == 0x01) { // 门关闭状态
				message.setType(ReturnMsgType.CLOSED);
			} else { // 门打开状态
				message.setType(ReturnMsgType.OPENED);
				message.setCustomMsg("门处于开启状态,即将关闭");
				try {
					super.closeDoor(boardNo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			receiveMessage(message);
		} else if (instructionNo == BoardInstruction.CLOSEDOOR.getInstructionWord()) {
			// 57 4b 4c 59 09 00 92 00 92
			if (data[7] == 0x00) {
				message.setType(ReturnMsgType.CLOSED_SUCC);
			} else {
				message.setType(ReturnMsgType.CLOSED_FAIL);
			}
			receiveMessage(message);
		}
	}
}