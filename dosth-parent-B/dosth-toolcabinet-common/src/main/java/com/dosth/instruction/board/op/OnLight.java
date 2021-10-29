package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 取料口开灯
 * @author guozhidong
 *
 */
public final class OnLight extends LightElectroControl {

	private static final Logger logger = LoggerFactory.getLogger(OnLight.class);

	public OnLight() {
		super(Instruction.ONLIGHT);
	}

	/**
	 * @description 取料口开灯
	 */
	public void start() {
		logger.info("取料口开灯");
		super.light1(true);
	}
}