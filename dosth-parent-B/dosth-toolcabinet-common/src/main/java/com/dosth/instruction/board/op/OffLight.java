package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 取料口关灯
 * @author guozhidong
 *
 */
public final class OffLight extends LightElectroControl {

	private static final Logger logger = LoggerFactory.getLogger(OffLight.class);
	
	public OffLight() {
		super(Instruction.OFFLIGHT);
	}

	/**
	 * @description 取料口关灯
	 */
	public void start() {
		logger.info("取料口关灯");
		super.light1(false);
	}
}