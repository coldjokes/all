package com.dosth.instruction.board.op;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;
import com.dosth.instruction.board.enums.Instruction;

/**
 * @description 灯与电磁铁控制
 * @author guozhidong
 *
 */
public abstract class LightElectroControl extends Board {

	private static final Logger logger = LoggerFactory.getLogger(LightElectroControl.class);

	protected LightElectroControl(Instruction instruction) {
		super(instruction);
	}
	
	/**
	 * @description 操作灯1指令
	 * @param flag 操作状态 true 开启 false 关闭
	 */
	protected void light1(Boolean flag) {
		logger.info("发送操作灯1数据");
		Byte[] data = new Byte[] {0x01, 0x00};
		if (flag != null && flag) {
			data[1] = 0x01;
		}
		super.setData(data);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送开灯1指令失败");
			e.printStackTrace();
		}
	}

	/**
	 * @description 操作灯2指令
	 * @param flag 操作状态 true 开启 false 关闭
	 */
	protected void light2(Boolean flag) {
		logger.info("发送操作灯2数据");
		Byte[] data = new Byte[] {0x02, 0x00};
		if (flag != null && flag) {
			data[1] = 0x01;
		}
		super.setData(data);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送开灯2指令失败");
			e.printStackTrace();
		}
	}

	/**
	 * @description 操作电磁铁指令
	 * @param flag 操作状态 true 开启 false 关闭
	 */
	protected void electro(Boolean flag) {
		logger.info("发送操作电磁铁数据");
		Byte[] data = new Byte[] {0x03, 0x00};
		if (flag != null && flag) {
			data[1] = 0x01;
		}
		super.setData(data);
		try {
			super.sendData();
		} catch (Exception e) {
			logger.error("发送开灯指令失败");
			e.printStackTrace();
		}
	}
}