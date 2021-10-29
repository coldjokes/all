package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.enums.CabinetType;

/**
 * 锁控板参数
 * 
 * @author Weifeng.Li
 *
 */
@Entity
@SuppressWarnings("serial")
public class LockParam extends BasePojo {

	@Column(name = "EQU_SETTING_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String equSettingId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_ID", insertable = false, updatable = false)
	private EquSetting equSetting;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(36) COMMENT '类型'")
	@PageTableTitle(value = "类型", isEnum = true)
	private CabinetType cabinetType;

	@Column(columnDefinition = "int(11) COMMENT '栈号'")
	private Integer boardNo;

	@Column(columnDefinition = "int(11) COMMENT '行号'")
	private Integer rowNo;

	@Column(columnDefinition = "int(11) COMMENT '列号'")
	private Integer colNo;

	public LockParam() {
		super();
	}

	public LockParam(String equSettingId, CabinetType cabinetType, Integer boardNo, Integer rowNo, Integer colNo) {
		super();
		this.equSettingId = equSettingId;
		this.cabinetType = cabinetType;
		this.boardNo = boardNo;
		this.rowNo = rowNo;
		this.colNo = colNo;
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

	public CabinetType getCabinetType() {
		return cabinetType;
	}

	public void setCabinetType(CabinetType cabinetType) {
		this.cabinetType = cabinetType;
	}

	public Integer getBoardNo() {
		return boardNo;
	}

	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getColNo() {
		return colNo;
	}

	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}

}
