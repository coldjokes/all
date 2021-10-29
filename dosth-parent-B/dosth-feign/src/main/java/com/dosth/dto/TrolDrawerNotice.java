package com.dosth.dto;

import java.io.Serializable;

/**
 * @description
 * @author Zhidong.Guo
 *
 */
@SuppressWarnings("serial")
public class TrolDrawerNotice implements Serializable {
	// 领取记录Id
	private String recordId;
	// 货道Id
	private String staId;
	// 数量
	private Integer amount;
	// 实取数量<可控抽屉柜>
	private Integer realNum;

	public TrolDrawerNotice() {
	}

	public TrolDrawerNotice(String recordId, String staId, Integer amount, Integer realNum) {
		this.recordId = recordId;
		this.staId = staId;
		this.amount = amount;
		this.realNum = realNum;
	}

	public String getRecordId() {
		return this.recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	public String getStaId() {
		return this.staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getRealNum() {
		return this.realNum;
	}

	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}
}