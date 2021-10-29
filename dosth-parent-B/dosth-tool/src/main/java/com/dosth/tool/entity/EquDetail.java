package com.dosth.tool.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 设备详情
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class EquDetail extends BasePojo {

	@Column(name = "EQU_SETTING_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String equSettingId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "设备分配", isForeign = true)
	private EquSetting equSetting;

	@Column(columnDefinition = "varchar(50) COMMENT 'IP'")
	@PageTableTitle(value = "IP")
	private String ip;

	@Column(columnDefinition = "varchar(10) COMMENT '端口'")
	@PageTableTitle(value = "端口")
	private String port;

	@Column(columnDefinition = "int(11) COMMENT '行号'")
	private Integer rowNo;

	@Column(columnDefinition = "int(11) COMMENT '层高'")
	private Integer levelHt;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Transient
	private List<EquDetailSta> staList;

	public EquDetail() {
		setStatus(UsingStatus.ENABLE);
	}

	public EquDetail(String equSettingId, Integer rowNo) {
		this.equSettingId = equSettingId;
		this.rowNo = rowNo;
		setStatus(UsingStatus.ENABLE);
	}

	public String getEquSettingId() {
		return this.equSettingId;
	}

	public void setEquSettingId(String equSettingId) {
		this.equSettingId = equSettingId;
	}

	public EquSetting getEquSetting() {
		return this.equSetting;
	}

	public void setEquSetting(EquSetting equSetting) {
		this.equSetting = equSetting;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getLevelHt() {
		if (this.levelHt == null) {
			this.levelHt = 0;
		}
		return this.levelHt;
	}

	public void setLevelHt(Integer levelHt) {
		this.levelHt = levelHt;
	}

	public UsingStatus getStatus() {
		if (this.status == null) {
			this.status = UsingStatus.ENABLE;
		}
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public List<EquDetailSta> getStaList() {
		if (this.staList == null) {
			this.staList = new ArrayList<>();
		}
		return this.staList;
	}

	public void setStaList(List<EquDetailSta> staList) {
		this.staList = staList;
	}
}