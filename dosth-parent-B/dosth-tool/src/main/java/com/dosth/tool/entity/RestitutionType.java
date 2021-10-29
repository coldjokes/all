package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.toolcabinet.enums.ReturnBackType;

/**
 * @description 归还类型定义
 * 
 * @author liweifneg
 *
 */
@Entity
@SuppressWarnings("serial")
public class RestitutionType extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '归还名称'")
	@PageTableTitle(value = "归还名称")
	private String restName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '归还类型'")
	@PageTableTitle(value = "归还类型")
	private ReturnBackType returnBackType;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Column(columnDefinition = "varchar(500) COMMENT '备注'")
	@PageTableTitle(value = "备注", isVisible = false)
	private String remark;

	public String getRestName() {
		return restName;
	}

	public void setRestName(String restName) {
		this.restName = restName;
	}

	public ReturnBackType getReturnBackType() {
		return this.returnBackType;
	}

	public void setReturnBackType(ReturnBackType returnBackType) {
		this.returnBackType = returnBackType;
	}

	public UsingStatus getStatus() {
		return status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}