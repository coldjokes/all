package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 领用流水信息
 * @author chenlei
 *
 */
@SuppressWarnings("serial")
public class BillInfo implements Serializable {
	/** 借出数量 */
	private Integer num;
	/** 借出人员 */
	private String accountId;
	/** 领取类型明细 */
	private String receiveInfo;
	/** 领取类型 */
	private String receiveType;
	/** 物料信息 */
	private MatDetail matDetail;

	public BillInfo() {
	}


	public BillInfo(Integer num, String accountId, String receiveInfo, String receiveType, MatDetail matDetail) {
		this.num = num;
		this.accountId = accountId;
		this.receiveInfo = receiveInfo;
		this.receiveType = receiveType;
		this.matDetail = matDetail;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	
	public String getReceiveInfo() {
		return receiveInfo;
	}

	public void setReceiveInfo(String receiveInfo) {
		this.receiveInfo = receiveInfo;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public MatDetail getMatDetail() {
		return matDetail;
	}

	public void setMatDetail(MatDetail matDetail) {
		this.matDetail = matDetail;
	}
}