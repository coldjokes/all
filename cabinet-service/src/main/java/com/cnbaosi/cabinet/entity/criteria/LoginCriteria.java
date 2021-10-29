package com.cnbaosi.cabinet.entity.criteria;

/**
 * 登录相关条件
 * 
 * @author Yifeng Wang  
 */

public class LoginCriteria {
	
	private String username; //用户名
	private String password; //密码
	private String icCard; //ic卡号
	private String identifyCode; //登录机器识别码
	private boolean isApp; //是否app登录
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIcCard() {
		return icCard;
	}
	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}
	public String getIdentifyCode() {
		return identifyCode;
	}
	public void setIdentifyCode(String identifyCode) {
		this.identifyCode = identifyCode;
	}
	public Boolean getIsApp() {
		return isApp;
	}
	public void setIsApp(Boolean isApp) {
		this.isApp = isApp;
	}
}

