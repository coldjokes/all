package com.dosth.common.constant;

/**
 * @description 是否
 * 
 * @author guozhidong
 *
 */
public enum YesOrNo implements IEnumState {
	YES(1, "是"), NO(0, "否");

	private Integer code;
	private String message;

	private YesOrNo(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	public static YesOrNo valueOfMessage(String message) {
		if (message == null) {
			return null;
		} else {
			for (YesOrNo yesOrNo : YesOrNo.values()) {
				if (yesOrNo.getMessage().equals(message)) {
					return yesOrNo;
				}
			}
			return null;
		}
	}
	
	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return "";
		} else {
			for (YesOrNo yesOrNo : YesOrNo.values()) {
				if (yesOrNo.getCode() == code) {
					return yesOrNo.getMessage();
				}
			}
			return "";
		}
	}

	@Override
	public String valueOfName(String name) {
		return YesOrNo.valueOf(name).getMessage();
	}
}