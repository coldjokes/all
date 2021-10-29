package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;

/**
 * PLC设置
 * 
 * @author liweifeng
 *
 */
@Entity
@SuppressWarnings("serial")
public class PlcSetting extends BasePojo {

	@Column(columnDefinition = "varchar(200) COMMENT 'PLC名称'")
	@PageTableTitle(value = "PLC名称")
	private String plcName;

	@Column(columnDefinition = "varchar(200) COMMENT 'PLC地址'")
	@PageTableTitle(value = "PLC地址")
	private String address;

	@Column(columnDefinition = "varchar(100) COMMENT '默认值'")
	@PageTableTitle(value = "默认值")
	private String defaultValue;

	@Column(columnDefinition = "varchar(500) COMMENT '备注'")
	@PageTableTitle(value = "备注")
	private String remark;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '默认项'")
	@PageTableTitle(value = "默认项", isEnum = true)
	private YesOrNo isDefault;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Transient
	private String settingId;

	@Transient
	private String settingVal;

	public PlcSetting() {
		setIsDefault(YesOrNo.YES);
		setStatus(UsingStatus.ENABLE);
	}

	public PlcSetting(String id, String plcName, String address, String defaultValue, String settingId,
			String settingVal) {
		this.id = id;
		this.plcName = plcName;
		this.address = address;
		this.defaultValue = defaultValue;
		this.settingId = settingId;
		this.settingVal = settingVal;
	}

	public String getPlcName() {
		return this.plcName;
	}

	public void setPlcName(String plcName) {
		this.plcName = plcName;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public YesOrNo getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(YesOrNo isDefault) {
		this.isDefault = isDefault;
	}

	public UsingStatus getStatus() {
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public String getSettingId() {
		return this.settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public String getSettingVal() {
		return this.settingVal;
	}

	public void setSettingVal(String settingVal) {
		this.settingVal = settingVal;
	}
}