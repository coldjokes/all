package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 波特率
 * @author Weifeng.Li
 */
public enum BaudType implements IEnumState {
	BAUD_9600(1, "9600"), BAUD_19200(2, "19200"), BAUD_115200(3, "115200");

	private Integer code;
	private String message;

	private BaudType(Integer code, String message) {
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
			for (BaudType state : BaudType.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return BaudType.valueOf(name).getMessage();
	}

}
