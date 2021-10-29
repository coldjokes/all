package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;

/**
 * 单位
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class Unit extends BasePojo {

	@Column(name = "UNIT_NAME", columnDefinition = "varchar(36) COMMENT '名称'")
	@PageTableTitle(value = "名称")
	private String unitName;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public UsingStatus getStatus() {
		if (this.status == null) {
			this.status = UsingStatus.ENABLE;
		}
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}
}