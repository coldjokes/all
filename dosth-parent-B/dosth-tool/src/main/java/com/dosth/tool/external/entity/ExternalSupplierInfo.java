package com.dosth.tool.external.entity;

import java.util.List;

/**
 * 物料信息 
 * 
 * @author chen
 */
public class ExternalSupplierInfo {

	private String manufacturerId; //供应商id
	private String manufacturerName; //供应商名称
	private String contact;//负责人
	private String address; //供应商地址
	private String phone; //供应商电话
	private List<ExternalSupport> supportList; //客服人员信息
	
	public ExternalSupplierInfo(String manufacturerId, String manufacturerName, String contact, String address, 
			String phone, List<ExternalSupport> supportList) {
		this.manufacturerId = manufacturerId;
		this.manufacturerName = manufacturerName;
		this.contact = contact;
		this.address = address;
		this.phone = phone;
		this.supportList = supportList;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<ExternalSupport> getSupportList() {
		return supportList;
	}

	public void setSupportList(List<ExternalSupport> supportList) {
		this.supportList = supportList;
	}

}

