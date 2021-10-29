package com.cnbaosi.dto.tool;

import java.io.Serializable;
import java.util.List;

/**
 * @description 供应商
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignSupplier implements Serializable {
	private String name; // 供应商名称
	private String serialNo; // 供应商编号
	private String address; // 地址
	private String contact; // 联系人
	private String phone; // 联系电话
	private List<FeignSupporter> supporterList; // 供应商联系人列表

	public FeignSupplier() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact() {
		return this.contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<FeignSupporter> getSupporterList() {
		return this.supporterList;
	}

	public void setSupporterList(List<FeignSupporter> supporterList) {
		this.supporterList = supporterList;
	}
}