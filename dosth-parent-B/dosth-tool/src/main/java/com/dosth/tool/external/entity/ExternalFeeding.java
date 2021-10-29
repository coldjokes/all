package com.dosth.tool.external.entity;

public class ExternalFeeding {
	//补货人id
	private String accountId;
	//补货人姓名
	private String accountName; 
	//补货数量
	private Integer feedNum; 
	//补货时间
	private String opDate; 
	//物料id
	private String matId; 
	//物料名称
	private String matName; 
	//物料规格
	private String matSpec; 
	//物料编号
	private String matBarCode; 
	//供应商ID
	private String supplyID; 
	//供应商全称
	private String supplyFullName; 
	//柜子id
	private String equId; 
	//柜子名称
	private String equName;
	
	public ExternalFeeding(String accountId, String accountName, Integer feedNum, String matId, String matName, String matSpec, 
			String matBarCode, String supplyID, String supplyFullName, String equId, String equName, String opDate) {
		this.accountId = accountId;
		this.accountName = accountName;
		this.feedNum = feedNum;
		this.matId = matId;
		this.matName = matName;
		this.matSpec = matSpec;
		this.matBarCode = matBarCode;
		this.supplyID = supplyID;
		this.supplyFullName = supplyFullName;
		this.equId = equId;
		this.equName = equName;
		this.opDate = opDate;
	}
	
	public String getSupplyID() {
		return supplyID;
	}
	public void setSupplyID(String supplyID) {
		this.supplyID = supplyID;
	}
	public String getSupplyFullName() {
		return supplyFullName;
	}
	public void setSupplyFullName(String supplyFullName) {
		this.supplyFullName = supplyFullName;
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
	public Integer getFeedNum() {
		return feedNum;
	}
	public void setFeedNum(Integer feedNum) {
		this.feedNum = feedNum;
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
	public String getMatBarCode() {
		return matBarCode;
	}
	public void setMatBarCode(String matBarCode) {
		this.matBarCode = matBarCode;
	}
	public String getOpDate() {
		return opDate;
	}
	public void setOpDate(String opDate) {
		this.opDate = opDate;
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
	
}
