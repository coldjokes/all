package com.dosth.toolcabinet.dto;

import java.io.Serializable;

import com.dosth.toolcabinet.dto.enums.LockStatus;

/**
 * 
 * @description 锁孔板状态
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class LockState implements Serializable {
	private Integer index;
	private LockStatus lockStatus;

	public LockState() {
	}

	public LockState(Integer index, LockStatus lockStatus) {
		this.index = index;
		this.lockStatus = lockStatus;
	}

	public Integer getIndex() {
		return this.index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public LockStatus getLockStatus() {
		return this.lockStatus;
	}

	public void setLockStatus(LockStatus lockStatus) {
		this.lockStatus = lockStatus;
	}
}