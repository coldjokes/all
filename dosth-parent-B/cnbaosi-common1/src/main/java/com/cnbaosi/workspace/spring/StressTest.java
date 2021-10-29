package com.cnbaosi.workspace.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.DeterminantConstant;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.StorageMedium;

/**
 * @description 压力测试
 * @author guozhidong
 *
 */
public abstract class StressTest extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(StressTest.class);

	private Integer height; // 目标层高度
	private Integer motorIndex; // 目标马达索引
	private Integer doorHeight; // 门高度
	private Integer amount; // 领取数量

	private Boolean doorClosed = false; // 门关闭状态
	private Boolean hopperResetFlag = false; // 料斗复位标识
	private Boolean isFinish = false; // 领料完成

	public StressTest(Byte groupBoardNo, Byte mainBoardNo, Integer motorIndex, Integer amount, Integer height, Integer doorHeight) {
		super.groupBoardNo = groupBoardNo;
		super.mainBoardNo = mainBoardNo;
		this.motorIndex = motorIndex;
		this.amount = amount;
		this.height = height;
		this.doorHeight = doorHeight;
	}

	@Override
	public void start() {
		logger.info("读取状态");
		try {
			super.readStatus(groupBoardNo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
		// 01 关闭 00 开启
		byte boardNo = data[5]; // 栈号
		byte instructionNo = data[6]; // 指令集
		Message message = new Message(data);
		try {
			if (instructionNo == BoardInstruction.READSTATUS.getInstructionWord()) { // 读状态
				if (data[10] == DeterminantConstant.DEVIATION && data[11] == DeterminantConstant.DEVIATION) {
					if (boardNo == groupBoardNo.byteValue()) {
						hopperResetFlag = true;
					}
				}
				if (groupBoardNo.byteValue() == mainBoardNo.byteValue()) { // 标准柜或C柜左边
					if (data[12] == 0x00) { // 门开启
						message.setCustomMsg("门处于开启状态,即将关门");
						super.closeDoor(groupBoardNo);
					} else { // 门已关闭
						if (this.hopperResetFlag) {
							message.setCustomMsg("门处于关闭状态,料斗即将复位");
							super.servorRest(groupBoardNo);
						} else {
							message.setCustomMsg("门处于关闭状态,料斗即将去高度" + this.height);
							super.servor(groupBoardNo, this.height);
						}
					}
				} else { // C柜右边
					if (boardNo == groupBoardNo.byteValue()) { // 主控栈号
						message.setCustomMsg("读取侧柜状态");
						super.readStatus(mainBoardNo);
					} else {
						if (data[12] == 0x00) { // 门开启
							message.setCustomMsg("侧柜取料口打开状态,即将关闭");
							super.closeDoor(mainBoardNo);
						} else {
							if (this.hopperResetFlag) {
								message.setCustomMsg("门处于关闭状态,料斗即将复位");
								super.servorRest(groupBoardNo);
							} else {
								message.setCustomMsg("门处于关闭状态,料斗即将去高度" + this.height);
								super.servor(groupBoardNo, this.height);
							}
						}
					}
				}
			} else if (instructionNo == BoardInstruction.CLOSEDOOR.getInstructionWord()) { // 开关门
				// 57 4B 4C 59 09 00 92 00 92
				if (data[7] == 0x00) { // 关门或开门成功
					doorClosed = true;
					if (isFinish != null && isFinish) { // 取料完成,开门成功
						message.setCustomMsg("取料完成,开门成功");
						message.setType(ReturnMsgType.OPEN_SUCC);
					} else if (doorClosed != null && doorClosed) {
						if (groupBoardNo == mainBoardNo) {
							message.setType(ReturnMsgType.CLOSED);
							message.setCustomMsg("门关闭成功,料斗即将去高度" + this.height);
							super.servor(groupBoardNo, this.height);
						} else {
							message.setType(ReturnMsgType.CLOSED_SUCC);
							message.setCustomMsg("左门关闭完成,检测右门状态");
							super.readStatus(mainBoardNo);
						}
					}
				} else {
					message.setCustomMsg("开关门故障:" + boardNo);
					message.setType(ReturnMsgType.ERR_CODE);
				}
			} else if (instructionNo == BoardInstruction.SERVORESET.getInstructionWord()) { // 伺服驱动器复位
				// 57 4B 4C 59 09 00 95 00 XOR
				if (data[7] == 0x00) { // 伺服驱动器成功
					message.setCustomMsg("料斗复位成功,料斗去托盘位置");
					super.servor(groupBoardNo, this.height);
				} else { // 伺服驱动器失败或伺服报警
					if (data[7] == 0x03) { // 伺服ALM
						message.setType(ReturnMsgType.SERVOR_REST_ALM);
					} else { // 其他归类为失败
						message.setType(ReturnMsgType.SERVOR_RESET_FAIL);
					}
				}
			} else if (instructionNo == BoardInstruction.SERVOCONTROL.getInstructionWord()) { // 料斗
				// 57 4B 4C 59 09 00 94 00 XOR
				if (data[7] == 0x00) {
					message.setType(ReturnMsgType.SERVOR);
					if (this.isFinish != null && this.isFinish) { // 到门口了
						logger.info("到门口了,即将开门");
						message.setCustomMsg("到门口了,即将开门");
						super.openDoor(mainBoardNo);
					} else { // 到层高度
						logger.info("已运行到" + this.height);
						message.setCustomMsg("到目标高度" + this.height + ",即将取料F" + ((this.motorIndex / 12) + 1) + "-" + (this.motorIndex % 12) + ",针脚号:" + this.motorIndex + ",取料数量:" + this.amount);
						super.revolve(mainBoardNo, this.motorIndex, this.amount);
					}
				} else {
					if (data[7] == 0x03) { // 伺服ALM
						message.setType(ReturnMsgType.SERVOR_FAIL);
					} else if (data[7] == 0x01) { // 上限位报警
						message.setType(ReturnMsgType.SERVOR_TOP_ALM);
					} else if (data[7] == 0x02) { // 下限位报警
						message.setType(ReturnMsgType.SERVOR_BOTTOM_ALM);
					} else { // 归类为伺服故障
						message.setType(ReturnMsgType.SERVOR_FAIL);
					}
				}
			} else if (instructionNo == BoardInstruction.REVOLVE.getInstructionWord()) { // 马达
				// 57 4B 4C 59 0A 00 90 00 05 XOR
				if (data[8] == 0) { // 当前货道取完
					this.isFinish = true;
					if (data[7] == 0x00) {
						message.setCustomMsg("取料完成,料斗即将去取料口");
					} else {
						message.setCustomMsg("取料失败,料斗即将去取料口");
					}
					logger.info(message.getCustomMsg());
					servor(groupBoardNo, this.doorHeight);
				} else { // 未取完
					if (data[7] == 0x00) {
						message.setCustomMsg("取料正常,剩余数量:" + data[8]);
					} else {
						message.setCustomMsg("马达异常,剩余数量:" + data[8]);
					}
				}
			}
			receiveMessage(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.error("485通讯异常:" + e.getMessage());
		}
	}
}