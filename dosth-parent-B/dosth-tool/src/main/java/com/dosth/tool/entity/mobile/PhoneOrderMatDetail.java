package com.dosth.tool.entity.mobile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;

/**
 * 
 * @description 手机预约物料明细
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class PhoneOrderMatDetail extends BasePojo {

	@Column(name = "PHONE_ORDER_MAT_ID", columnDefinition = "varchar(36) COMMENT '订单物料ID'")
	private String phoneOrderMatId;
	@ManyToOne
	@JoinColumn(name = "PHONE_ORDER_MAT_ID", insertable = false, updatable = false)
	private PhoneOrderMat phoneOrderMat;

	@Column(name = "EQU_SETTING_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String cabinetId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "主柜", isForeign = true)
	private EquSetting equSetting;

	@PageTableTitle(value = "货位")
	@Column(name = "EQU_DETAIL_STA_ID", columnDefinition = "varchar(36) COMMENT '刀具柜货位ID'")
	private String equDetailStaId;
	@ManyToOne
	@JoinColumn(name = "EQU_DETAIL_STA_ID", insertable = false, updatable = false)
	private EquDetailSta equDetailSta;

	@PageTableTitle(value = "层数")
	@Column(columnDefinition = "int(11) COMMENT '层数'")
	private int rowNo;

	@PageTableTitle(value = "列数")
	@Column(columnDefinition = "int(11) COMMENT '列数'")
	private int colNo;

	@PageTableTitle(value = "数量")
	@Column(columnDefinition = "int(11) COMMENT '数量'")
	private int num;

	public PhoneOrderMatDetail() {
	}

	public PhoneOrderMatDetail(String phoneOrderMatId, String cabinetId, String equDetailStaId, int rowNo, int colNo,
			int num) {
		this.phoneOrderMatId = phoneOrderMatId;
		this.cabinetId = cabinetId;
		this.equDetailStaId = equDetailStaId;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.num = num;
	}

	public String getPhoneOrderMatId() {
		return this.phoneOrderMatId;
	}

	public void setPhoneOrderMatId(String phoneOrderMatId) {
		this.phoneOrderMatId = phoneOrderMatId;
	}

	public PhoneOrderMat getPhoneOrderMat() {
		return this.phoneOrderMat;
	}

	public void setPhoneOrderMat(PhoneOrderMat phoneOrderMat) {
		this.phoneOrderMat = phoneOrderMat;
	}

	public String getCabinetId() {
		return this.cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public EquSetting getEquSetting() {
		return this.equSetting;
	}

	public void setEquSetting(EquSetting equSetting) {
		this.equSetting = equSetting;
	}

	public String getEquDetailStaId() {
		return this.equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public EquDetailSta getEquDetailSta() {
		return equDetailSta;
	}

	public void setEquDetailSta(EquDetailSta equDetailSta) {
		this.equDetailSta = equDetailSta;
	}

	public int getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getColNo() {
		return this.colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public int getNum() {
		return this.num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}