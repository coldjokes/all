package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 用户详情
 * @author liweifeng
 *
 */
@SuppressWarnings("serial")
public class UserInfo implements Serializable {
	/** 用户姓名 */
	private String userName;
	/** 帐号名称 */
	private String loginName;
	/** 限额开始时间 */
	private String startTime;
	/** 限额结束时间 */
	private String endTime;
	/** 限额总数 */
	private Integer limitSumNum;
	/** 未归还限额 */
	private Integer notReturnLimitNum;
	/** 帐号Id */
	private String accountId;
	/** 部门Id */
	private String deptId;
	/** 部门名称 */
	private String deptName;
	/** IC卡号 */
	private String icCard;
	/** roleID */
	private String roleId;

	public UserInfo() {
		super();
	}

	public UserInfo(String userName, String loginName, String startTime, String endTime, String accountId,
			String deptId, String icCard) {
		super();
		this.userName = userName;
		this.loginName = loginName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.accountId = accountId;
		this.deptId = deptId;
		this.icCard = icCard;
	}

	public UserInfo(String userName, String loginName, String startTime, String endTime, Integer limitSumNum,
			Integer notReturnLimitNum, String accountId, String deptId,String deptName, String icCard) {
		super();
		this.userName = userName;
		this.loginName = loginName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.limitSumNum = limitSumNum;
		this.notReturnLimitNum = notReturnLimitNum;
		this.accountId = accountId;
		this.deptId = deptId;
		this.deptName = deptName;
		this.icCard = icCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getLimitSumNum() {
		return limitSumNum;
	}

	public void setLimitSumNum(Integer limitSumNum) {
		this.limitSumNum = limitSumNum;
	}

	public Integer getNotReturnLimitNum() {
		return notReturnLimitNum;
	}

	public void setNotReturnLimitNum(Integer notReturnLimitNum) {
		this.notReturnLimitNum = notReturnLimitNum;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDeptId() {
		return deptId;
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

	public String getIcCard() {
		return icCard;
	}

	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
