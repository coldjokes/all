package com.dosth.tool.external.entity;

import com.dosth.common.constant.UsingStatus;

/**
 * 供应商客服
 * 
 * @author chenlei
 *
 */
public class ExternalSupport {

	private String id;// 客服人员ID

	private String contactName;// 客服人员姓名

	private String contactPhone;// 客服人员电话

	private String mailAddress;// 客服人员邮箱

	private String manufacturerId;// 供应商ID

	private UsingStatus status;// 状态

	private String remark;// 备注

	public ExternalSupport(String id, String contactName, String contactPhone, String mailAddress,
			String manufacturerId, UsingStatus status, String remark) {
		this.id = id;
		this.contactName = contactName;
		this.contactPhone = contactPhone;
		this.mailAddress = mailAddress;
		this.manufacturerId = manufacturerId;
		this.status = status;
		this.remark = remark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
