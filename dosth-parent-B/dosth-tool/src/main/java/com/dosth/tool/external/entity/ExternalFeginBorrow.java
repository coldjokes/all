package com.dosth.tool.external.entity;

import java.util.Date;

public class ExternalFeginBorrow {
	//单据号(刀具库的领料单号)
	private String wmsNo;
	//领取数量
	private Integer qtyReal; 
	//领取人
	private String stockEmp; 
	//领取时间
	private Date operatorDate; 
	//物料号
	private String itemNo; 
	//公司编号
	private String companyCode; 
	//刀具柜号
	private String stockNo; 
	
	public ExternalFeginBorrow() {
	}
	
	public ExternalFeginBorrow(String wmsNo, Integer qtyReal, String stockEmp, Date operatorDate, 
			String itemNo, String companyCode, String stockNo) {
		this.wmsNo = wmsNo;
		this.qtyReal = qtyReal;
		this.stockEmp = stockEmp;
		this.operatorDate = operatorDate;
		this.itemNo = itemNo;
		this.companyCode = companyCode;
		this.stockNo = stockNo;
	}

	public String getWmsNo() {
		return wmsNo;
	}

	public void setWmsNo(String wmsNo) {
		this.wmsNo = wmsNo;
	}

	public Integer getQtyReal() {
		return qtyReal;
	}

	public void setQtyReal(Integer qtyReal) {
		this.qtyReal = qtyReal;
	}

	public String getStockEmp() {
		return stockEmp;
	}

	public void setStockEmp(String stockEmp) {
		this.stockEmp = stockEmp;
	}

	public Date getOperatorDate() {
		return operatorDate;
	}

	public void setOperatorDate(Date operatorDate) {
		this.operatorDate = operatorDate;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
}
