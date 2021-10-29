package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 出入库标识
 * 
 * @author guozhidong
 *
 */
public enum InOutSta implements IEnumState {

	IN(1, "入库"), OUT(0, "出库");
	private Integer code;
	private String message;

	private InOutSta(Integer code, String message) {
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
			for (InOutSta inOutSta : InOutSta.values()) {
				if (inOutSta.getCode().equals(code)) {
					return inOutSta.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return InOutSta.valueOf(name).getMessage();
	}
}