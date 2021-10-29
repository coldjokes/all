package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description COM口
 * @author Weifeng.Li
 */
public enum HardwareCom implements IEnumState {
	COM1(1, "COM1"), COM2(2, "COM2"), COM3(3, "COM3"), COM4(4, "COM4"), COM5(5, "COM5"), COM6(6, "COM6"),
	COM7(7, "COM7"), COM8(8, "COM8"),;

	private Integer code;
	private String message;

	private HardwareCom(Integer code, String message) {
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
			for (HardwareCom state : HardwareCom.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return HardwareCom.valueOf(name).getMessage();
	}

}
