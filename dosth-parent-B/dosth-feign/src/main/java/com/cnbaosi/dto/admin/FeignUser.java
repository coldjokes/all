package com.cnbaosi.dto.admin;

import java.io.Serializable;

/**
 * @description 用户信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignUser implements Serializable {
	private String accountId; // 账户Id
	private String userName; // 用户名
	private String loginName; // 登录名
	private String icCardNo; // IC卡号
	private String deptNo; // 部门编号
	private String deptName; // 部门名称

	public FeignUser() {
	}

	public FeignUser(String accountId, String userName, String loginName) {
		this.accountId = accountId;
		this.userName = userName;
		this.loginName = loginName;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getIcCardNo() {
		return this.icCardNo;
	}

	public void setIcCardNo(String icCardNo) {
		this.icCardNo = icCardNo;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}