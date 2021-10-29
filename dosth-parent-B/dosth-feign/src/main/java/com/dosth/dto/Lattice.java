package com.dosth.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Engine槽位
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Lattice implements Serializable {
	/** 领用记录Id */
	private String recordId;
	/** 格子Id */
	private String staId;
	/** 仓位名称 */
	private String staName;
	/** 借出记录Id、数量对照表 */
	private Map<String, Integer> recordMap;
	/** Host */
	private String host;
	/** 端口 */
	private String port;
	/** 列号 */
	private int colNo;
	/** 当前格子数量 */
	private Integer curReserve;
	/** 剩余数量 */
	private Integer remainNum;
	/** 最大设置数 */
	private Integer maxReserve;
	/** 警告值 */
	private Integer warnVal;
	/** 补料数量 */
	private Integer feedNum;
	/** 层高 */
	private Integer levelHeight;
	/** 设备状态 */
	private String equSta;
	/** COMM */
	private String comm;
	/** 栈号 */
	private Integer boardNo;
	/** 锁索引号 */
	private Integer lockIndex;
	/** 盒子索引号 */
	private Integer boxIndex;
	/** 物料信息 */
	private MatInfo matInfo;

	/** 物料Id */
	private String matId;
	/** 编号 */
	private String barCode;
	/** 类型编码 */
	private String typeCode;
	/** 物料名称 */
	private String name;
	/** 品牌 */
	private String brand;
	/** 规格 */
	private String spec;
	/** 图片 */
	private String icon;
	/** 间隔 */
	private Integer interval;

	public Lattice() {
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public Lattice(String staId) {
		this.staId = staId;
	}

	public String getStaId() {
		return this.staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public String getStaName() {
		return this.staName;
	}

	public void setStaName(String staName) {
		this.staName = staName;
	}

	public Map<String, Integer> getRecordMap() {
		if (this.recordMap == null) {
			this.recordMap = new HashMap<>();
		}
		return this.recordMap;
	}

	public void setRecordMap(Map<String, Integer> recordMap) {
		this.recordMap = recordMap;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return this.port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getColNo() {
		return this.colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public Integer getCurReserve() {
		if (this.curReserve == null) {
			this.curReserve = 0;
		}
		return this.curReserve;
	}

	public void setCurReserve(Integer curReserve) {
		this.curReserve = curReserve;
	}

	public Integer getRemainNum() {
		return remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}

	public Integer getMaxReserve() {
		return this.maxReserve;
	}

	public void setMaxReserve(Integer maxReserve) {
		this.maxReserve = maxReserve;
	}

	public Integer getWarnVal() {
		return this.warnVal;
	}

	public void setWarnVal(Integer warnVal) {
		this.warnVal = warnVal;
	}

	public Integer getFeedNum() {
		return this.feedNum;
	}

	public void setFeedNum(Integer feedNum) {
		this.feedNum = feedNum;
	}

	public Integer getLevelHeight() {
		return this.levelHeight;
	}

	public void setLevelHeight(Integer levelHeight) {
		this.levelHeight = levelHeight;
	}

	public String getEquSta() {
		return this.equSta;
	}

	public void setEquSta(String equSta) {
		this.equSta = equSta;
	}

	public String getComm() {
		return this.comm;
	}

	public void setComm(String comm) {
		this.comm = comm;
	}

	public Integer getBoardNo() {
		return this.boardNo;
	}

	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}

	public Integer getLockIndex() {
		return this.lockIndex;
	}

	public void setLockIndex(Integer lockIndex) {
		this.lockIndex = lockIndex;
	}

	public Integer getBoxIndex() {
		return this.boxIndex;
	}

	public void setBoxIndex(Integer boxIndex) {
		this.boxIndex = boxIndex;
	}

	public MatInfo getMatInfo() {
		return this.matInfo;
	}

	public void setMatInfo(MatInfo matInfo) {
		this.matInfo = matInfo;
	}

	public String getMatId() {
		return matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getBarCode() {
		return barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getInterval() {
		return this.interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((staId == null) ? 0 : staId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lattice other = (Lattice) obj;
		if (staId == null) {
			if (other.staId != null)
				return false;
		} else if (!staId.equals(other.staId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lattice [staId=" + staId + ", staName=" + staName + ", recordMap=" + recordMap + ", host=" + host
				+ ", port=" + port + ", colNo=" + colNo + ", curReserve=" + curReserve + ", maxReserve=" + maxReserve
				+ ", warnVal=" + warnVal + ", feedNum=" + feedNum + ", levelHeight=" + levelHeight + ", equSta="
				+ equSta + ", comm=" + comm + ", boardNo=" + boardNo + ", lockIndex=" + lockIndex + ", boxIndex="
				+ boxIndex + "]";
	}
}