package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 储存设备类型
 */
public enum StorageType implements IEnumState {
	
	MAIN(1, "主柜"), TEMP(0, "暂存柜");
	
	private Integer code;
	private String message;

	private StorageType(Integer code, String message) {
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
			for (StorageType type : StorageType.values()) {
				if (type.getCode().equals(code)) {
					return type.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return StorageType.valueOf(name).getMessage();
	}
}