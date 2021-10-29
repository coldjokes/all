package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 用户登录信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppLogin", description = "用户登录信息")
public class AppLogin implements Serializable {
	@ApiModelProperty(name = "userPhone", value = "用户手机号")
	private String userPhone;
	@ApiModelProperty(name = "userPassword", value = "用户密码")
	private String userPassword;
	@ApiModelProperty(name = "userName", value = "用户名")
	private String userName;

	public String getUserPhone() {
		return this.userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}