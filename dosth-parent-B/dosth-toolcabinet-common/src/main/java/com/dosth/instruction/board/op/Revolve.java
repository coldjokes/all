package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 货道电机旋转
 * @author guozhidong
 *
 */
public final class Revolve extends Board {
	
	private static final Logger logger = LoggerFactory.getLogger(Revolve.class);
	
	public Revolve() {
		super(Instruction.REVOLVE);
	}
	
	/**
	 * @description 货道电机旋转
	 * @param motorIndex 马达索引
	 * @param amount 出货个数
	 */
	public void out(int motorIndex, int amount) {
		logger.info("货道电机旋转");
		super.setData(new Byte[] {(byte) motorIndex, (byte) amount});
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送电机旋转指令失败");
			e.printStackTrace();
		}
	}
}