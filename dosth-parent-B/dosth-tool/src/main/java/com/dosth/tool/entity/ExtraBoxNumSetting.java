package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 暂存柜数量设定
 * 
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class ExtraBoxNumSetting extends BasePojo {

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(255) COMMENT '帐号ID'")
	private String accountId;

	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "姓名/工号", isForeign = true)
	private ViewUser user;

	@Column(columnDefinition = "varchar(100) COMMENT '暂存柜数量'")
	@PageTableTitle(value = "暂存柜数量")
	private String extraBoxNum;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true, isVisible = false)
	private UsingStatus status;

	public ExtraBoxNumSetting() {
		setStatus(UsingStatus.ENABLE);
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public ViewUser getUser() {
		return this.user;
	}

	public void setUser(ViewUser user) {
		this.user = user;
	}

	public String getExtraBoxNum() {
		return this.extraBoxNum;
	}

	public void setExtraBoxNum(String extraBoxNum) {
		this.extraBoxNum = extraBoxNum;
	}

	public UsingStatus getStatus() {
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}
}