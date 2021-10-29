package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 归还类型
 * @author Weifeng.Li
 */
public enum ReturnWayType implements IEnumState {
	PR(1, "打印"), RE(2, "扫码");

	private Integer code;
	private String message;

	private ReturnWayType(Integer code, String message) {
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
			for (ReturnWayType state : ReturnWayType.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return ReturnWayType.valueOf(name).getMessage();
	}

}
