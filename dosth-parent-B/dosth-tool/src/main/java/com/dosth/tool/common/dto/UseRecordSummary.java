package com.dosth.tool.common.dto;

public class UseRecordSummary {

//	@PageTableTitle(value = "人员Id")
	private String accountId;
	
//	@PageTableTitle(value = "人员名")
	private String userName;
	
//	@PageTableTitle(value = "物料Id")
	private String matInfoId;
	
//	@PageTableTitle(value = "编号")
	private String barCode;
	
//	@PageTableTitle(value = "物料名称")
	private String matInfoName;

//	@PageTableTitle("领取单位")
	private String borrowUnit;

//	@PageTableTitle("领取数量")
	private Integer borrowNum;
	
//	@PageTableTitle("待还数量")
	private Integer waitBackNum;

//	@PageTableTitle("供应商")
	private String supplierName;

//	@PageTableTitle("品牌")
	private String brand;
	
//	@PageTableTitle("规格")
	private String spec;

//	@PageTableTitle(value = "单价（元）")
	private Float price;

//	@PageTableTitle(value = "金额（元）")
	private Float money;
	
//	@PageTableTitle(value = "包装数量 ")
	private Integer packNum;
	
	public UseRecordSummary() {
		super();
	}

	public UseRecordSummary(String barCode, String matInfoName, String borrowUnit, Integer borrowNum,
			String supplierName, String brand, String spec, Float price, Float money, Integer packNum) {
		this.barCode = barCode;
		this.matInfoName = matInfoName;
		this.borrowUnit = borrowUnit;
		this.borrowNum = borrowNum;
		this.supplierName = supplierName;
		this.brand = brand;
		this.spec = spec;
		this.price = price;
		this.money = money;
		this.packNum = packNum;
	}
	
	public UseRecordSummary(String accountId, String userName, String matInfoId, String barCode, String matInfoName, 
			String spec, String borrowUnit, Integer waitBackNum) {
		this.accountId = accountId;
		this.userName = userName;
		this.matInfoId = matInfoId;
		this.barCode = barCode;
		this.matInfoName = matInfoName;
		this.spec = spec;
		this.borrowUnit = borrowUnit;
		this.waitBackNum = waitBackNum;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getMatInfoName() {
		return matInfoName;
	}

	public void setMatInfoName(String matInfoName) {
		this.matInfoName = matInfoName;
	}

	public String getBorrowUnit() {
		return borrowUnit;
	}

	public void setBorrowUnit(String borrowUnit) {
		this.borrowUnit = borrowUnit;
	}

	public Integer getBorrowNum() {
		return borrowNum;
	}

	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getMoney() {
		return money;
	}

	public void setMoney(Float money) {
		this.money = money;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getWaitBackNum() {
		return waitBackNum;
	}

	public void setWaitBackNum(Integer waitBackNum) {
		this.waitBackNum = waitBackNum;
	}

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}
	
	public Integer getPackNum() {
		return packNum;
	}
	
	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}
}