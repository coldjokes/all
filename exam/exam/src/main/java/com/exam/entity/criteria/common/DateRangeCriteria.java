package com.exam.entity.criteria.common;

/**
 * 时间范围查询条件
*
* @author Yifeng Wang  
*/
public class DateRangeCriteria {
	
    private Long startTime; //起始时间
    private Long endTime; //结束时间
    
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
    
    
}
