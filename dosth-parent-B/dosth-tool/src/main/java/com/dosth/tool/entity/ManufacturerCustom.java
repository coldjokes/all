package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;

/**
 * 供应商详情
 * 
 * @author liweifeng
 *
 */
@Entity
@SuppressWarnings("serial")
public class ManufacturerCustom extends BasePojo {

	@Column(columnDefinition = "varchar(20) COMMENT '姓名'")
	@PageTableTitle(value = "姓名")
	private String contactName;

	@Column(columnDefinition = "varchar(20) COMMENT '电话'")
	@PageTableTitle(value = "电话")
	private String contactPhone;

	@Column(columnDefinition = "varchar(200) COMMENT '邮箱'")
	@PageTableTitle(value = "邮箱")
	private String mailAddress;

	@Column(name = "MANUFACTURER_ID", columnDefinition = "varchar(36) COMMENT '供应商ID'")
	private String manufacturerId;
	@ManyToOne
	@JoinColumn(name = "MANUFACTURER_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "供应商", isForeign = true)
	private Manufacturer manufacturer;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Column(name = "REMARK", columnDefinition = "varchar(500) COMMENT '备注'")
	@PageTableTitle(value = "备注")
	private String remark;

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public UsingStatus getStatus() {
		return status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
