package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.db.entity.BasePojo;

/**
 * @Description 刀具柜plc参数设置
 * @Author guozhidong
 */
@Entity
@SuppressWarnings("serial")
public class CabinetPlcSetting extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String cabinetId;

	@Column(name = "PLC_SETTING_ID", columnDefinition = "varchar(36) COMMENT 'plc参数ID'")
	private String plcSettingId;
	@ManyToOne
	@JoinColumn(name = "PLC_SETTING_ID", insertable = false, updatable = false)
	private PlcSetting plcSetting;

	@Column(columnDefinition = "varchar(50) COMMENT '参数值'")
	private String settingVal;

	public CabinetPlcSetting() {
		super();
	}

	public CabinetPlcSetting(PlcSetting plcSetting) {
		this.plcSetting = plcSetting;
	}

	public String getCabinetId() {
		return this.cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public String getPlcSettingId() {
		return this.plcSettingId;
	}

	public void setPlcSettingId(String plcSettingId) {
		this.plcSettingId = plcSettingId;
	}

	public PlcSetting getPlcSetting() {
		return this.plcSetting;
	}

	public void setPlcSetting(PlcSetting plcSetting) {
		this.plcSetting = plcSetting;
	}

	public String getSettingVal() {
		return this.settingVal;
	}

	public void setSettingVal(String settingVal) {
		this.settingVal = settingVal;
	}
}