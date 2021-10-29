package com.dosth.tool.common.dto;

import java.io.Serializable;

/**
 * @description 领取方式汇总
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class CategorySummary implements Serializable {
	// 领取方式名称
	private String categoryName;
	// 领取方式汇总
	private Integer borrowNumSummary;

	public CategorySummary() {
	}

	public CategorySummary(String categoryName, Integer borrowNumSummary) {
		this.categoryName = categoryName;
		this.borrowNumSummary = borrowNumSummary;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getBorrowNumSummary() {
		return this.borrowNumSummary;
	}

	public void setBorrowNumSummary(Integer borrowNumSummary) {
		this.borrowNumSummary = borrowNumSummary;
	}
}