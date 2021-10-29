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

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.vo.ViewUser;

/**
 * 
 * @description 补料清单
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class FeedingList extends BasePojo {

	@Column(name = "CABINET_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String cabinetId;
	@ManyToOne
	@JoinColumn(name = "CABINET_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "主柜名称", isForeign = true)
	private EquSetting equSetting;

	@Column(columnDefinition = "varchar(50) COMMENT '补料清单'")
	@PageTableTitle(value = "补料清单")
	private String feedingName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '补料类型'")
	@PageTableTitle(value = "补料类型", isEnum = true)
	private FeedType feedType;

	@Column(name = "CREATE_ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '补料创建人'")
	private String createAccountId;
	@ManyToOne
	@JoinColumn(name = "CREATE_ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "创建人员", isForeign = true)
	private ViewUser user;

	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "创建时间")
	@Column(columnDefinition = "datetime COMMENT '补料创建时间'")
	private Date createTime;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '是否完成'")
	@PageTableTitle(value = "是否完成", isEnum = true)
	private YesOrNo isFinish;

	@Column(name = "FEEDING_ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '补料人'")
	private String feedingAccountId;
	@ManyToOne
	@JoinColumn(name = "FEEDING_ACCOUNT_ID", insertable = false, updatable = false)
	private ViewUser feedingAccount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '补料时间'")
	private Date feedingTime;

	public FeedingList(String cabinetId, String feedingName, String createAccountId, YesOrNo isFinish, FeedType type) {
		this.cabinetId = cabinetId;
		this.feedingName = feedingName;
		this.createAccountId = createAccountId;
		this.isFinish = isFinish;
		this.feedType = type;
		setCreateTime(new Date());
	}

	public FeedingList() {
	}

	public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public EquSetting getEquSetting() {
		return equSetting;
	}

	public void setEquSetting(EquSetting equSetting) {
		this.equSetting = equSetting;
	}

	public String getFeedingName() {
		return feedingName;
	}

	public void setFeedingName(String feedingName) {
		this.feedingName = feedingName;
	}

	public FeedType getFeedType() {
		return this.feedType;
	}

	public void setFeedType(FeedType feedType) {
		this.feedType = feedType;
	}

	public String getCreateAccountId() {
		return createAccountId;
	}

	public void setCreateAccountId(String createAccountId) {
		this.createAccountId = createAccountId;
	}

	public ViewUser getUser() {
		return user;
	}

	public void setUser(ViewUser user) {
		this.user = user;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public YesOrNo getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(YesOrNo isFinish) {
		this.isFinish = isFinish;
	}

	public String getFeedingAccountId() {
		return feedingAccountId;
	}

	public void setFeedingAccountId(String feedingAccountId) {
		this.feedingAccountId = feedingAccountId;
	}

	public ViewUser getFeedingAccount() {
		return this.feedingAccount;
	}

	public void setFeedingAccount(ViewUser feedingAccount) {
		this.feedingAccount = feedingAccount;
	}

	public Date getFeedingTime() {
		return feedingTime;
	}

	public void setFeedingTime(Date feedingTime) {
		this.feedingTime = feedingTime;
	}

}