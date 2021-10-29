package com.cnbaosi.cabinet.entity.modal.dto;

import java.util.Date;
import java.util.List;

public class StockSummaryDto {

	private String materialId; // 物料id
	private String materialName; // 物料名称
	private String materialNo; // 物料编号
	private String materialSpec; // 物料规格
	private String materialPicture; // 物料图片
	private String materialRemark; //物料备注
	private Integer materialWarnVal; //物料库存预警值
	private Integer totalAmount; // 总数量
	private String lastStockOperateUserId; //最后库存操作人id
	private String lastStockOperateUserFullname; //最后库存操作人姓名
	private Date lastStockOperateTime; //最后库存操作时间
	private List<StockDetailDto> stockDetailList; //位置详情

	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialNo() {
		return materialNo;
	}
	public void setMaterialNo(String materialNo) {
		this.materialNo = materialNo;
	}
	public String getMaterialSpec() {
		return materialSpec;
	}
	public void setMaterialSpec(String materialSpec) {
		this.materialSpec = materialSpec;
	}
	public String getMaterialPicture() {
		return materialPicture;
	}
	public void setMaterialPicture(String materialPicture) {
		this.materialPicture = materialPicture;
	}
	public Integer getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount) {
		this.totalAmount = totalAmount;
	}
	public List<StockDetailDto> getStockDetailList() {
		return stockDetailList;
	}
	public void setStockDetailList(List<StockDetailDto> stockDetailList) {
		this.stockDetailList = stockDetailList;
	}
	public String getMaterialRemark() {
		return materialRemark;
	}
	public void setMaterialRemark(String materialRemark) {
		this.materialRemark = materialRemark;
	}
	public Integer getMaterialWarnVal() {
		return materialWarnVal;
	}
	public void setMaterialWarnVal(Integer materialWarnVal) {
		this.materialWarnVal = materialWarnVal;
	}
	public String getLastStockOperateUserId() {
		return lastStockOperateUserId;
	}
	public void setLastStockOperateUserId(String lastStockOperateUserId) {
		this.lastStockOperateUserId = lastStockOperateUserId;
	}
	public String getLastStockOperateUserFullname() {
		return lastStockOperateUserFullname;
	}
	public void setLastStockOperateUserFullname(String lastStockOperateUserFullname) {
		this.lastStockOperateUserFullname = lastStockOperateUserFullname;
	}
	public Date getLastStockOperateTime() {
		return lastStockOperateTime;
	}
	public void setLastStockOperateTime(Date lastStockOperateTime) {
		this.lastStockOperateTime = lastStockOperateTime;
	}
}
