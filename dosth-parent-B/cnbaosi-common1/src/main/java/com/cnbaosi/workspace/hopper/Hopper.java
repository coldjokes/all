package com.cnbaosi.workspace.hopper;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.CabinetConstant;
import com.cnbaosi.dto.Message;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.workspace.StorageMedium;
import com.cnbaosi.workspace.spring.Door;

/**
 * @description 料斗
 * @author guozhidong
 *
 */
public abstract class Hopper extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(Hopper.class);
	// 门集合
	protected Map<Byte, Door> doorMap;
	
	public Hopper(Byte boardNo, Map<Byte, Door> doorMap) {
		super.mainBoardNo = boardNo;
		this.doorMap = doorMap;
	}
	
	public Map<Byte, Door> getDoorMap() {
		return this.doorMap;
	}

	@Override
	public void start() {
		try {
			logger.info("料斗运动前门状态验证...");
			for (Entry<Byte, Door> entry : doorMap.entrySet()) {
				super.readStatus(entry.getKey());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		// 57 4B 4C 59 0E 00 96 00 00 00 FF FF 01 90
		// 01 关闭 00 开启
		byte boardNo = data[5]; // 栈号
		byte inNo = data[6]; // 协议号
		Message message = new Message(data);
		if (inNo == BoardInstruction.READSTATUS.getInstructionWord()) { // 读状态
			if (data[12] == 0x01) { // 门关闭状态
				doorMap.get(boardNo).setClosed(true);
			} else { // 门处于非关闭状态
				logger.info("门处于非关闭状态,提示信息,中断下一步操作");
				message.setType(ReturnMsgType.OPENED);
				message.setCustomMsg(this.doorMap.get(boardNo).getDoorName() + "处于打开状态");
				receiveMessage(message);
			}
			Boolean closed = true;
			for (Entry<Byte, Door> entry : doorMap.entrySet()) {
				closed = entry.getValue().getClosed();
				if (!closed) {
					break;
				}
			}
			CabinetConstant.hopperRunningFlag = false;
			if (closed) { // 如果全部关闭了,执行下一步操作
				CabinetConstant.hopperRunningFlag = true;
				next();
			}
		} else if (inNo == BoardInstruction.SERVORESET.getInstructionWord()) { // 伺服复位
			// 57 4B 4C 59 09 00 95 00 XOR
			if (data[7] == 0x00) { // 伺服驱动器成功
				message.setType(ReturnMsgType.SERVOR_RESET_SUCC);
			} else { // 伺服驱动器失败或伺服报警
				message = new Message(data);
				if (data[7] == 0x04) {
					message.setType(ReturnMsgType.TIME_OUT);
					message.setCustomMsg("伺服驱动器复位" + ReturnMsgType.TIME_OUT.getDesc());
				} else if (data[7] == 0x03) { // 伺服ALM
					message.setType(ReturnMsgType.SERVOR_REST_ALM);
				} else { // 其他归类为失败
					message.setType(ReturnMsgType.SERVOR_RESET_FAIL);
				}
			}
			CabinetConstant.hopperRunningFlag = false;
			receiveMessage(message);
		} else if (inNo == BoardInstruction.SERVOCONTROL.getInstructionWord()) {
			// 57 4B 4C 59 09 00 94 00 XOR
			if (data[7] == 0x00) {
				message.setType(ReturnMsgType.SERVOR_OP_SUCC);
			} else {
				message = new Message(data);
				if (data[7] == 0x04) { // 伺服驱动器超时异常
					message.setType(ReturnMsgType.TIME_OUT);
					message.setCustomMsg("伺服驱动器操作" + ReturnMsgType.TIME_OUT.getDesc());
				} else if (data[7] == 0x03) { // 伺服ALM
					message.setType(ReturnMsgType.SERVOR_FAIL);
				} else if (data[7] == 0x01) { // 上限位报警
					message.setType(ReturnMsgType.SERVOR_TOP_ALM);
				} else if (data[7] == 0x02) { // 下限位报警
					message.setType(ReturnMsgType.SERVOR_BOTTOM_ALM);
				} else { // 归类为伺服故障
					message.setType(ReturnMsgType.SERVOR_OP_FAIL);
				}
			}
			CabinetConstant.hopperRunningFlag = false;
			receiveMessage(message);
		}
	}
	
	/**
	 * @description 下一操作步骤
	 */
	public abstract void next();
}