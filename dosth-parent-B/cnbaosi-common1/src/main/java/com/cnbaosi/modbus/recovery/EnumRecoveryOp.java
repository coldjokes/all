package com.cnbaosi.modbus.recovery;

/**
 * @description 回收口操作枚举
 * @author guozhidong
 *
 */
public enum EnumRecoveryOp {
	/**
	 * @description 回收口Ⅰ复位
	 */
	RESET(1, "回收口复位"),
	/**
	 * @description 回收口左翻
	 */
	TURNLEFT(2, "回收口左翻"),
	/**
	 * @description 回收口Ⅰ右翻
	 */
	TURNRIGHT(3, "回收口右翻"),
	/**
	 * @description 回收口Ⅱ复位
	 */
	SECRESET(11, "回收口Ⅱ复位"),
	/**
	 * @description 回收口Ⅱ右翻
	 */
	SECTURNRIGHT(12, "回收口Ⅱ右翻"),
	/**
	 * @description 回收口Ⅱ右翻
	 */
	SECTURNLEFT(13, "回收口Ⅱ右翻");

	private int code;
	private String desc;

	private EnumRecoveryOp(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}