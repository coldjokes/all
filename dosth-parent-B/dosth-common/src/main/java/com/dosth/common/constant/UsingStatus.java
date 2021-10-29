package com.dosth.common.constant;

/**
 * 启用状态
 * 
 * @author guozhidong
 *
 */
public enum UsingStatus implements IEnumState {

	ENABLE(1, "启用"), DISABLE(0, "禁用");

	private Integer code;
	private String message;

	private UsingStatus(Integer code, String message) {
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
			for (UsingStatus status : UsingStatus.values()) {
				if (status.getCode() == code) {
					return status.getMessage();
				}
			}
			return "";
		}
	}

	@Override
	public String valueOfName(String name) {
		return UsingStatus.valueOf(name).getMessage();
	}
}