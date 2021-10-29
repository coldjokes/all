package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 入库单补料
 * @author chenlei
 *
 */
@SuppressWarnings("serial")
public class FeignWarehouseFeed implements Serializable {
	private String feedingListId; // 单据号(刀具库的补料单号)
	private String orderNo; // 入库单号
	private Integer feedNum; // 补料数量
	private String companyNo; // 公司编号
	private String stockNo; // 刀具柜号
	private String matNo; // 物料号
	public String getFeedingListId() {
		return feedingListId;
	}
	public void setFeedingListId(String feedingListId) {
		this.feedingListId = feedingListId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Integer getFeedNum() {
		return feedNum;
	}
	public void setFeedNum(Integer feedNum) {
		this.feedNum = feedNum;
	}
	public String getCompanyNo() {
		return companyNo;
	}
	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}
	public String getStockNo() {
		return stockNo;
	}
	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}
	public String getMatNo() {
		return matNo;
	}
	public void setMatNo(String matNo) {
		this.matNo = matNo;
	}
	
}