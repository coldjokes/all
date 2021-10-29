package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 设备状态
 * 
 * @author guozhidong
 *
 */
public enum EquSta implements IEnumState {
	NONE (0, "正常") ,MICROSWITCH(1, "微动开关"), OVERLOAD(2, "电流过载"), OPENFAULT(3, "开路故障"), SHORTTROUBLE(4, "短路故障");

	private Integer code;
	private String message;

	private EquSta(Integer code, String message) {
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
			for (EquSta equSta : EquSta.values()) {
				if (equSta.getCode().equals(code)) {
					return equSta.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return EquSta.valueOf(name).getMessage();
	}
}