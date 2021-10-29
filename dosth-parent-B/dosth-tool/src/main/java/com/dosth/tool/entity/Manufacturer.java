package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;

/**
 * 生产商
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class Manufacturer extends BasePojo {

	@Column(name = "MANUFACTURER_NAME", columnDefinition = "varchar(50) COMMENT '供应商名称'")
	@PageTableTitle(value = "供应商名称")
	private String manufacturerName;

	@Column(name = "MANUFACTURER_NO", columnDefinition = "varchar(30) COMMENT '供应商编号'")
	@PageTableTitle(value = "供应商编号")
	private String manufacturerNo;

	@Column(name = "ADDRESS", columnDefinition = "varchar(200) COMMENT '地址'")
	@PageTableTitle(value = "地址")
	private String address;

	@Column(name = "PHONE", columnDefinition = "varchar(30) COMMENT '联系电话'")
	@PageTableTitle(value = "联系电话")
	private String phone;

	@Column(name = "CONTACT", columnDefinition = "varchar(30) COMMENT '负责人'")
	@PageTableTitle(value = "负责人")
	private String contact;

	@Column(name = "REMARK", columnDefinition = "varchar(500) COMMENT '备注'")
	@PageTableTitle(value = "备注")
	private String remark;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	public String getManufacturerName() {
		return this.manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getManufacturerNo() {
		return manufacturerNo;
	}

	public void setManufacturerNo(String manufacturerNo) {
		this.manufacturerNo = manufacturerNo;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}