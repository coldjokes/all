package com.cnbaosi.printer.enums;

/**
 * @description 字体大小
 * @author guozhidong
 *
 */
public enum FontSize {
	/**
	 * @description 小号
	 */
	S((byte)0x11, 27, "小号"),
	/**
	 * @description 中号
	 */
	M((byte)0x22, 50, "中号"),
	/**
	 * @description 大号
	 */
	L((byte)0x33, 70, "大号"),
	/**
	 * @description 加大号
	 */
	XL((byte)0x44, 90, "加大号");

	private byte code; // 编码
	private int lineHeight; // 字体行高
	private String desc; // 说明

	private FontSize(byte code, int lineHeight, String desc) {
		this.code = code;
		this.lineHeight = lineHeight;
		this.desc = desc;
	}

	public byte getCode() {
		return this.code;
	}

	public int getLineHeight() {
		return this.lineHeight;
	}

	public String getDesc() {
		return this.desc;
	}
}