package com.dosth.toolcabinet.enums;

/**
 * 读写
 * 
 * @author guozhidong
 *
 */
public enum ReadWrite {
	/**
	 * 只读
	 */
	ReadOnly("R", "只读"),
	/**
	 * 读写
	 */
	ReadWrite("R/W", "读写");

	/**
	 * code
	 */
	private String code;
	/**
	 * 描述
	 */
	private String desc;

	private ReadWrite(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}