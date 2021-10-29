package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description App用户登录信息封装
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppToken", description = "登录信息")
public class AppToken implements Serializable {
	
	@ApiModelProperty(name = "user", value = "用户信息")
	private AppUser user;
	
	@ApiModelProperty(name = "token", value = "令牌")
	private String token;

	public AppUser getUser() {
		return this.user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}