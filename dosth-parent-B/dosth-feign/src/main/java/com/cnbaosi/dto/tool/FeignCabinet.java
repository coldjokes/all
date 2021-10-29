package com.cnbaosi.dto.tool;

import java.io.Serializable;
import java.util.List;

/**
 * @description 库位信息
 * @author chenlei
 *
 */
@SuppressWarnings("serial")
public class FeignCabinet implements Serializable {
	private String equId; //设备id
	private String equName; //设备名称
	private Integer rowNum; //总行数
	private Integer colNum; //总列数
	private List<FeignStaDetail> staList; //单元格信息

	public FeignCabinet() {
	}

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

	public List<FeignStaDetail> getStaList() {
		return staList;
	}

	public void setStaList(List<FeignStaDetail> staList) {
		this.staList = staList;
	}

}