package com.cnbaosi.workspace.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.common.DeterminantConstant;
import com.cnbaosi.dto.ConsumingDetail;
import com.cnbaosi.dto.Message;
import com.cnbaosi.dto.Position;
import com.cnbaosi.enums.BoardInstruction;
import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.util.SerialTool;
import com.cnbaosi.workspace.StorageMedium;

/**
 * 可控抽屉柜
 * 
 * @author guozhidong
 *
 */
public abstract class TrolDrawerCabinet extends StorageMedium {

	private static final Logger logger = LoggerFactory.getLogger(TrolDrawerCabinet.class);
	
	// 抽屉号
	private Integer drawerNo;
	// 打开数量
	private Integer openNum;
	
	public TrolDrawerCabinet(Byte boardNo, Integer drawerNo, Integer openNum) {
		super.mainBoardNo = boardNo;
		this.drawerNo = drawerNo;
		this.openNum = openNum;
	}

	@Override
	public void start() {
		logger.info("读抽屉状态");
		try {
			super.putOrderBytes(read());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void getListenerData(byte[] data) {
		byte boardNo = data[4];
		byte instructionNo = data[5]; // 指令集
		Message message = new Message(data);
		// 先验证上一次命令是否执行完成
		if (instructionNo == DeterminantConstant.BUSY[0] && data[6] == DeterminantConstant.BUSY[1] && data[7] == DeterminantConstant.BUSY[2] && data[8] == DeterminantConstant.BUSY[3]) {
			message.setType(ReturnMsgType.BUSY);
			receiveMessage(message);
		} else {
			// 42 53 43 0b 05 c0 00 00 00 11 8d
			byte instructionWord = data[5]; // 协议号
			if (instructionWord == BoardInstruction.TROREAD.getInstructionWord()) { // 可控抽屉柜读状态
				if (data[6 + drawerNo - 1] == 0x00) { // 正常关闭
					logger.info("[" + boardNo + "-" + this.drawerNo + "]抽屉关闭,直接打开:" + this.openNum);
					try {
						super.putOrderBytes(open());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					logger.warn("[" + boardNo + "-" + this.drawerNo + "]抽屉未关闭");
					message.setType(ReturnMsgType.NO_CLOSE);
					message.setCustomMsg("[" + boardNo+ "-" + this.drawerNo + "]抽屉未关闭");
					receiveMessage(message);
				}
			} else
			// 42 53 43 0B 05 C1 01 03 03 00 9C
			if (instructionWord == BoardInstruction.TROOPEN.getInstructionWord()) { // 可控抽屉柜打开
				if (data[9] == 0x00) { // 打开成功
					logger.info("[" + boardNo + "-" + this.drawerNo + "]抽屉打开成功:" + "->" + this.openNum);
					message.setCustomMsg("[" + boardNo + "-" + this.drawerNo + "]抽屉打开成功");
					message.setType(ReturnMsgType.OPEN_SUCC);
				} else if (data[9] == 0x02) { // 打开超时
					logger.info("[" + boardNo + "-" + this.drawerNo + "]抽屉打开超时:" + this.openNum + ",打开实际格数:" + data[8]);
					message.setType(ReturnMsgType.TIME_OUT);
					message.setCustomMsg("[" + boardNo + "-" + this.drawerNo + "]抽屉打开" + data[8] + "/" + data[7]);
				}
				notice(new ConsumingDetail(new Position((int) boardNo, this.drawerNo), (int) data[7], (int) data[8]));
				receiveMessage(message);
			}
		}
	}

	/**
	 * @description 读可控抽屉状态
	 */
	private byte[] read() {
		byte[] bytes = new byte[7];
		for (int i = 0; i < DeterminantConstant.TRO_START.length; i++) {
			bytes[i] = DeterminantConstant.TRO_START[i];
		}
		bytes[3] = BoardInstruction.TROREAD.getSendFrameLength();
		bytes[4] = super.mainBoardNo;
		bytes[5] = BoardInstruction.TROREAD.getInstructionWord();
		bytes[6] = SerialTool.getXor(bytes);
		return bytes;
	}

	/**
	 * @description 打开可控抽屉
	 * @return
	 */
	private byte[] open() {
		byte[] bytes = new byte[9];
		for (int i = 0; i < DeterminantConstant.TRO_START.length; i++) {
			bytes[i] = DeterminantConstant.TRO_START[i];
		}
		bytes[3] = BoardInstruction.TROOPEN.getSendFrameLength();
		bytes[4] = super.mainBoardNo;
		bytes[5] = BoardInstruction.TROOPEN.getInstructionWord();
		bytes[6] = this.drawerNo.byteValue();
		bytes[7] = this.openNum.byteValue();
		bytes[8] = SerialTool.getXor(bytes);
		return bytes;
	}
	
	/**
	 * @description 回调领取详情
	 */
	public abstract void notice(ConsumingDetail detail);
}