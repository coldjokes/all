package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 借出栅栏格局封装对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class BorrowGrid implements Serializable {
	private String equDetailStaId; // 设备信息Id
	private String matInfoId; // 借出物料Id
	private Integer borrowNum; // 借出数量
	private String borrowType; // 借出单位

	public BorrowGrid() {
	}

	public BorrowGrid(String equDetailStaId, Integer borrowNum) {
		this.equDetailStaId = equDetailStaId;
		this.borrowNum = borrowNum;
	}

	public BorrowGrid(String equDetailStaId, String matInfoId, Integer borrowNum, String borrowType) {
		super();
		this.equDetailStaId = equDetailStaId;
		this.matInfoId = matInfoId;
		this.borrowNum = borrowNum;
		this.borrowType = borrowType;
	}

	public String getEquDetailStaId() {
		return this.equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public Integer getBorrowNum() {
		if (this.borrowNum == null) {
			this.borrowNum = 1;
		}
		return this.borrowNum;
	}

	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}

	public String getBorrowType() {
		return borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}
}