package com.cnbaosi.dto;

import java.io.Serializable;

/**
 * @description 坐标
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Position implements Serializable {
	// 行号
	private Integer rowNo;
	// 列号
	private Integer colNo;

	public Position(Integer rowNo, Integer colNo) {
		this.rowNo = rowNo;
		this.colNo = colNo;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public Integer getColNo() {
		return this.colNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((colNo == null) ? 0 : colNo.hashCode());
		result = prime * result + ((rowNo == null) ? 0 : rowNo.hashCode());
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
		Position other = (Position) obj;
		if (colNo == null) {
			if (other.colNo != null)
				return false;
		} else if (!colNo.equals(other.colNo))
			return false;
		if (rowNo == null) {
			if (other.rowNo != null)
				return false;
		} else if (!rowNo.equals(other.rowNo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Position [rowNo=" + rowNo + ", colNo=" + colNo + "]";
	}
}