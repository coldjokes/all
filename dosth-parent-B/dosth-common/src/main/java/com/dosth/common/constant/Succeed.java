package com.dosth.common.constant;

/**
 * 是否成功
 * 
 * @author guozhidong
 *
 */
public enum Succeed implements IEnumState {

	SUCCESS(1, "成功"), FAIL(0, "失败");

	private Integer code;
	private String message;

	private Succeed(Integer code, String message) {
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

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return "";
		} else {
			for (Succeed succeed : Succeed.values()) {
				if (succeed.getCode() == code) {
					return succeed.getMessage();
				}
			}
			return "";
		}
	}

	@Override
	public String valueOfName(String name) {
		return Succeed.valueOf(name).getMessage();
	}
}