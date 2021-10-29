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
 * @description 门操作
 * @author guozhidong
 *
 */
public abstract class DoorOp extends DoorReader {

	private static final Logger logger = LoggerFactory.getLogger(DoorOp.class);
	// 打开标识,默认为打开
	private Boolean openFlag = true;
	// 重试次数
	private int retryTime = 0;
	
	public DoorOp(Byte mainBoardNo, Map<Byte, Door> doorMap, Boolean openFlag) {
		super(mainBoardNo, doorMap);
		this.openFlag = openFlag;
	}

	@Override
	public void next(byte[] data) {
		// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
		byte boardNo = data[5];
		byte instructionNo = data[6]; // 指令集
		Message message = new Message(data);
		if (instructionNo == BoardInstruction.READSTATUS.getInstructionWord()) { //  读状态
			// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
			if (data[12] == 0x01) { // 门关闭状态
				logger.info("门处于关闭状态");
				if (openFlag != null && openFlag) {
					doorMap.get(boardNo).setOpen(false);
					try {
						logger.info("要执行的是开门动作,发送开门指令");
						super.openDoor(boardNo);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					logger.info("要关门,就忽略关门操作步骤");
					doorMap.get(boardNo).setClosed(true);
					message.setType(ReturnMsgType.CLOSED);
					message.setCustomMsg("门处于关闭状态");
					receiveMessage(message);
				}
			} else { // 门打开状态
				logger.info("门处于开启状态");
				if (openFlag != null && openFlag) {
					logger.info("要开门,就忽略开门操作步骤");
					doorMap.get(boardNo).setClosed(true);
					message.setType(ReturnMsgType.OPENED);
					message.setCustomMsg("门处于开启状态");
					receiveMessage(message);
				} else {
					logger.info("要关门,发送关门指令");
					try {
						super.closeDoor(boardNo);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} else if (instructionNo == BoardInstruction.CLOSEDOOR.getInstructionWord()) { // 开关门
			// 57 4b 4c 59 09 00 92 00 92
			if (data[7] == 0x00) { // 操作成功
				if (openFlag != null && openFlag) {
					doorMap.get(boardNo).setOpen(true);
					message.setType(ReturnMsgType.OPEN_SUCC);
				} else {
					doorMap.get(boardNo).setClosed(true);
					message.setType(ReturnMsgType.CLOSED_SUCC);
				}
				message.setCustomMsg(doorMap.get(boardNo).getDoorName() + message.getType().getDesc());
				receiveMessage(message);
			} else {
				if (openFlag != null && openFlag) {
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
				} else {
					doorMap.get(boardNo).setClosed(false);
					message.setType(ReturnMsgType.CLOSED_FAIL);
					message.setCustomMsg(doorMap.get(boardNo).getDoorName() + message.getType().getDesc());
					if (data[7] == 0x02) {
						message.setCustomMsg("防夹手报警");
					}
					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								if (retryTime < 3) {
									Thread.sleep(3 * 3000);
									message.setType(ReturnMsgType.CLOSED_FAIL);
									message.setCustomMsg("30S后将重试......");
									receiveMessage(message);
									Thread.sleep(30 * 1000);
									closeDoor(boardNo);
									retryTime++;
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				receiveMessage(message);
			}
			Boolean checkFlag = true;
			for (Entry<Byte, Door> entry : doorMap.entrySet()) {
				if (this.openFlag != null && this.openFlag) { // 开门检测
					checkFlag = entry.getValue().getOpen();
				} else {
					checkFlag = entry.getValue().getClosed();
				}
				if (!checkFlag) {
					break;
				}
			}
			if (checkFlag) {
				message.setCustomMsg(null);
				if (this.openFlag != null && this.openFlag) { 
					message.setType(ReturnMsgType.OPENED);
				} else {
					message.setType(ReturnMsgType.CLOSED);
				}
				receiveMessage(message);
			} else {
				if (retryTime >= 3) {
					if (this.openFlag != null && this.openFlag) { 
						message.setType(ReturnMsgType.OPEN_FAIL);
					} else {
						message.setType(ReturnMsgType.CLOSED_FAIL);
					}
					receiveMessage(message);
				}
			}
		}
	}
}