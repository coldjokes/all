package com.dosth.tool.entity.vo;

import java.util.Date;

public class EquDetailStaVo {

	// 格子id
	private String id;
	// 刀具柜名称
	private String cabinetName;
	// 行号
	private Integer rowNo;
	// 列号
	private Integer colNo;
	// 物料id
	private String matInfoId;
	// 物料名称
	private String matInfoName;
	// 编号
	private String barCode;
	// 规格
	private String spec;
	// 最大存储
	private Integer maxReserve;
	// 告警阀值
	private Integer warnVal;
	// 当前数量
	private Integer curNum;
	// 最后上架时间
	private Date lastFeedTime;
	// 状态
	private String status;
	// 货位
	private String position;
	// 待确认数量
	private Integer waitFeedingNum;
	// 图片
	private String icon;

	public EquDetailStaVo() {
	}

	public EquDetailStaVo(String id, String matInfoId, String matInfoName, String barCode, String spec,
			Integer maxReserve, Integer warnVal, Integer curNum, String position, Integer waitFeedingNum, String icon) {
		this.id = id;
		this.matInfoId = matInfoId;
		this.matInfoName = matInfoName;
		this.barCode = barCode;
		this.spec = spec;
		this.maxReserve = maxReserve;
		this.warnVal = warnVal;
		this.curNum = curNum;
		this.position = position;
		this.waitFeedingNum = waitFeedingNum;
		this.icon = icon;
	}

	public EquDetailStaVo(String id, String cabinetName, Integer rowNo, Integer colNo, String matInfoName,
			String barCode, String spec, Integer maxReserve, Integer warnVal, Integer curNum, Date lastFeedTime,
			String status) {
		this.id = id;
		this.cabinetName = cabinetName;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.matInfoName = matInfoName;
		this.barCode = barCode;
		this.spec = spec;
		this.maxReserve = maxReserve;
		this.warnVal = warnVal;
		this.curNum = curNum;
		this.lastFeedTime = lastFeedTime;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getColNo() {
		return colNo;
	}

	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public String getMatInfoName() {
		return matInfoName;
	}

	public void setMatInfoName(String matInfoName) {
		this.matInfoName = matInfoName;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getMaxReserve() {
		return maxReserve;
	}

	public void setMaxReserve(Integer maxReserve) {
		this.maxReserve = maxReserve;
	}

	public Integer getWarnVal() {
		return warnVal;
	}

	public void setWarnVal(Integer warnVal) {
		this.warnVal = warnVal;
	}

	public Integer getCurNum() {
		return curNum;
	}

	public void setCurNum(Integer curNum) {
		this.curNum = curNum;
	}

	public Date getLastFeedTime() {
		return lastFeedTime;
	}

	public void setLastFeedTime(Date lastFeedTime) {
		this.lastFeedTime = lastFeedTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Integer getWaitFeedingNum() {
		return waitFeedingNum;
	}

	public void setWaitFeedingNum(Integer waitFeedingNum) {
		this.waitFeedingNum = waitFeedingNum;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}