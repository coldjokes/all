package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.db.entity.BasePojo;

/**
 * 刀具柜配置
 * 
 * @author WeiFeng.Li
 *
 */
@Entity
@SuppressWarnings("serial")
public class CabinetSetup extends BasePojo {

	@Column(name = "EQU_SETTING_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String equSettingId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_ID", insertable = false, updatable = false)
	private EquSetting equSetting;

	@Column(columnDefinition = "varchar(36) COMMENT '参数名'")
	private String setupKey;

	@Column(columnDefinition = "varchar(36) COMMENT '参数值'")
	private String setupValue;

	public CabinetSetup() {
		super();
	}

	public CabinetSetup(String equSettingId, String setupKey, String setupValue) {
		super();
		this.equSettingId = equSettingId;
		this.setupKey = setupKey;
		this.setupValue = setupValue;
	}

	public String getEquSettingId() {
		return equSettingId;
	}

	public void setEquSettingId(String equSettingId) {
		this.equSettingId = equSettingId;
	}

	public EquSetting getEquSetting() {
		return equSetting;
	}

	public void setEquSetting(EquSetting equSetting) {
		this.equSetting = equSetting;
	}

	public String getSetupKey() {
		return setupKey;
	}

	public void setSetupKey(String setupKey) {
		this.setupKey = setupKey;
	}

	public String getSetupValue() {
		return setupValue;
	}

	public void setSetupValue(String setupValue) {
		this.setupValue = setupValue;
	}

}
