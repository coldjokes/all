package com.dosth.dto;

import java.io.Serializable;

/**
 * @description 库存明细
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class StockDetail implements Serializable {
	private Integer rowNo; // 行号
	private Integer colNo; // 列号
	private Integer curNum; // 当前数量

	protected StockDetail() {
	}

	public StockDetail(Integer rowNo, Integer colNo, Integer curNum) {
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.curNum = curNum;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getColNo() {
		return this.colNo;
	}

	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}

	public Integer getCurNum() {
		return this.curNum;
	}

	public void setCurNum(Integer curNum) {
		this.curNum = curNum;
	}
}