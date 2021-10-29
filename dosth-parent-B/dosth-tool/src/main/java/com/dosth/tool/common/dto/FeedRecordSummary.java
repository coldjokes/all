package com.dosth.tool.common.dto;

public class FeedRecordSummary {

//	@PageTableTitle(value = "编号")
	private String barCode;
	
//	@PageTableTitle(value = "物料名称")
	private String matInfoName;

//	@PageTableTitle("单位")
	private String unit;

//	@PageTableTitle("补料数量")
	private Integer feedNum;

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

	public FeedRecordSummary() {
		super();
	}

	public FeedRecordSummary(String barCode, String matInfoName, String unit, Integer feedNum,
			String supplierName, String brand, String spec, Float price, Float money) {
		this.barCode = barCode;
		this.matInfoName = matInfoName;
		this.unit = unit;
		this.feedNum = feedNum;
		this.supplierName = supplierName;
		this.brand = brand;
		this.spec = spec;
		this.price = price;
		this.money = money;
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

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getFeedNum() {
		return feedNum;
	}

	public void setFeedNum(Integer feedNum) {
		this.feedNum = feedNum;
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

}