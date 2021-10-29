package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 门
 * @author guozhidong
 *
 */
public final class ToDoor extends LightElectroControl {
	
	private static final Logger logger = LoggerFactory.getLogger(ToDoor.class);
	
	public ToDoor() {
		super(Instruction.TODOOR);
	}

	/**
	 * @description 料斗到达门口
	 */
	public void start() {
		logger.info("到达高度：" + doorHeight);
		Byte[] data = new Byte[3];
		data[0] = speed;
		data = toHeight(doorHeight, data);
		super.setData(data);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送料斗到达取料门高度" + doorHeight + "指令失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 根据高度转换成二进制数组
	 * @param height
	 * @param data
	 * @return
	 */
	private Byte[] toHeight(int height, Byte[] data) {
		data[1] = (byte) (height / 256);
		data[2] = (byte) (height % 256);
		return data;
	}
}