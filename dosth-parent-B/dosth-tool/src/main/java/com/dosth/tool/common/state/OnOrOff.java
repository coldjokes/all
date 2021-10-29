package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 开/关
 * @author chenlei
 */
public enum OnOrOff implements IEnumState {
	ON(0, "开"), OFF(1, "关");

	private Integer code;
	private String message;

	private OnOrOff(Integer code, String message) {
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
			for (OnOrOff state : OnOrOff.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return OnOrOff.valueOf(name).getMessage();
	}

}
