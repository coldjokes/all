package com.dosth.criteria;

import com.dosth.enums.CabinetType;

public class EquSettingCriteria {

	/**
	 * id
	 */
	private String id;

	/**
	 * 名称
	 */
	private String equSettingName;

	/**
	 * 序列号
	 */
	private String serialNo;

	/**
	 * 类型
	 */
	private CabinetType cabinetType;

	/**
	 * 主柜id
	 */
	private String equSettingParentId;

	/**
	 * 用户id
	 */
	private String accountId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEquSettingName() {
		return equSettingName;
	}

	public void setEquSettingName(String equSettingName) {
		this.equSettingName = equSettingName;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public CabinetType getCabinetType() {
		return cabinetType;
	}

	public void setCabinetType(CabinetType cabinetType) {
		this.cabinetType = cabinetType;
	}

	public String getEquSettingParentId() {
		return equSettingParentId;
	}

	public void setEquSettingParentId(String equSettingParentId) {
		this.equSettingParentId = equSettingParentId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
