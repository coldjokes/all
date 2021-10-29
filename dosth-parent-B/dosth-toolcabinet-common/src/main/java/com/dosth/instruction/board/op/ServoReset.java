package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.enums.Instruction;

/**
 * 
 * @description 伺服器复位
 * @author guozhidong
 *
 */
public final class ServoReset extends Board {

	private static final Logger logger = LoggerFactory.getLogger(ServoReset.class);
	
	public ServoReset() {
		super(Instruction.SERVORESET);
	}
	
	/**
	 * @description 伺服器复位
	 */
	public void reset() {
		super.setData(null);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送伺服器复位指令失败");
			e.printStackTrace();
		}
	}
}