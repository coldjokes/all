package com.dosth.dto;

public class ExtraBoxNum {

	/**
	 * 帐号id
	 */
	private String accountId;

	/**
	 * 暂存柜数量
	 */
	private String extraBoxNum;

	public ExtraBoxNum() {
		super();
	}

	public ExtraBoxNum(String accountId, String extraBoxNum) {
		super();
		this.accountId = accountId;
		this.extraBoxNum = extraBoxNum;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getExtraBoxNum() {
		return extraBoxNum;
	}

	public void setExtraBoxNum(String extraBoxNum) {
		this.extraBoxNum = extraBoxNum;
	}

}
