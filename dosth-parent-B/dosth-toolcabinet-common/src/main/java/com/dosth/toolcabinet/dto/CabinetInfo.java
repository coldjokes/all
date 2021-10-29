package com.dosth.toolcabinet.dto;

import java.io.Serializable;

import com.dosth.constant.EnumDoor;

/**
 * 柜子信息
 * 
 * @author liweifeng
 *
 */
@SuppressWarnings("serial")
public class CabinetInfo implements Serializable {

	/** 柜子Id */
	private String id;
	/** 柜子名称 */
	private String cabinetName;
	/** 柜子版本 */
	private String cabinetType;
	/** 柜子层数 */
	private Integer rowNum;
	/** 柜子列数 */
	private Integer colNum;
	/** 柜子状态 */
	private String status;
	/** 柜子门 */
	private EnumDoor door;

	public CabinetInfo(String id, String cabinetName, String cabinetType, Integer rowNum, Integer colNum,
			String status) {
		this.id = id;
		this.cabinetName = cabinetName;
		this.cabinetType = cabinetType;
		this.rowNum = rowNum;
		this.colNum = colNum;
		this.status = status;
	}

	public CabinetInfo() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCabinetName() {
		return this.cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public String getCabinetType() {
		return cabinetType;
	}

	public void setCabinetType(String cabinetType) {
		this.cabinetType = cabinetType;
	}

	public Integer getRowNum() {
		return this.rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	public Integer getColNum() {
		return this.colNum;
	}

	public void setColNum(Integer colNum) {
		this.colNum = colNum;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public EnumDoor getDoor() {
		return this.door;
	}

	public void setDoor(EnumDoor door) {
		this.door = door;
	}
}