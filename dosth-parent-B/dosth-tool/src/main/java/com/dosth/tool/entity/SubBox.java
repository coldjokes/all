package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.CabinetSta;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 副柜格子
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class SubBox extends BasePojo {

	@Column(name = "EQU_SETTING_ID", columnDefinition = "varchar(36) COMMENT '暂存柜ID'")
	private String equSettingId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_ID", insertable = false, updatable = false)
	@PageTableTitle(isForeign = true, value = "暂存柜名称")
	private EquSetting equSetting;

	@Column(columnDefinition = "int(11) COMMENT '栈号'")
	private Integer boardNo;

	@Column(columnDefinition = "int(11) COMMENT '针脚号'")
	private Integer lockIndex;

	@PageTableTitle(value = "索引号")
	@Column(columnDefinition = "int(11) COMMENT '索引号'")
	private Integer boxIndex;

	@PageTableTitle(value = "行号")
	@Column(columnDefinition = "int(11) COMMENT '行号'")
	private Integer rowNo;

	@PageTableTitle(value = "列号")
	@Column(columnDefinition = "int(11) COMMENT '列号'")
	private Integer colNo;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private CabinetSta cabinetSta;

	@PageTableTitle(value = "可存库位")
	@Column(columnDefinition = "int(11) COMMENT '可存库位'")
	private Integer extraNum;

	@Transient
	@PageTableTitle(isForeign = true, value = "当前用户")
	private ViewUser user;

	@Transient
	private String matInfoId; // 物料Id

	@Transient
	@PageTableTitle(value = "物料名称")
	private String matInfo;

	@Transient
	@PageTableTitle(value = "物料编号")
	private String barCode;

	@Transient
	@PageTableTitle(value = "物料型号")
	private String spec;

	@Transient
	@PageTableTitle(value = "供应商")
	private String manufacturerName;

	@Transient
	@PageTableTitle(value = "存储数量")
	private Integer num;

	@Transient
	@PageTableTitle(value = "单价")
	private Float price;

	@Transient
	@PageTableTitle(value = "金额")
	private Float sumPrice;

	@Transient
	@PageTableTitle(value = "单位")
	private String borrowType;

	public SubBox() {
		setCabinetSta(CabinetSta.NORMAL);
	}

	public SubBox(String equSettingId, int rowNo, int colNo, int boxIndex) {
		setCabinetSta(CabinetSta.NORMAL);
		this.equSettingId = equSettingId;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.boxIndex = boxIndex;
	}

	public SubBox(String equSettingId, Integer boardNo, Integer lockIndex, Integer boxIndex, Integer rowNo,
			Integer colNo) {
		setCabinetSta(CabinetSta.NORMAL);
		this.equSettingId = equSettingId;
		this.boardNo = boardNo;
		this.lockIndex = lockIndex;
		this.boxIndex = boxIndex;
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

	public Integer getBoardNo() {
		return this.boardNo;
	}

	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}

	public Integer getLockIndex() {
		return this.lockIndex;
	}

	public void setLockIndex(Integer lockIndex) {
		this.lockIndex = lockIndex;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getColNo() {
		return this.colNo;
	}

	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}

	public Integer getBoxIndex() {
		return this.boxIndex;
	}

	public void setBoxIndex(Integer boxIndex) {
		this.boxIndex = boxIndex;
	}

	public CabinetSta getCabinetSta() {
		return this.cabinetSta;
	}

	public void setCabinetSta(CabinetSta cabinetSta) {
		this.cabinetSta = cabinetSta;
	}

	public Integer getExtraNum() {
		if (this.extraNum == null) {
			this.extraNum = 1;
		}
		return this.extraNum;
	}

	public void setExtraNum(Integer extraNum) {
		this.extraNum = extraNum;
	}

	public ViewUser getUser() {
		return user;
	}

	public void setUser(ViewUser user) {
		this.user = user;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public String getMatInfo() {
		return matInfo;
	}

	public void setMatInfo(String matInfo) {
		this.matInfo = matInfo;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Float sumPrice) {
		this.sumPrice = sumPrice;
	}
	
	public String getBorrowType() {
		return borrowType;
	}
	
	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}
}