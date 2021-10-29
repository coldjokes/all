package com.exam.entity.criteria.common;

/**
  * 分页查询条件
 *
 * @author Yifeng Wang  
 */
public class PageCriteria {
	
    private Integer curPage; //当前页
    private Integer pageSize; //每页长度
    
    public PageCriteria() {
        this.curPage = 1;
        this.pageSize = 1000;
    }
    
	public Integer getCurPage() {
		return curPage;
	}
	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
    
    
}
