package com.cnbaosi.cabinet.entity.modal;

/**
 * 列
 * 
 * @author Yifeng Wang  
 */

public class CabinetCell extends BaseModel<CabinetCell> {

	private static final long serialVersionUID = 1L;
	
	private String cabinetId; //柜体id;
	private String rowId; // 列id
	
	private String name; //显示名称
	
	private Integer stack; //栈号
	private Integer pin; //针脚地址
	private Integer sort; //序号
	
	public CabinetCell() {
		super();
	}
	public CabinetCell(String name, Integer pin) {
		super();
		this.name = name;
		this.pin = pin;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPin() {
		return pin;
	}
	public void setPin(Integer pin) {
		this.pin = pin;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getCabinetId() {
		return cabinetId;
	}
	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}
	public Integer getStack() {
		return stack;
	}
	public void setStack(Integer stack) {
		this.stack = stack;
	}
}

