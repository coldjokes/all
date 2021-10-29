package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 
 * @description 确认方式
 * @author guozhidong
 *
 */
public enum AuditStatus implements IEnumState {
	PASS(1, "通过"), NOT_PASS(2, "未通过"), NO_CHECK(3, "未审核");
	private Integer code;
	private String message;

	private AuditStatus(Integer code, String message) {
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
			for (AuditStatus mode : AuditStatus.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return AuditStatus.valueOf(name).getMessage();
	}
}