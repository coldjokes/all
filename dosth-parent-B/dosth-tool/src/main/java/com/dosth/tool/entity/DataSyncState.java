package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.DataSyncType;

/**
 * 数据同步状态
 * 
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class DataSyncState extends BasePojo {

	@Column(name = "CABINET_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String cabinetId;
	@ManyToOne
	@JoinColumn(name = "CABINET_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "主柜名称", isForeign = true)
	private EquSetting equSetting;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '同步类型'")
	@PageTableTitle(value = "同步类型", isEnum = true)
	private DataSyncType dataSyncType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '同步时间'")
	private Date syncTime;

	public DataSyncState(String cabinetId, DataSyncType dataSyncType, Date syncTime) {
		this.cabinetId = cabinetId;
		this.dataSyncType = dataSyncType;
		this.syncTime = syncTime;
	}

	public DataSyncState() {
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public EquSetting getEquSetting() {
		return equSetting;
	}

	public void setEquSetting(EquSetting equSetting) {
		this.equSetting = equSetting;
	}

	public DataSyncType getDataSyncType() {
		return dataSyncType;
	}

	public void setDataSyncType(DataSyncType dataSyncType) {
		this.dataSyncType = dataSyncType;
	}

	public Date getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}

}