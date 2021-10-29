package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 
 * @description 确认方式
 * @author guozhidong
 *
 */
public enum VerifyMode implements IEnumState {
	ABNORCONF(1, "异常确认"), SCANCONF(2, "扫描确认"), APPCONF(3, "APP确认"), NORCONF(4, "正常确认");
	private Integer code;
	private String message;

	private VerifyMode(Integer code, String message) {
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
			for (VerifyMode mode : VerifyMode.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return VerifyMode.valueOf(name).getMessage();
	}
}