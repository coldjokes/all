package com.cnbaosi.enums;

/**
 * @description 翻转动作
 * @author guozhidong
 *
 */
public enum RecoveryAction {
	/**
	 * @description 左翻
	 */
	LEFT(0, "左翻"),
	/**
	 * @description 右翻
	 */
	RIGHT(1, "右翻"),
	/**
	 * @description 复位
	 */
	RESET(2, "复位");
	private int code; // 控制动作
	private String desc; // 描述

	private RecoveryAction(int code, String desc) {
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