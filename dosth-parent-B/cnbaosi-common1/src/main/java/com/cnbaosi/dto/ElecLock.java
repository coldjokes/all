package com.cnbaosi.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 电磁锁对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ElecLock implements Serializable {
	private String cabinetId; // 暂存柜Id
	private Integer boardNo; // 栈号
	private Integer lockIndex; // 地址位(板子针脚)
	private Integer boxIndex; // 索引位(客户端排序位)
	
	private String soundPath; // 音频路径
	
	private Date startDate; // 启动时间

	public ElecLock() {
	}

	public ElecLock(String cabinetId, Integer boardNo, Integer lockIndex, Integer boxIndex) {
		this.cabinetId = cabinetId;
		this.boardNo = boardNo;
		this.lockIndex = lockIndex;
		this.boxIndex = boxIndex;
	}

	public String getCabinetId() {
		return this.cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public Integer getBoardNo() {
		return this.boardNo;
	}

	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}

	public Integer getLockIndex() {
		return this.lockIndex;
	}

	public void setLockIndex(Integer lockIndex) {
		this.lockIndex = lockIndex;
	}

	public Integer getBoxIndex() {
		return this.boxIndex;
	}

	public void setBoxIndex(Integer boxIndex) {
		this.boxIndex = boxIndex;
	}

	public String getSoundPath() {
		return this.soundPath;
	}

	public void setSoundPath(String soundPath) {
		this.soundPath = soundPath;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}