package com.dosth.remote;

import java.io.Serializable;

import com.dosth.constant.Constants;

/**
 * 
 * @description 远程响应信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ResponseTip implements Serializable {
	private int code = Constants.OP_SUCCESS; // 操作码
	private String message; // 操作信息

	public ResponseTip() {
	}

	public ResponseTip(String message) {
		this.message = message;
	}

	public ResponseTip(int code, String message) {
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