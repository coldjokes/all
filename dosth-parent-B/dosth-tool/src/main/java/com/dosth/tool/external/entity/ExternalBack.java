package com.dosth.tool.external.entity;

import java.util.Date;

public class ExternalBack {
	
	//归还人员id
	private String accountId; 
	//归还人员姓名
	private String accountName;
	//物料id
	private String matId; 
	//物料名称
	private String matName; 
	 //物料规格
	private String matSpec;
	//物料编号
	private String matBarcode; 
	//供应商全称
	private String SupplyFullName; 
	//供应商ID
	private String SupplyID; 
	//归还条形码
	private String backBarCode; 
	//归还数量
	private Integer returnNum; 
	//柜子ID
	private String equId;
	//柜子名称
	private String equName;
	//归还时间
	private Date returnDate; 
	//领用时间
	private Date opDate; 
	//归还状态
	private Boolean status; 
	
	public ExternalBack(String accountId, String accountName, String matId, String matName, String matSpec, String matBarcode, 
			String SupplyFullName, String SupplyID, String backBarCode, Integer returnNum, String equId, String equName, 
			Date returnDate, Date opDate, Boolean status) {
		this.accountId = accountId;
		this.accountName = accountName;
		this.matId = matId;
		this.matName = matName;
		this.matSpec = matSpec;
		this.matBarcode = matBarcode;
		this.SupplyFullName = SupplyFullName;
		this.SupplyID = SupplyID;
		this.backBarCode = backBarCode;
		this.returnNum = returnNum;
		this.equId = equId;
		this.equName = equName;
		this.returnDate = returnDate;
		this.opDate = opDate;
		this.status = status;
	}

	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getMatId() {
		return matId;
	}
	public void setMatId(String matId) {
		this.matId = matId;
	}
	public String getMatName() {
		return matName;
	}
	public void setMatName(String matName) {
		this.matName = matName;
	}
	public String getMatSpec() {
		return matSpec;
	}
	public void setMatSpec(String matSpec) {
		this.matSpec = matSpec;
	}
	public String getMatBarcode() {
		return matBarcode;
	}
	public void setMatBarcode(String matBarcode) {
		this.matBarcode = matBarcode;
	}
	public String getSupplyFullName() {
		return SupplyFullName;
	}
	public void setSupplyFullName(String supplyFullName) {
		SupplyFullName = supplyFullName;
	}
	public String getSupplyID() {
		return SupplyID;
	}
	public void setSupplyID(String supplyID) {
		SupplyID = supplyID;
	}
	public String getBackBarCode() {
		return backBarCode;
	}
	public void setBackBarCode(String backBarCode) {
		this.backBarCode = backBarCode;
	}
	public Integer getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}
	public String getEquId() {
		return equId;
	}
	public void setEquId(String equId) {
		this.equId = equId;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
}
