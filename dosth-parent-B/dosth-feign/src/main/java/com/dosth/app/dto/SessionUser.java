package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description Session用户
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "SessionUser", description = "Session用户")
public class SessionUser implements Serializable {
	@ApiModelProperty(name = "user", value = "用户信息")
	private AppUser user;

	public AppUser getUser() {
		return this.user;
	}

	public void setUser(AppUser user) {
		this.user = user;
	}
}