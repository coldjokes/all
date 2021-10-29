package com.cnbaosi.dto.tool;

public class ExternalWarehouseFeed {
	//库房号
	private String stockNo;
	//单据号
	private String orderNo; 
	//公司代号
	private String companyNo; 
	//业务类型
	private String type; 
	//物料编码
	private String matNo; 
	//制单数量
	private Integer feedNum; 
	
	public ExternalWarehouseFeed() {
		
	}
	
	public ExternalWarehouseFeed(String stockNo, String orderNo, String companyNo, String type, String matNo, Integer feedNum) {
		this.stockNo = stockNo;
		this.orderNo = orderNo;
		this.companyNo = companyNo;
		this.type = type;
		this.matNo = matNo;
		this.feedNum = feedNum;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMatNo() {
		return matNo;
	}

	public void setMatNo(String matNo) {
		this.matNo = matNo;
	}

	public Integer getFeedNum() {
		return feedNum;
	}

	public void setFeedNum(Integer feedNum) {
		this.feedNum = feedNum;
	}

}
