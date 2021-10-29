package com.dosth.enums;

public enum CabinetType {
	/**
	 * PLC刀具柜
	 */
	KNIFE_CABINET_PLC(1, "PLC刀具柜"),
	/**
	 * 行列式A刀具柜
	 */
	KNIFE_CABINET_DETA(2, "行列式A刀具柜"),
	/**
	 * 行列式B刀具柜
	 */
	KNIFE_CABINET_DETB(3, "行列式B刀具柜"),
	/**
	 * C型柜
	 */
	KNIFE_CABINET_C(4, "C型柜"),
	/**
	 * 副柜
	 */
	SUB_CABINET(5, "副柜"),
	/**
	 * 暂存柜
	 */
	TEM_CABINET(6, "暂存柜"),
	/**
	 * 储物柜
	 */
	STORE_CABINET(7, "储物柜"),
	/**
	 * 虚拟仓
	 */
	VIRTUAL_WAREHOUSE(8, "虚拟仓"),
	/**
	 * 回收柜
	 */
	RECOVERY_CABINET(9, "回收柜"),

	/**
	 * C型柜-A柜
	 */
	KNIFE_CABINET_C_A(10, "C型柜-A柜"),

	/**
	 * C型柜-B柜
	 */
	KNIFE_CABINET_C_B(11, "C型柜-B柜"),
	/**
	 * 可控抽屉柜
	 */
	TROL_DRAWER(12, "可控抽屉柜");

	private Integer code;
	private String message;

	private CabinetType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public String valueOfCode(Integer code) {
		if (code == null) {
			return "";
		} else {
			for (CabinetType type : CabinetType.values()) {
				if (type.getCode() == code) {
					return type.getMessage();
				}
			}
			return "";
		}
	}

	public String valueOfName(String name) {
		return CabinetType.valueOf(name).getMessage();
	}

}
