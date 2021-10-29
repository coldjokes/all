package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;

/**
 * 库存实体类
 * 
 * @author Yifeng Wang
 */
public class Stock extends BaseModel<Stock> {
	
	private static final long serialVersionUID = 1L;
	
	private String computerId; //主机id
	private String cabinetId;	//设备id
	private String cabinetCellId; //设备方格id
	private String materialId; //物料id
	private Integer amount;  //库存数量
	private Date updateTime; //更新时间
	
	public String getCabinetId() {
		return cabinetId;
	}
	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}
	public String getCabinetCellId() {
		return cabinetCellId;
	}
	public void setCabinetCellId(String cabinetCellId) {
		this.cabinetCellId = cabinetCellId;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
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
}
