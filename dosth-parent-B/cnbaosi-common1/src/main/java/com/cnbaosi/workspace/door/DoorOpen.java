package com.cnbaosi.workspace.door;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 开门协议
 * @author guozhidong
 *
 */
public abstract class DoorOpen extends DoorReader {
	
	private static final Logger logger = LoggerFactory.getLogger(DoorOpen.class);
	// 取料口高度
	private Integer doorHeight;
	// 重试次数
	private int retryTime = 0;
	
	public DoorOpen(Byte boardNo, Map<Byte, Door> doorMap, Integer doorHeight) {
		super(boardNo, doorMap);
		this.doorHeight = doorHeight;
	}

	@Override
	protected void next(byte[] data) {
		// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		Message message = new Message(data);
		if (instructionNo == BoardInstruction.READSTATUS.getInstructionWord()) { //  读状态
			// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
			if (data[12] == 0x01) { // 门关闭状态
				logger.info("门处于关闭状态");
				doorMap.get(boardNo).setOpen(false);
				try {
					logger.info("要执行的是料斗到开门,发送开门指令");					
					super.servor(boardNo, doorHeight);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else { // 门打开状态
				logger.info("门处于开启状态");
				doorMap.get(boardNo).setClosed(true);
				message.setType(ReturnMsgType.OPENED);
				message.setCustomMsg("门处于开启状态");
				receiveMessage(message);
			}
		} else if (instructionNo == BoardInstruction.SERVOCONTROL.getInstructionWord()) {
			// 57 4B 4C 59 09 00 94 00 94
			if (data[7] == 0x00) { // 料斗到达取料口
				try {
					logger.info("料斗到达取料口,发送开门指令");
					super.openDoor(boardNo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				logger.error("私服驱动器异常,直接弹出提示");
				message.setType(ReturnMsgType.SERVOR_FAIL);
				receiveMessage(message);
			}		
		} else if (instructionNo == BoardInstruction.CLOSEDOOR.getInstructionWord()) { // 开门
			// 57 4b 4c 59 09 00 92 00 92
			if (data[7] == 0x00) { // 操作成功
				doorMap.get(boardNo).setOpen(true);
				message.setType(ReturnMsgType.OPEN_SUCC);
				message.setCustomMsg(doorMap.get(boardNo).getDoorName() + message.getType().getDesc());
				receiveMessage(message);
			} else {
				doorMap.get(boardNo).setOpen(false);
				message.setType(ReturnMsgType.OPEN_FAIL);
				message.setCustomMsg(doorMap.get(boardNo).getDoorName() + message.getType().getDesc());
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (retryTime < 3) {
								Thread.sleep(3 * 3000);
								message.setType(ReturnMsgType.OPEN_FAIL);
								message.setCustomMsg("30S后将重试......");
								receiveMessage(message);
								Thread.sleep(30 * 1000);
								openDoor(boardNo);
								retryTime++;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
				receiveMessage(message);
			}
			Boolean checkFlag = true;
			for (Entry<Byte, Door> entry : doorMap.entrySet()) {
				checkFlag = entry.getValue().getOpen();
				if (!checkFlag) {
					break;
				}
			}
			if (checkFlag) {
				message.setCustomMsg(null);
				message.setType(ReturnMsgType.OPENED);
				receiveMessage(message);
			} else {
				if (retryTime >= 3) {
					message.setType(ReturnMsgType.OPEN_FAIL);
					receiveMessage(message);
				}
			}
		}
	}
}