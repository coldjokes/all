package com.cnbaosi.dto;

import java.io.Serializable;

/**
 * @description 取料封装对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Slave implements Serializable {
	// 马达索引序号
	private String staId;
	// 领用记录Id
	private String recordId;
	// 物理列号
	private int colNo;
	// 马达索引
	private int motorIndex;
	// 取料数量
	private int amount;

	public Slave(String staId, String recordId, int colNo, int motorIndex, int amount) {
		this.staId = staId;
		this.recordId = recordId;
		this.colNo = colNo;
		this.motorIndex = motorIndex;
		this.amount = amount;
	}

	public String getStaId() {
		return this.staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public int getColNo() {
		return this.colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public int getMotorIndex() {
		return this.motorIndex;
	}

	public void setMotorIndex(int motorIndex) {
		this.motorIndex = motorIndex;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}