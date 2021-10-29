package com.dosth.common.exception;

/**
 * 异常类
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class DoSthException extends RuntimeException {

	// 友好提示的code码
	protected int friendlyCode;

	// 友好提示
	protected String friendlyMsg;

	// 业务异常跳转的页面
	protected String urlPath;

	public DoSthException(int friendlyCode, String friendlyMsg, String urlPath) {
		this.setValues(friendlyCode, friendlyMsg, urlPath);
	}

	public DoSthException(DoSthExceptionEnum bizExceptionEnum) {
		this.setValues(bizExceptionEnum.getCode(), bizExceptionEnum.getMessage(), bizExceptionEnum.getUrlPath());
	}

	private void setValues(int friendlyCode, String friendlyMsg, String urlPath) {
		this.friendlyCode = friendlyCode;
		this.friendlyMsg = friendlyMsg;
		this.urlPath = urlPath;
	}

	public int getCode() {
		return this.friendlyCode;
	}

	public void setCode(int code) {
		this.friendlyCode = code;
	}

	public String getMessage() {
		return this.friendlyMsg;
	}

	public void setMessage(String message) {
		this.friendlyMsg = message;
	}

	public String getUrlPath() {
		return this.urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
}