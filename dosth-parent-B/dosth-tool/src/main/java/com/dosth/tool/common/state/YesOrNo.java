package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 是/否
 * @author Weifeng.Li
 */
public enum YesOrNo implements IEnumState {
	YSE(1, "是"), NO(2, "否");

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

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (YesOrNo state : YesOrNo.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return YesOrNo.valueOf(name).getMessage();
	}

}
