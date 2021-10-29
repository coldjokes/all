package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description App用户封装信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppUser", description = "用户信息")
public class AppUser implements Serializable {

	// 主键
	@ApiModelProperty(name = "userId", value = "用户Id")
	private String userId;

	// 用户名
	@ApiModelProperty(name = "userName", value = "用户名")
	private String userName;

	// 部门
	@ApiModelProperty(name = "userPartment", value = "部门")
	private String userPartment;

	// 工号
	@ApiModelProperty(name = "userNo", value = "工号")
	private String userNo;
	
	// 头像
	@ApiModelProperty(name = "userImage", value = "头像")
	private String userImage;
	
	public AppUser() {
	}

	public AppUser(String userId, String userName, String userPartment, String userNo, String userImage) {
		this.userId = userId;
		this.userName = userName;
		this.userPartment = userPartment;
		this.userNo = userNo;
		this.userImage = userImage;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPartment() {
		return userPartment;
	}

	public void setUserPartment(String userPartment) {
		this.userPartment = userPartment;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

}