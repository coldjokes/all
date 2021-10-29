package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 是/否
 * @author chenlei
 */
public enum TrueOrFalse implements IEnumState {
	TRUE(1, "是"), FALSE(2, "否");

	private Integer code;
	private String message;

	private TrueOrFalse(Integer code, String message) {
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
			for (TrueOrFalse state : TrueOrFalse.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return TrueOrFalse.valueOf(name).getMessage();
	}

}
