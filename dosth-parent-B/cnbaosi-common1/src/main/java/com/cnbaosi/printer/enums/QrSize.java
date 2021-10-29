package com.cnbaosi.printer.enums;

/**
 * @description 二维码尺寸
 * @author guozhidong
 *
 */
public enum QrSize {
	/**
	 * @description 普通
	 */
	NORMAL((byte) 0x03, 148, 135, "普通"),
	/**
	 * @description 大号
	 */
	LARGE((byte) 0x04, 150, 150, "大号");

	private Byte code; // 编码
	private int left; // 二维码距左边距
	private int height; // 二维码高度
	private String desc; // 描述

	private QrSize(Byte code, int left, int height, String desc) {
		this.code = code;
		this.left = left;
		this.height = height;
		this.desc = desc;
	}

	public Byte getCode() {
		return this.code;
	}

	public int getLeft() {
		return this.left;
	}

	public int getHeight() {
		return this.height;
	}

	public String getDesc() {
		return this.desc;
	}
}