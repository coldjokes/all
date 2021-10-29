package com.dosth.comm;

import java.io.Serializable;

/**
 * @description 锁控板通讯封装对象
 * @Author guozhidong
 */
@SuppressWarnings("serial")
public class LockPara implements Serializable {

	private Integer lockIndex; // 锁的索引位
	private Integer boardNo; // 栈号
	private Integer boxIndex; // 储存索引

	public LockPara() {
	}

	public LockPara(Integer boxIndex, Integer boardNo, Integer lockIndex) {
		this.lockIndex = lockIndex;
		this.boardNo = boardNo;
		this.boxIndex = boxIndex;
	}

	public Integer getLockIndex() {
		return this.lockIndex;
	}

	public void setLockIndex(Integer lockIndex) {
		this.lockIndex = lockIndex;
	}

	public Integer getBoardNo() {
		return this.boardNo;
	}

	public void setBoardNo(Integer boardNo) {
		this.boardNo = boardNo;
	}

	public Integer getBoxIndex() {
		return this.boxIndex;
	}

	public void setBoxIndex(Integer boxIndex) {
		this.boxIndex = boxIndex;
	}
}