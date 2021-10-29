package com.exam.entity.criteria;

/**
  * 日志搜索条件
 * 
 * @author Yifeng Wang  
 */

public class LogCriteria{

	private String username; //用户名
	private Integer logType; //日志类型
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
}

