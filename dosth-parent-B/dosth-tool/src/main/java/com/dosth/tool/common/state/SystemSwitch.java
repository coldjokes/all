package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

public enum SystemSwitch implements IEnumState {
	MATERIAL_SWITCH(1, "MATERIAL_SWITCH");
	private Integer code;
	private String message;

	private SystemSwitch(Integer code, String message) {
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
			for (SystemSwitch mode : SystemSwitch.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return SystemSwitch.valueOf(name).getMessage();
	}
}
