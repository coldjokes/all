package com.dosth.dto;

import java.io.Serializable;

/**
 * @description 领用通讯对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class BorrowNotice implements Serializable {
	// 领用记录Id
	private String recordId;
	// 货道弹簧Id
	private String staId;
	// 成功标识 true 成功 false 失败
	private Boolean flag;

	public BorrowNotice() {
	}

	public BorrowNotice(String recordId, String staId) {
		this.recordId = recordId;
		this.staId = staId;
	}

	public BorrowNotice(String recordId, String staId, Boolean flag) {
		this.recordId = recordId;
		this.staId = staId;
		this.flag = flag;
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

	public Boolean getFlag() {
		return this.flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "BorrowNotice [recordId=" + recordId + ", staId=" + staId + ", flag=" + flag + "]";
	}
}