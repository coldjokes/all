package com.cnbaosi.dto;

import java.io.Serializable;

/**
 * 
 * @description 操作提示信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class OpTip implements Serializable {
	private int code = 200; // 操作码
	private String message; // 操作信息

	public OpTip() {
	}

	public OpTip(String message) {
		this.message = message;
	}

	public OpTip(int code, String message) {
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