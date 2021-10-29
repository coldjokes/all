package com.exam.entity.modal;

/**
 *  后台日志
 * 
 * @author Yifeng Wang  
 */
public class Log extends BaseModel<Log> {
	private static final long serialVersionUID = 1L;

	private String username; //用户名
	private Integer logType; //用户具体操作类型
	private String business; //业务名称
	private String params; //相关参数
	private String remark; //备注信息
	private String ipAddress; //ip地址
	
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
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
}

