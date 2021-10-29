package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.enums.Instruction;

/**
 * 
 * @description 伺服电机控制
 * @author guozhidong
 *
 */
public final class ServoControl extends Board {

	private static final Logger logger = LoggerFactory.getLogger(ServoControl.class);

	public ServoControl() {
		super(Instruction.SERVOCONTROL);
	}
	
	private byte speed = 0x01;

	/**
	 * @description 到达层级
	 * @param floor 目标层级
	 */
	public void toFloor(int floor) {
		logger.info("到达层级：" + floor);
		Byte[] data = new Byte[3];
		data[0] = speed;
		data = floorH(floor, data);
		super.setData(data);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送料斗到达层级" + floor + "指令失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * @description 到达高度
	 * @param height 目标高度
	 */
	public void toHeight(int height) {
		logger.info("到达高度：" + height);
		Byte[] data = new Byte[3];
		data[0] = speed;
		data = toHeight(height, data);
		super.setData(data);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送料斗到达层级高度" + height + "指令失败");
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

	/**
	 * @description 根据层数生成高度的二进制数组
	 * @param floor 层级
	 * @return
	 */
	private Byte[] floorH(int floor, Byte[] data) {
		int h = (floor - 1) * 175 + 60;
		return toHeight(h, data);
	}
}