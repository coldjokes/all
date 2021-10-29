package com.dosth.tool.external.entity;

import java.util.Date;

/**
 * @author Administrator
 *
 */
public class ExternalBorrow {
	//取货人id
	private String accountId;
	//取货人名字
	private String accountName; 
	//取货时间
	private Date opDate; 
	//领取数量
	private Integer borrowNum; 
	//柜子id
	private String equId; 
	//柜子名称
	private String equName; 
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
	//领取源
	private String borrowOrigin;
	
	public ExternalBorrow() {
	}
	
	public ExternalBorrow(String accountId, String accountName, String borrowOrigin, Integer borrowNum, String equId, String equName, String matId,
			String matName, String matSpec, String matBarCode, String supplyID, String supplyFullName) {
		this.accountId = accountId;
		this.accountName = accountName;
		this.borrowOrigin = borrowOrigin;
		this.borrowNum = borrowNum;
		this.equId = equId;
		this.equName = equName;
		this.matId = matId;
		this.matName = matName;
		this.matSpec = matSpec;
		this.matBarCode = matBarCode;
		this.supplyID = supplyID;
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
	public Date getOpDate() {
		return opDate;
	}
	public void setOpDate(Date opDate) {
		this.opDate = opDate;
	}
	public Integer getBorrowNum() {
		return borrowNum;
	}
	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
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
	public String getBorrowOrigin() {
		return borrowOrigin;
	}
	public void setBorrowOrigin(String borrowOrigin) {
		this.borrowOrigin = borrowOrigin;
	}
}
