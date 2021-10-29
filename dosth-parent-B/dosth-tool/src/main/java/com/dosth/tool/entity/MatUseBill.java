package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 设备详情领用流水
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatUseBill extends BasePojo {

	@Column(name = "MAT_USE_RECORD_ID", columnDefinition = "varchar(36) COMMENT '领用记录ID'")
	private String matUseRecordId;
	@ManyToOne
	@JoinColumn(name = "MAT_USE_RECORD_ID", insertable = false, updatable = false)
	private MatUseRecord matUseRecord;

	@Column(name = "EQU_DETAIL_STA_ID", columnDefinition = "varchar(36) COMMENT '托盘ID'")
	private String equDetailStaId;

	@ManyToOne
	@JoinColumn(name = "EQU_DETAIL_STA_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "设备详情", isForeign = true)
	private EquDetailSta equDetailSta;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;

	@PageTableTitle(value = "物料编号")
	@Column(columnDefinition = "varchar(30) COMMENT '物料编号'")
	private String barCode;

	@PageTableTitle(value = "物料名称")
	@Column(columnDefinition = "varchar(50) COMMENT '物料名称'")
	private String matEquName;

	@PageTableTitle(value = "物料型号")
	@Column(columnDefinition = "varchar(50) COMMENT '物料型号'")
	private String spec;

	@PageTableTitle(value = "供应商")
	@Column(columnDefinition = "varchar(50) COMMENT '供应商'")
	private String manufacturer;

	@PageTableTitle(value = "品牌")
	@Column(columnDefinition = "varchar(50) COMMENT '品牌'")
	private String brand;

	@PageTableTitle("领用数量")
	@Column(columnDefinition = "int(11) COMMENT '领用数量'")
	private Integer borrowNum;

	@Column(name = "STORE_PRICE", precision = 8, scale = 2, columnDefinition = "float COMMENT '价格'")
	@PageTableTitle(value = "价格")
	private Float storePrice;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle("领用时间")
	@Column(columnDefinition = "datetime COMMENT '领用时间'")
	private Date opDate;

	public MatUseBill() {
		setOpDate(new Date());
	}

	public MatUseBill(String matUseRecordId, String equDetailStaId, String matInfoId, String barCode, String matEquName,
			String spec, String manufacturer, String brand, Float storePrice, Integer borrowNum, String accountId) {
		this.matUseRecordId = matUseRecordId;
		this.equDetailStaId = equDetailStaId;
		this.matInfoId = matInfoId;
		this.barCode = barCode;
		this.matEquName = matEquName;
		this.spec = spec;
		this.manufacturer = manufacturer;
		this.brand = brand;
		this.storePrice = storePrice;
		this.borrowNum = borrowNum;
		this.accountId = accountId;
		setOpDate(new Date());
	}

	public String getMatUseRecordId() {
		return this.matUseRecordId;
	}

	public void setMatUseRecordId(String matUseRecordId) {
		this.matUseRecordId = matUseRecordId;
	}

	public MatUseRecord getMatUseRecord() {
		return this.matUseRecord;
	}

	public void setMatUseRecord(MatUseRecord matUseRecord) {
		this.matUseRecord = matUseRecord;
	}

	public String getEquDetailStaId() {
		return this.equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public EquDetailSta getEquDetailSta() {
		return this.equDetailSta;
	}

	public void setEquDetailSta(EquDetailSta equDetailSta) {
		this.equDetailSta = equDetailSta;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getMatEquName() {
		return this.matEquName;
	}

	public void setMatEquName(String matEquName) {
		this.matEquName = matEquName;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Integer getBorrowNum() {
		return this.borrowNum;
	}

	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}

	public Float getStorePrice() {
		return this.storePrice;
	}

	public void setStorePrice(Float storePrice) {
		this.storePrice = storePrice;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Date getOpDate() {
		return this.opDate;
	}

	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
}