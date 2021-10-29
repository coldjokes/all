package com.cnbaosi.enums;

/**
 * @description 提示等级
 * @author guozhidong
 *
 */
public enum TipLevel {
	/**
	 * @description 故障
	 */
	ERR(1, "故障"), 
	/**
	 * @description 警告
	 */
	WARN(2, "警告"), 
	/**
	 * @description 信息
	 */
	INFO(3, "信息");

	private int code;
	private String message;

	private TipLevel(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}