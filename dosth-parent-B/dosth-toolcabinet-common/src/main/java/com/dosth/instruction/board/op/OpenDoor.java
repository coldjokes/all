package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 开门
 * @author guozhidong
 *
 */
public final class OpenDoor extends LightElectroControl {

	private static final Logger logger = LoggerFactory.getLogger(LightElectroControl.class);

	public OpenDoor() {
		super(Instruction.OPENDOOR);
	}
	
	/**
	 * @description 开门
	 */
	public void start() {
		logger.info("开门");
		super.electro(true);
	}
}