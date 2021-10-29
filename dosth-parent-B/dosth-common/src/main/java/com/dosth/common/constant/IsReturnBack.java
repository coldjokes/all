package com.dosth.common.constant;

/**
 * @description 是否归还
 * 
 * @author Weifeng Li
 *
 */
public enum IsReturnBack implements IEnumState {
	ISBACK(1, "已归还"), NOTBACK(2, "未归还");
	private Integer code;
	private String message;

	private IsReturnBack(Integer code, String message) {
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
			return null;
		} else {
			for (IsReturnBack mode : IsReturnBack.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return IsReturnBack.valueOf(name).getMessage();
	}
}