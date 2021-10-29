package com.dosth.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;

/**
 * 系统维护
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class SystemInfo extends BasePojo {

	@Column(columnDefinition = "varchar(50) COMMENT '系统名称'")
	@PageTableTitle(value = "系统名称")
	private String systemName;

	@Column(columnDefinition = "varchar(50) COMMENT '系统URL'")
	@PageTableTitle(value = "系统URL")
	private String url;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	public String getSystemName() {
		return this.systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public UsingStatus getStatus() {
		return status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

}