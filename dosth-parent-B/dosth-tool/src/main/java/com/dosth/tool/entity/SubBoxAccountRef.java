package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.tool.vo.ViewUser;

/**
 *
 * @description 副柜帐户关系
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class SubBoxAccountRef extends BasePojo {

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '帐户ID'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "领用人员", isForeign = true)
	private ViewUser user;

	@Column(name = "SUB_BOX_ID", columnDefinition = "varchar(36) COMMENT '暂存柜货位ID'")
	private String subBoxId;
	@ManyToOne
	@JoinColumn(name = "SUB_BOX_ID", insertable = false, updatable = false)
	private SubBox subBox;

	public SubBoxAccountRef() {
	}

	public SubBoxAccountRef(String accountId, String subBoxId) {
		this.accountId = accountId;
		this.subBoxId = subBoxId;
	}

	public SubBoxAccountRef(SubBox subBox, ViewUser user) {
		this.subBox = subBox;
		this.user = user;
	}

	public String getAccountId() {
		return this.accountId;
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

	public String getSubBoxId() {
		return this.subBoxId;
	}

	public void setSubBoxId(String subBoxId) {
		this.subBoxId = subBoxId;
	}

	public SubBox getSubBox() {
		return this.subBox;
	}

	public void setSubBox(SubBox subBox) {
		this.subBox = subBox;
	}
}