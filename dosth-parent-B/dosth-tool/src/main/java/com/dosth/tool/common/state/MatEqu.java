package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 物料/设备
 * 
 * @author guozhidong
 *
 */
public enum MatEqu implements IEnumState {
	MATERIAL(1, "物料"), STORAGE(3, "储存设备");

	private Integer code;
	private String message;

	private MatEqu(Integer code, String message) {
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
			for (MatEqu matEqu : MatEqu.values()) {
				if (matEqu.getCode().equals(code)) {
					return matEqu.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return MatEqu.valueOf(name).getMessage();
	}
}