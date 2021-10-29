package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 入库单补货记录
 * 
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class WarehouseFeed extends BasePojo {

	@PageTableTitle("入库单编号")
	@Column(columnDefinition = "varchar(255) COMMENT '入库单编号'")
	private String feedNo;

	@PageTableTitle("库房号")
	@Column(columnDefinition = "varchar(255) COMMENT '库房号'")
	private String stockNo;

	@Column(name = "FEEDING_LIST_ID", columnDefinition = "varchar(36) COMMENT '补料清单'")
	private String feedingListId;
	@ManyToOne
	@JoinColumn(name = "FEEDING_LIST_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "补料清单", isForeign = true)
	private FeedingList feedingList;

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@OneToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "物料名称", isForeign = true)
	private MatEquInfo matInfo;

	@PageTableTitle("补料数量")
	@Column(columnDefinition = "int(11) COMMENT '补料数量'")
	private Integer feedNum;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "上架时间")
	@Column(columnDefinition = "datetime COMMENT '上架时间'")
	private Date feedTime;

	@PageTableTitle("返回信息")
	@Column(columnDefinition = "varchar(255) COMMENT '返回信息'")
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '是否确认'")
	@PageTableTitle(value = "是否确认")
	private YesOrNo status;

	public WarehouseFeed() {
	}

	public WarehouseFeed(String feedNo, String stockNo, String feedingListId, String matInfoId, Integer feedNum,
			YesOrNo status) {
		this.feedNo = feedNo;
		this.stockNo = stockNo;
		this.feedingListId = feedingListId;
		this.matInfoId = matInfoId;
		this.feedNum = feedNum;
		this.status = status;
		this.feedTime = new Date();
	}

	public String getFeedNo() {
		return feedNo;
	}

	public void setFeedNo(String feedNo) {
		this.feedNo = feedNo;
	}

	public String getStockNo() {
		return stockNo;
	}

	public void setStockNo(String stockNo) {
		this.stockNo = stockNo;
	}

	public String getFeedingListId() {
		return feedingListId;
	}

	public void setFeedingListId(String feedingListId) {
		this.feedingListId = feedingListId;
	}

	public FeedingList getFeedingList() {
		return feedingList;
	}

	public void setFeedingList(FeedingList feedingList) {
		this.feedingList = feedingList;
	}

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatInfo() {
		return matInfo;
	}

	public void setMatInfo(MatEquInfo matInfo) {
		this.matInfo = matInfo;
	}

	public Integer getFeedNum() {
		return feedNum;
	}

	public void setFeedNum(Integer feedNum) {
		this.feedNum = feedNum;
	}

	public Date getFeedTime() {
		return feedTime;
	}

	public void setFeedTime(Date feedTime) {
		this.feedTime = feedTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public YesOrNo getStatus() {
		return status;
	}

	public void setStatus(YesOrNo status) {
		this.status = status;
	}

}