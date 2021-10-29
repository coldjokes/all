package com.dosth.toolcabinet.enums;

/**
 * 
 * @description 归还类型大类
 * @author guozhidong
 *
 */
public enum ReturnBackType {
	NORMAL("1", "可复用归还"), ABNORMAL("2", "不可复用归还");
	/**
	 * code
	 */
	private String code;
	/**
	 * @descrition 描述
	 */
	private String desc;

	private ReturnBackType(String code, String desc) {
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