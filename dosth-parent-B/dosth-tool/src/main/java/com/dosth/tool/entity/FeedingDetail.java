package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 
 * @description 补料明细
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class FeedingDetail extends BasePojo {

	@Transient
	@PageTableTitle("刀具柜名称")
	private String cabinetName; // 柜子名称
	@Transient
	@PageTableTitle("补料清单名称")
	private String feedingName; // 补料清单名称
	@Transient
	@PageTableTitle("补料类型")
	private String feedType; // 补料类型
	@Transient
	@PageTableTitle("补料货位")
	private String position; // 补料货位
	@Transient
	@PageTableTitle("物料名称")
	private String matName; // 物料名称
	@Transient
	@PageTableTitle("物料编号")
	private String barCode; // 物料编码
	@Transient
	@PageTableTitle("物料型号")
	private String spec; // 物料规格

	@Column(name = "FEEDING_LIST_ID", columnDefinition = "varchar(36) COMMENT '补料清单ID'")
	private String feedingListId;
	@ManyToOne
	@JoinColumn(name = "FEEDING_LIST_ID", insertable = false, updatable = false)
	private FeedingList feedingList;

	@Column(name = "EQU_DETAIL_STA_ID", columnDefinition = "varchar(36) COMMENT '刀具柜货位ID'")
	private String equDetailStaId;
	@ManyToOne
	@JoinColumn(name = "EQU_DETAIL_STA_ID", insertable = false, updatable = false)
	private EquDetailSta equDetailSta;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	private MatEquInfo matInfo;

	@PageTableTitle("补充数量")
	@Column(columnDefinition = "int(11) COMMENT '补充数量'")
	private Integer feedingNum;

	@Transient
	@PageTableTitle("补料人员")
	private String feedingAccount; // 补料人员
	@Transient
	@PageTableTitle("补料时间")
	private String feedingTime; // 补料时间
	@Transient
	@PageTableTitle("是否完成")
	private String isFinish; // 是否完成

	@Transient
	private String matInfoName; // 物料名称

	@Transient
	private String equDetailStaText; // 柜子详情文本

	public FeedingDetail(String feedingListId, String equDetailStaId, String matInfoId, Integer feedingNum) {
		this.feedingListId = feedingListId;
		this.equDetailStaId = equDetailStaId;
		this.matInfoId = matInfoId;
		this.feedingNum = feedingNum;
	}

	public FeedingDetail() {
	}

	public FeedingList getFeedingList() {
		return this.feedingList;
	}

	public void setFeedingList(FeedingList feedingList) {
		this.feedingList = feedingList;
	}

	public String getFeedingListId() {
		return this.feedingListId;
	}

	public void setFeedingListId(String feedingListId) {
		this.feedingListId = feedingListId;
	}

	public EquDetailSta getEquDetailSta() {
		return this.equDetailSta;
	}

	public void setEquDetailSta(EquDetailSta equDetailSta) {
		this.equDetailSta = equDetailSta;
	}

	public String getEquDetailStaId() {
		return this.equDetailStaId;
	}

	public void setEquDetailStaId(String equDetailStaId) {
		this.equDetailStaId = equDetailStaId;
	}

	public MatEquInfo getMatInfo() {
		return this.matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public Integer getFeedingNum() {
		return this.feedingNum;
	}

	public void setFeedingNum(Integer feedingNum) {
		this.feedingNum = feedingNum;
	}

	public String getCabinetName() {
		return this.cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public String getFeedingName() {
		return this.feedingName;
	}

	public void setFeedingName(String feedingName) {
		this.feedingName = feedingName;
	}

	public String getFeedType() {
		return this.feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getMatName() {
		return this.matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getFeedingAccount() {
		return this.feedingAccount;
	}

	public void setFeedingAccount(String feedingAccount) {
		this.feedingAccount = feedingAccount;
	}

	public String getFeedingTime() {
		return this.feedingTime;
	}

	public void setFeedingTime(String feedingTime) {
		this.feedingTime = feedingTime;
	}

	public String getIsFinish() {
		return this.isFinish;
	}

	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
}