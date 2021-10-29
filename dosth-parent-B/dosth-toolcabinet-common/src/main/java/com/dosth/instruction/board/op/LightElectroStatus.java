package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 灯和电磁铁状态
 * @author guozhidong
 *
 */
public final class LightElectroStatus extends Board {
	
	private static final Logger logger = LoggerFactory.getLogger(LightElectroStatus.class);
	
	public LightElectroStatus() {
		super(Instruction.LIGHTELECTROSTATUS);
	}
	
	public void readStatus() {
		try {
			logger.info("读取灯和电磁铁的状态");
			super.setData(null);
			super.sendData();
		} catch (Exception e) {
			logger.error("发送读取灯和电磁铁的状态数据异常");
			e.printStackTrace();
		}
	}
}