package com.dosth.common.constant;

/**
 * 性别
 * 
 * @author guozhidong
 *
 */
public enum Sex implements IEnumState {
	/**
	 * 男
	 */
	MALE(1, "男"),
	/**
	 * 女
	 */
	FEMALE(0, "女");

	private Integer code;
	private String message;

	private Sex(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return "";
		} else {
			for (Sex s : Sex.values()) {
				if (s.getCode() == code) {
					return s.getMessage();
				}
			}
			return "";
		}
	}

	@Override
	public String valueOfName(String name) {
		return Sex.valueOf(name).getMessage();
	}
}