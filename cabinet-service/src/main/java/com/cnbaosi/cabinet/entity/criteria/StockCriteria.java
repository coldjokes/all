package com.cnbaosi.cabinet.entity.criteria;

public class StockCriteria {

	private String cabinetId; //设备id
	private String cabinetCellId; //库存库位id
	private String materialId; //物料id

	public String getCabinetId() {
		return cabinetId;
	}
	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getCabinetCellId() {
		return cabinetCellId;
	}
	public void setCabinetCellId(String cabinetCellId) {
		this.cabinetCellId = cabinetCellId;
	}
}
