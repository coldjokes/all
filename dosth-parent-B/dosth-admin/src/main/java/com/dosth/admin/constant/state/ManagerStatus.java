package com.dosth.admin.constant.state;

import com.dosth.common.constant.IEnumState;

/**
 * 管理员的状态
 * 
 * @author guozhidong
 *
 */
public enum ManagerStatus implements IEnumState {

	OK(1, "启用"), FREEZED(2, "冻结"), DELETED(3, "删除");

	private Integer code;
	private String message;

	ManagerStatus(Integer code, String message) {
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
			return "";
		} else {
			for (ManagerStatus status : ManagerStatus.values()) {
				if (status.getCode() == code) {
					return status.getMessage();
				}
			}
			return "";
		}
	}

	@Override
	public String valueOfName(String name) {
		if (name != null) {
			return ManagerStatus.valueOf(name).getMessage();

		}
		return "";
	}
}