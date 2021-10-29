package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 供应商支持人员
 * @author guozhidong
 */
@SuppressWarnings("serial")
public class FeignSupporter implements Serializable {
	private String contactName; // 人员姓名
	private String contactPhone; // 电话
	private String mailAddress; // 邮箱

	public FeignSupporter() {
	}

	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getMailAddress() {
		return this.mailAddress;
	}

	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}
}