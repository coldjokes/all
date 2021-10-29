package com.cnbaosi.workspace.hopper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.workspace.spring.Door;

/**
 * @description 料斗复位
 * @author guozhidong
 *
 */
public abstract class HopperReset extends Hopper {

	private static final Logger logger = LoggerFactory.getLogger(HopperReset.class);

	public HopperReset(Byte boardNo, Map<Byte, Door> doorMap) {
		super(boardNo, doorMap);
	}
	
	@Override
	public void next() {
		try {
			logger.info("料斗复位");
			super.servorRest(super.mainBoardNo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}