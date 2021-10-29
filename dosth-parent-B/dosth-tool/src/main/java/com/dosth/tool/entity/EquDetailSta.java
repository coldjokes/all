package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.EquSta;

/**
 * @description 设备详情状态
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class EquDetailSta extends BasePojo {

	@Column(name = "EQU_DETAIL_ID", columnDefinition = "varchar(36) COMMENT '托盘ID'")
	private String equDetailId;
	@ManyToOne
	@JoinColumn(name = "EQU_DETAIL_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "刀具柜名称", isForeign = true)
	private EquDetail equDetail;

	@Column(columnDefinition = "varchar(50) COMMENT '货位名称'")
	private String staName;

	@Transient
	@PageTableTitle(value = "行号")
	private Integer rowNo;

	@PageTableTitle(value = "列号")
	@Column(columnDefinition = "int(11) COMMENT '列号'")
	private Integer colNo;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "物料名称", isForeign = true)
	private MatEquInfo matInfo;

	@Transient
	@PageTableTitle(value = "物料名称")
	private String matInfoName;

	@Transient
	@PageTableTitle(value = "物料编号")
	private String barCode;

	@Transient
	@PageTableTitle(value = "物料型号")
	private String spec;

	@Transient
	@PageTableTitle(value = "供应商")
	private String manufacturerName;

	@PageTableTitle(value = "当前数量")
	@Column(columnDefinition = "int(11) COMMENT '当前数量'")
	private Integer curNum;

	@Transient
	@PageTableTitle(value = "包装单位")
	private String packUnit;

	@Transient
	@PageTableTitle(value = "单价")
	private Float price;

	@Transient
	@PageTableTitle(value = "金额")
	private Float sumPrice;

	@PageTableTitle(value = "最大存储")
	@Column(columnDefinition = "int(11) COMMENT '最大存储'")
	private Integer maxReserve;

	@PageTableTitle(value = "警告阀值")
	@Column(columnDefinition = "int(11) COMMENT '警告阀值'")
	private Integer warnVal;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "最后上架时间")
	@Column(columnDefinition = "datetime COMMENT '最后上架时间称'")
	private Date lastFeedTime;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '设备状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private EquSta equSta;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '启用状态'")
	private UsingStatus status;

	@Column(columnDefinition = "varchar(10) COMMENT '串口号'")
	private String comm;

	@Column(columnDefinition = "int(11) COMMENT '栈号'")
	private Integer boardNo;

	@Column(columnDefinition = "int(11) COMMENT '电磁锁索引号'")
	private Integer lockIndex;

	@Column(columnDefinition = "int(11) COMMENT '索引号'")
	private Integer boxIndex;

	@Column(name = "INTERVAL_VAL", columnDefinition = "int(11) COMMENT '间隔'")
	private Integer interval;

	public EquDetailSta() {
		setEquSta(EquSta.NONE);
		setStatus(UsingStatus.ENABLE);
	}

	public EquDetailSta(String equDetailId, Integer rowNo, Integer colNo, Integer maxReserve, Integer warnVal,
			Date lastFeedTime, Integer boardNo, Integer lockIndex, Integer boxIndex) {
		super();
		this.equDetailId = equDetailId;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.maxReserve = maxReserve;
		this.warnVal = warnVal;
		this.lastFeedTime = lastFeedTime;
		this.boardNo = boardNo;
		this.lockIndex = lockIndex;
		this.boxIndex = boxIndex;
		setEquSta(EquSta.NONE);
		setStatus(UsingStatus.ENABLE);
	}

	public String getEquDetailId() {
		return this.equDetailId;
	}

	public void setEquDetailId(String equDetailId) {
		this.equDetailId = equDetailId;
	}

	public EquDetail getEquDetail() {
		return this.equDetail;
	}

	public void setEquDetail(EquDetail equDetail) {
		this.equDetail = equDetail;
	}

	public String getStaName() {
		if (this.staName == null || "".equals(this.staName)) {
			return new StringBuilder("F-").append(this.equDetail.getRowNo()).append("-").append(this.colNo).toString();
		}
		return this.staName;
	}

	public void setStaName(String staName) {
		this.staName = staName;
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

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatInfo() {
		return this.matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
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

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Integer getCurNum() {
		if (this.curNum == null) {
			this.curNum = 0;
		}
		return this.curNum;
	}

	public void setCurNum(Integer curNum) {
		this.curNum = curNum;
	}

	public String getPackUnit() {
		return packUnit;
	}

	public void setPackUnit(String packUnit) {
		this.packUnit = packUnit;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Float sumPrice) {
		this.sumPrice = sumPrice;
	}

	public Integer getMaxReserve() {
		if (this.maxReserve == null) {
			this.maxReserve = 0;
		}
		return this.maxReserve;
	}

	public void setMaxReserve(Integer maxReserve) {
		this.maxReserve = maxReserve;
	}

	public Integer getWarnVal() {
		if (this.warnVal == null) {
			this.warnVal = 3;
		}
		return this.warnVal;
	}

	public void setWarnVal(Integer warnVal) {
		this.warnVal = warnVal;
	}

	public Date getLastFeedTime() {
		return lastFeedTime;
	}

	public void setLastFeedTime(Date lastFeedTime) {
		this.lastFeedTime = lastFeedTime;
	}

	public EquSta getEquSta() {
		if (this.equSta == null) {
			this.equSta = EquSta.NONE;
		}
		return this.equSta;
	}

	public void setEquSta(EquSta equSta) {
		this.equSta = equSta;
	}

	public UsingStatus getStatus() {
		if (this.status == null) {
			this.status = UsingStatus.ENABLE;
		}
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	/**
	 * @description 获取主柜对象
	 */
	public EquSetting getMainCabinet() {
		return this.getEquDetail().getEquSetting();
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

	public Integer getInterval() {
		if (this.interval == null) {
			this.interval = 1;
		}
		return this.interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}
}