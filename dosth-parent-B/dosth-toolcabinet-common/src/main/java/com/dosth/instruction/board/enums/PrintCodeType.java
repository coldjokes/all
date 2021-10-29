package com.dosth.instruction.board.enums;

/**
 * @description 打印码
 * @author guozhidong
 *
 */
public enum PrintCodeType {
	/**
	 * @description 二维码
	 */
	QR("二维码"),
	/**
	 * @description 条形码
	 */
	BAR("条形码");

	// 描述
	private String desc;

	private PrintCodeType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return this.desc;
	}
}