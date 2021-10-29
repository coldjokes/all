package com.dosth.tool.vo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

/**
 * 人员视图
 * 
 * @author guozhidong
 *
 */
@Entity
@Immutable
@Subselect("SELECT * FROM VIEW_USER")
@SuppressWarnings("serial")
public class ViewUser implements Serializable {
	@Id
	@GeneratedValue
	@Column(name = "ACCOUNT_ID")
	private String accountId;

	@Column(name = "DEPT_ID")
	private String deptId;

	@Column(name = "USER_NAME")
	private String userName;

	@Column(name = "LOGIN_NAME")
	private String loginName;

	@Column(name = "IC_CARD")
	private String icCard;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "LIMIT_SUM_NUM")
	private Integer limitSumNum;

	@Column(name = "NOT_RETURN_LIMIT_NUM")
	private Integer notReturnLimitNum;

	@Column(name = "START_TIME")
	private String startTime;

	@Column(name = "END_TIME")
	private String endTime;

	public ViewUser() {
		super();
	}

	public ViewUser(String accountId, Integer limitSumNum, Integer notReturnLimitNum, String startTime,
			String endTime) {
		super();
		this.accountId = accountId;
		this.limitSumNum = limitSumNum;
		this.notReturnLimitNum = notReturnLimitNum;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	public String getIcCard() {
		return icCard;
	}

	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

}