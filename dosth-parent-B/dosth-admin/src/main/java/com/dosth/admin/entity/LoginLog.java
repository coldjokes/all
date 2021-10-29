package com.dosth.admin.entity;

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
import com.dosth.common.constant.Succeed;
import com.dosth.common.db.entity.BasePojo;

/**
 * 登录记录
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class LoginLog extends BasePojo {

	@Column(columnDefinition = "varchar(30) COMMENT '日志名称'")
	@PageTableTitle(value = "日志名称")
	private String logName;

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(36) COMMENT '操作帐号'")
	private String accountId;
	@ManyToOne
	@JoinColumn(name = "ACCOUNT_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "操作账户", isForeign = true)
	private Account account;

	@Column(columnDefinition = "datetime COMMENT '操作时间'")
	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "操作时间")
	private Date createTime;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '是否成功'")
	@PageTableTitle(value = "是否成功", isEnum = true)
	private Succeed succeed;

	@Column(columnDefinition = "varchar(255) COMMENT '具体消息'")
	@PageTableTitle(value = "具体消息")
	private String message;

	@Column(columnDefinition = "varchar(20) COMMENT '登录IP'")
	@PageTableTitle(value = "登录IP")
	private String ip;

	public String getLogName() {
		return this.logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getCreateTime() {
		if (this.createTime == null) {
			this.createTime = new Date();
		}
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Succeed getSucceed() {
		if (this.succeed == null) {
			this.succeed = Succeed.SUCCESS;
		}
		return this.succeed;
	}

	public void setSucceed(Succeed succeed) {
		this.succeed = succeed;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}