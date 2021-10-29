package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 分页条件对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "PageCondition", description = "分页条件")
public class PageCondition implements Serializable {
	@ApiModelProperty(name = "pageNo", value = "当前页数")
	private Integer pageNo; // 当前页数
	@ApiModelProperty(name = "pageSize", value = "每页大小")
	private Integer pageSize; // 每页大小

	public PageCondition() {
	}

	public PageCondition(Integer pageNo, Integer pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public Integer getPageNo() {
		return this.pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}