package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.common.state.OnOrOff;

/**
 * @description 通知管理
 * @author Weifeng Li
 *
 */
@Entity
@SuppressWarnings("serial")
public class NoticeMgr extends BasePojo {

	@Column(columnDefinition = "varchar(50) COMMENT '刀具柜名称'")
	@PageTableTitle(value = "刀具柜名称")
	private String equSettingName;

	@Column(name = "EQU_SETTING_ID", columnDefinition = "varchar(36) COMMENT '刀具柜ID'")
	private String equSettingId;
	@ManyToOne
	@JoinColumn(name = "EQU_SETTING_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "刀具柜", isForeign = true)
	private EquSetting equSetting;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(36) COMMENT '类型'")
	@PageTableTitle(value = "类型", isEnum = true)
	private NoticeType noticeType;

	@Column(columnDefinition = "int(11) COMMENT '数量'")
	@PageTableTitle(value = "数量")
	private Integer num;

	@Column(columnDefinition = "int(11) COMMENT '计数'")
	@PageTableTitle(value = "计数")
	private Integer count;

	@Column(columnDefinition = "int(11) COMMENT '预警值'")
	@PageTableTitle(value = "预警值")
	private Integer warnValue;

	@PageTableTitle(value = "收件人")
	@Column(columnDefinition = "varchar(255) COMMENT '收件人'")
	private String userName;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(255) COMMENT '帐号ID'")
	private String accountId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(36) COMMENT '启用状态'")
	@PageTableTitle(value = "启用状态", isEnum = true)
	private OnOrOff status;

	public NoticeMgr() {
		super();
	}

	public NoticeMgr(String equSettingName, String equSettingId, NoticeType noticeType, Integer num, Integer count,
			Integer warnValue, String userName, String accountId) {
		super();
		this.equSettingName = equSettingName;
		this.equSettingId = equSettingId;
		this.noticeType = noticeType;
		this.num = num;
		this.count = count;
		this.warnValue = warnValue;
		this.userName = userName;
		this.accountId = accountId;
	}

	public String getEquSettingName() {
		return equSettingName;
	}

	public void setEquSettingName(String equSettingName) {
		this.equSettingName = equSettingName;
	}

	public String getEquSettingId() {
		return equSettingId;
	}

	public void setEquSettingId(String equSettingId) {
		this.equSettingId = equSettingId;
	}

	public EquSetting getEquSetting() {
		return equSetting;
	}

	public void setEquSetting(EquSetting equSetting) {
		this.equSetting = equSetting;
	}

	public NoticeType getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(NoticeType noticeType) {
		this.noticeType = noticeType;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getWarnValue() {
		return warnValue;
	}

	public void setWarnValue(Integer warnValue) {
		this.warnValue = warnValue;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public OnOrOff getStatus() {
		if (this.status == null) {
			this.status = OnOrOff.OFF;
		}
		return this.status;
	}

	public void setStatus(OnOrOff status) {
		this.status = status;
	}
}