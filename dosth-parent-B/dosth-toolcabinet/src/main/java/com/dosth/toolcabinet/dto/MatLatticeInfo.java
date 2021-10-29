package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 物料格子信息
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MatLatticeInfo implements Serializable {
	/** 刀片柜单元格Id */
	private Long letticeId;
	/** ModbusSlaveId */
	private Integer slaveId;
	/** 物料类型 */
	private String typeCode;
	/** 借出数量 */
	private Integer borrNum;

	public MatLatticeInfo(Long letticeId, Integer slaveId, String typeCode, Integer borrNum) {
		this.letticeId = letticeId;
		this.slaveId = slaveId;
		this.typeCode = typeCode;
		this.borrNum = borrNum;
	}

	public Long getLetticeId() {
		return this.letticeId;
	}

	public Integer getSlaveId() {
		return this.slaveId;
	}

	public String getTypeCode() {
		return this.typeCode;
	}

	public Integer getBorrNum() {
		return this.borrNum;
	}
}