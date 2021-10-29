package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 关门
 * @author guozhidong
 *
 */
public final class CloseDoor extends LightElectroControl {

	private static final Logger logger = LoggerFactory.getLogger(LightElectroControl.class);

	public CloseDoor() {
		super(Instruction.CLOSEDOOR);
	}

	/**
	 * @description 关门
	 */
	public void start() {
		logger.info("关门");
		super.electro(false);
	}
}