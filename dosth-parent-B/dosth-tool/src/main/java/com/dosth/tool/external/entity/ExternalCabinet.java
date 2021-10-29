package com.dosth.tool.external.entity;

import java.util.List;

/**
 *  
 * 
 * @author Yifeng Wang  
 */
public class ExternalCabinet {

	private String equId; //设备id
	private String equName; //设备名称
	private Integer rowNum; //总行数
	private Integer colNum; //总列数
	private List<CabinetCell> cells; //单元格信息

	public String getEquId() {
		return equId;
	}

	public void setEquId(String equId) {
		this.equId = equId;
	}

	public String getEquName() {
		return equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	public List<CabinetCell> getCells() {
		return cells;
	}

	public void setCells(List<CabinetCell> cells) {
		this.cells = cells;
	}

	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}
	
}

