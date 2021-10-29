package com.cnbaosi.cabinet.entity.modal.vo;

public class MaterialStockVo {

	private String cabinetId; // 柜子id
	private String cellId; // 格口id
	private String materialId; // 物料id

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

}
