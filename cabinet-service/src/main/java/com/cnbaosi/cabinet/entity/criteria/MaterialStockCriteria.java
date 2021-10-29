package com.cnbaosi.cabinet.entity.criteria;

/**
 * 库存相关搜索条件
 * 
 * @author Yifeng Wang
 */

public class MaterialStockCriteria {

	private String computerId; //主机id
	private String text; // 物料名称、编号、型号
	private String materialId; //物料id
	private String cabinetId; //柜体id
	private String status; // 状态：1、可用； 2、借出

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public String getComputerId() {
		return computerId;
	}

	public void setComputerId(String computerId) {
		this.computerId = computerId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
			

}
