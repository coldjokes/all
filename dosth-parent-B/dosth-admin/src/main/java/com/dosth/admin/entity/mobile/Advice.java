package com.dosth.admin.entity.mobile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.admin.entity.Account;
import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 
 * @description 问题反馈
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class Advice extends BasePojo {

	@Column(name = "accountId", columnDefinition = "varchar(36) COMMENT '帐号ID'")
	private String accountId;
	@PageTableTitle(isForeign = true, value = "帐户Id")
	@ManyToOne
	@JoinColumn(name = "accountId", insertable = false, updatable = false)
	private Account account;

	@PageTableTitle(value = "内容")
	@Column(name = "adviceContent", columnDefinition = "longtext COMMENT '内容'")
	private String adviceContent;

	@PageTableTitle(value = "邮箱")
	@Column(name = "adviceMail", columnDefinition = "varchar(200) COMMENT '邮箱'")
	private String adviceMail;

	@Column(name = "adviceImages", columnDefinition = "varchar(200) COMMENT '头像'")
	private String adviceImages;

	@PageTableTitle(value = "其他联系方式")
	@Column(name = "adviceContact", columnDefinition = "varchar(200) COMMENT '其他联系方式'")
	private String adviceContact;

	public Advice() {
	}

	public Advice(String accountId, String adviceContent, String adviceMail, String adviceImages,
			String adviceContact) {
		this.accountId = accountId;
		this.adviceContent = adviceContent;
		this.adviceMail = adviceMail;
		this.adviceImages = adviceImages;
		this.adviceContact = adviceContact;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getAdviceContent() {
		return this.adviceContent;
	}

	public void setAdviceContent(String adviceContent) {
		this.adviceContent = adviceContent;
	}

	public String getAdviceMail() {
		return this.adviceMail;
	}

	public void setAdviceMail(String adviceMail) {
		this.adviceMail = adviceMail;
	}

	public String getAdviceImages() {
		return this.adviceImages;
	}

	public void setAdviceImages(String adviceImages) {
		this.adviceImages = adviceImages;
	}

	public String getAdviceContact() {
		return this.adviceContact;
	}

	public void setAdviceContact(String adviceContact) {
		this.adviceContact = adviceContact;
	}
}