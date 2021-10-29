package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 定时任务详情
 * 
 * @author Weifeng.Li
 *
 */
@Entity
@SuppressWarnings("serial")
public class TimeTaskDetail extends BasePojo {

	@Column(name = "ACCOUNT_ID", columnDefinition = "varchar(255) COMMENT '帐号ID'")
	private String accountId;

	@PageTableTitle(value = "收件人")
	@Column(columnDefinition = "varchar(255) COMMENT '收件人'")
	private String userName;

	@PageTableTitle(value = "执行时间")
	@Column(columnDefinition = "varchar(36) COMMENT '执行时间'")
	private String executionTime;

	@PageTableTitle(value = "执行表达式")
	@Column(columnDefinition = "varchar(36) COMMENT '执行表达式'")
	private String cronExpression;

	@Column(columnDefinition = "varchar(36) COMMENT '任务ID'")
	@PageTableTitle(value = "任务ID")
	private String jobId;

	@Column(columnDefinition = "varchar(36) COMMENT '任务分组'")
	@PageTableTitle(value = "任务分组")
	private String jobGroup;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(String executionTime) {
		this.executionTime = executionTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
}
