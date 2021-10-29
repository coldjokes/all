package com.dosth.tool.external.entity;

public class ExternalDto {
	
	//供应商编码
	private String supplyCode; 
	//供应商ID
	private String supplyID; 
	//供应商全称
	private String supplyFullName; 
	//取货人id
	private String accountId;
	//取货人名字
	private String accountName; 
	//领取数量
	private Integer borrowNum; 
	//领取归还标识 领取：1  归还 2
	private String consumingOrReturn;
	//归还数量
	private Integer returnNum;
	//归还时间
	private String returnDate;
	//物料id
	private String matId; 
	//物料名称
	private String matName; 
	//物料规格
	private String matSpec; 
	//物料编号
	private String matBarCode; 
	//物料版本
	private String partVersion; 
	//取货时间
	private String opDate; 
	//柜子id
	private String equId; 
	//柜子名称
	private String equName;
	
	public String getSupplyCode() {
		return supplyCode;
	}
	public void setSupplyCode(String supplyCode) {
		this.supplyCode = supplyCode;
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
	public Integer getBorrowNum() {
		return borrowNum;
	}
	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}
	public String getConsumingOrReturn() {
		return consumingOrReturn;
	}
	public void setConsumingOrReturn(String consumingOrReturn) {
		this.consumingOrReturn = consumingOrReturn;
	}
	public Integer getReturnNum() {
		return returnNum;
	}
	public void setReturnNum(Integer returnNum) {
		this.returnNum = returnNum;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
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
	public String getPartVersion() {
		return partVersion;
	}
	public void setPartVersion(String partVersion) {
		this.partVersion = partVersion;
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
