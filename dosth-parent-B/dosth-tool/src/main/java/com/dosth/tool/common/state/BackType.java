package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 
 * @description 归还种别
 * @author chenlei
 *
 */
public enum BackType implements IEnumState {
	PULLOFF(1, "下架"), NORMAL(2, "正常归还"), ABNORMAL(3, "异常归还");
	private Integer code;
	private String message;

	private BackType(Integer code, String message) {
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
			for (BackType mode : BackType.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return BackType.valueOf(name).getMessage();
	}
}