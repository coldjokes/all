package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 盘点状态
 * @author Zhidong.Guo
 *
 */
public enum InventoryStatus implements IEnumState {
	/**
	 * @description 盘盈
	 */
	SURPLUS(1, "盘盈"),
	/**
	 * @description 盘亏
	 */
	LOSS(-1, "盘亏"),
	/**
	 * @description 盘平
	 */
	PING(0, "正常");
	
	private Integer code;
	private String message;

	private InventoryStatus(Integer code, String message) {
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
			for (InventoryStatus mode : InventoryStatus.values()) {
				if (mode.getCode().equals(code)) {
					return mode.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return InventoryStatus.valueOf(name).getMessage();
	}
}