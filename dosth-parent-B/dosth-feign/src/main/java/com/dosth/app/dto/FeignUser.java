package com.dosth.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 用户
 * @author Weifeng Li
 *
 */
@ApiModel(value = "FeignUser", description = "用户")
public class FeignUser {

	@ApiModelProperty(name = "accountId", value = "账户id")
	private String accountId;

	@ApiModelProperty(name = "limitSumNum", value = "限额总数")
	private Integer limitSumNum;

	@ApiModelProperty(name = "notReturnLimitNum", value = "未归还限额")
	private Integer notReturnLimitNum;

	@ApiModelProperty(name = "startTime", value = "开始时间")
	private String startTime;

	@ApiModelProperty(name = "endTime", value = "结束时间")
	private String endTime;

	public FeignUser() {
		super();
	}

	public FeignUser(String accountId, Integer limitSumNum, Integer notReturnLimitNum, String startTime,
			String endTime) {
		super();
		this.accountId = accountId;
		this.limitSumNum = limitSumNum;
		this.notReturnLimitNum = notReturnLimitNum;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
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
