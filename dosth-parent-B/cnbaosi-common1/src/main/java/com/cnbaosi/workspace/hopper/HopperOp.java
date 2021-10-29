package com.cnbaosi.workspace.hopper;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.workspace.spring.Door;

/**
 * @description 料斗操作
 * @author guozhidong
 *
 */
public abstract class HopperOp extends Hopper {

	private static final Logger logger = LoggerFactory.getLogger(HopperOp.class);
	
	// 到达高度
	private Integer height;
	
	public HopperOp(Byte boardNo, Map<Byte, Door> doorMap,  Integer height) {
		super(boardNo, doorMap);
		this.height = height;
	}

	@Override
	public void next() {
		try {
			logger.info("料斗到达位置:" + this.height);
			super.servor(super.mainBoardNo, this.height);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}