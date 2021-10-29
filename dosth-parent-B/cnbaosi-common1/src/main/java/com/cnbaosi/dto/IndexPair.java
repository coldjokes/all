package com.cnbaosi.dto;

/**
 * @description 索引对
 * @author guozhidong
 *
 */
public class IndexPair {
	// 针脚索引
	private Integer lockIndex;
	// 未知索引
	private Integer boxIndex;
	// 开启状态
	private Boolean openStatus = false;

	public IndexPair(Integer lockIndex, Integer boxIndex) {
		this.lockIndex = lockIndex;
		this.boxIndex = boxIndex;
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

	public Boolean getOpenStatus() {
		return this.openStatus;
	}

	public void setOpenStatus(Boolean openStatus) {
		this.openStatus = openStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boxIndex == null) ? 0 : boxIndex.hashCode());
		result = prime * result + ((lockIndex == null) ? 0 : lockIndex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndexPair other = (IndexPair) obj;
		if (boxIndex == null) {
			if (other.boxIndex != null)
				return false;
		} else if (!boxIndex.equals(other.boxIndex))
			return false;
		if (lockIndex == null) {
			if (other.lockIndex != null)
				return false;
		} else if (!lockIndex.equals(other.lockIndex))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IndexPair [lockIndex=" + lockIndex + ", boxIndex=" + boxIndex + "]";
	}
}