package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 
 * @description 归还方式
 * @author chenlei
 *
 */
public enum BackWay implements IEnumState {
	SINGLE(1, "单个"), MULTIPLE(2, "多个");
	private Integer code;
	private String message;

	private BackWay(Integer code, String message) {
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
			for (BackWay mode : BackWay.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return BackWay.valueOf(name).getMessage();
	}
}