package com.dosth.admin.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.admin.constant.state.LogType;
import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.Succeed;
import com.dosth.common.db.entity.BasePojo;

/**
 * 操作日志
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class OperationLog extends BasePojo {
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '日志类型'")
	@PageTableTitle(value = "日志类型")
	private LogType logType;
	
	@Column(columnDefinition = "varchar(50) COMMENT '日志名称'")
	@PageTableTitle(value = "日志名称")
	private String logName;
	
	@Column(columnDefinition = "varchar(36) COMMENT '账户ID'")
	@PageTableTitle(value = "账户ID")
	private String accountId;
	
	@Column(columnDefinition = "varchar(200) COMMENT '类名称'")
	@PageTableTitle(value = "类名称")
	private String className;
	
	@Column(columnDefinition = "varchar(36) COMMENT '方法名称'")
	@PageTableTitle(value = "方法名称")
	private String method;
	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@PageTableTitle(value = "创建时间")
	@Column(columnDefinition = "datetime COMMENT '创建时间'")
	private Date createTime;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '是否成功'")
	@PageTableTitle(value = "是否成功", isEnum = true)
	private Succeed succeed;
	/**
	 * 备注
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@PageTableTitle(value = "备注")
	@Column(columnDefinition = "longtext COMMENT '备注'")
	private String message;

	public LogType getLogType() {
		return this.logType;
	}

	public void setLogType(LogType logType) {
		this.logType = logType;
	}

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

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Date getCreateTime() {
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

	@Override
	public String toString() {
		return "OperationLog{" + "id=" + id + ", logType=" + logType + ", logName=" + logName + ", accountId="
				+ accountId + ", className=" + className + ", method=" + method + ", createTime=" + createTime
				+ ", succeed=" + succeed + ", message=" + message + "}";
	}
}