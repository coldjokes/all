package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @Desc 柜子状态
 * @author guozhidong
 *
 */
public enum CabinetSta implements IEnumState {
	NORMAL(1, "正常"),
	FAULT(2, "故障");
	
	private Integer code;
	private String message;

	private CabinetSta(Integer code, String message) {
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
			for (CabinetSta sta : CabinetSta.values()) {
				if (sta.getCode().equals(code)) {
					return sta.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return CabinetSta.valueOf(name).getMessage();
	}
}