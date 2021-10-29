package com.cnbaosi.workspace.spring;

import java.util.HashMap;
import java.util.Map;

/**
 * @description 取料格子信息
 * @author guozhidong
 *
 */
public class Lattice {
	
	// 物理列号
	private int colNo;
	// 马达索引
	private int motorIndex;
	// recordId,数量状态对应
	private Map<String, RecordStatus> statusMap;

	public Lattice() {
	}

	public Lattice(int colNo, int motorIndex) {
		this.colNo = colNo;
		this.motorIndex = motorIndex;
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

	public Map<String, RecordStatus> getStatusMap() {
		if (this.statusMap == null) {
			this.statusMap = new HashMap<>();
		}
		return this.statusMap;
	}

	public void setStatusMap(Map<String, RecordStatus> statusMap) {
		this.statusMap = statusMap;
	}
}