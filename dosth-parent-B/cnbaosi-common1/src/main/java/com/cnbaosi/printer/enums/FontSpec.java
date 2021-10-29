package com.cnbaosi.printer.enums;

/**
 * @description 打印文本字体规格
 * @author guozhidong
 *
 */
public enum FontSpec {
	/**
	 * @description 正常
	 */
	NORMAL((byte) 0x00, "正常"), 
	/**
	 * @description 旋转90°
	 */
	RIGHTANGLE((byte)0x10, "旋转90°"),
	/**
	 * @description 放大一倍
	 */
	BOLD((byte) 0x01, "粗体"), 
	/**
	 * @description 下划线
	 */
	UNSERLINE((byte) 0x02, "下划线"), 
	/**
	 * @description 下划线加黑
	 */
	BOLDUNDERLINE((byte) 0x03, "下划线加黑"), 
	/**
	 * @description 反白
	 */
	TURNWHITE((byte) 0x04, "反白");

	private byte code;
	private String desc;

	private FontSpec(byte code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public byte getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}