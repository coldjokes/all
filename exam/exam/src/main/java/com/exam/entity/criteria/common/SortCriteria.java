package com.exam.entity.criteria.common;

/**
 * 排序条件
 * 
 * @author Yifeng Wang  
 */

public class SortCriteria {
	
	public static final String AES = "asc";
	public static final String DESC = "desc";
	
    private String sortField; //过滤字段
    private String sortOrder; //正序asc、反序desc
    
	public String getSortField() {
		return sortField;
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
    
}

