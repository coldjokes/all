package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * @description 账户用户信息
 * @Author guozhidong
 */
@SuppressWarnings("serial")
public class AccountUserInfo implements Serializable {
	private String accountId; // 帐户Id
	private String loginName; // 帐户名称
	private String password; // 登录密码
	private String deptId; // 部门Id
	private String deptName; // 部门名称
	private String userId; // 用户Id
	private String userName; // 用户姓名

	public AccountUserInfo() {
	}

	public AccountUserInfo(String accountId, String loginName, String password, String userId, String userName) {
		this.accountId = accountId;
		this.loginName = loginName;
		this.password = password;
		this.userId = userId;
		this.userName = userName;
	}

	public AccountUserInfo(String accountId, String loginName, String password, String deptId, String deptName, String userId,
			String userName) {
		this.accountId = accountId;
		this.loginName = loginName;
		this.password = password;
		this.deptId = deptId;
		this.deptName = deptName;
		this.userId = userId;
		this.userName = userName;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "AccountUserInfo [accountId=" + accountId + ", loginName=" + loginName + ", password=" + password
				+ ", deptId=" + deptId + ", deptName=" + deptName + ", userId=" + userId + ", userName=" + userName
				+ "]";
	}
}