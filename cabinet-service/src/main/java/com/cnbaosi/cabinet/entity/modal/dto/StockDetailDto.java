package com.cnbaosi.cabinet.entity.modal.dto;

import java.util.Date;

public class StockDetailDto {

	private String materialId; // 物料id
	private String materialName; // 物料名称
	private String materialNo; // 物料编号
	private String materialSpec; // 物料规格
	private String materialPicture; // 物料图片
	private String materialRemark; //物料备注
	private Integer materialWarnVal; //物料库存预警值

	private String lastStockOperateUserId; //最后库存操作人id
	private String lastStockOperateUserFullname; //最后库存操作人姓名
	private Date lastStockOperateTime; //最后库存操作时间
	
	private String stockId; //库存id
	private Integer amount; //库存数量
	private Date updateTime; //库存最后更新时间
	
	private String computerId; //主机id
	private String computerName; //主机名称
	
	private String cabinetId; //柜子id
	private String cabinetName; //柜子名称
	private String computerPort;//主机端口
	private Integer computerBaudRate; //主机端口波特率
	
	private String cellId; // 格口id
	private String cellName; // 格口名称
	private Integer cellPin; // 格口针脚地址
	private Integer cellStack; //栈号
	private Integer cellSort; //格口顺序
	
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
	public String getMaterialRemark() {
		return materialRemark;
	}
	public void setMaterialRemark(String materialRemark) {
		this.materialRemark = materialRemark;
	}
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getComputerId() {
		return computerId;
	}
	public void setComputerId(String computerId) {
		this.computerId = computerId;
	}
	public String getComputerName() {
		return computerName;
	}
	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}
	public String getCabinetId() {
		return cabinetId;
	}
	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}
	public String getCabinetName() {
		return cabinetName;
	}
	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}
	public String getCellId() {
		return cellId;
	}
	public void setCellId(String cellId) {
		this.cellId = cellId;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public Integer getCellPin() {
		return cellPin;
	}
	public void setCellPin(Integer cellPin) {
		this.cellPin = cellPin;
	}
	public String getComputerPort() {
		return computerPort;
	}
	public void setComputerPort(String computerPort) {
		this.computerPort = computerPort;
	}
	public Integer getComputerBaudRate() {
		return computerBaudRate;
	}
	public void setComputerBaudRate(Integer computerBaudRate) {
		this.computerBaudRate = computerBaudRate;
	}
	public Integer getCellStack() {
		return cellStack;
	}
	public void setCellStack(Integer cellStack) {
		this.cellStack = cellStack;
	}
	public Integer getCellSort() {
		return cellSort;
	}
	public void setCellSort(Integer cellSort) {
		this.cellSort = cellSort;
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
