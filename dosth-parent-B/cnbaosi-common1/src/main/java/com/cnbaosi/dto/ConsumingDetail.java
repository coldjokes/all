package com.cnbaosi.dto;

import java.io.Serializable;

/**
 * @description 领取详情
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ConsumingDetail implements Serializable {
	// 坐标
	private Position position;
	// 数量
	private Integer amount;
	// 实取数量<可控抽屉柜>
	private Integer realNum;

	public ConsumingDetail(Position position) {
		this.position = position;
	}

	public ConsumingDetail(Position position, Integer amount) {
		this.position = position;
		this.amount = amount;
	}

	public ConsumingDetail(Position position, Integer amount, Integer realNum) {
		this.position = position;
		this.amount = amount;
		this.realNum = realNum;
	}

	public Position getPosition() {
		return this.position;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((position == null) ? 0 : position.hashCode());
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
		ConsumingDetail other = (ConsumingDetail) obj;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConsumingDetail [position=" + position.toString() + ", amount=" + amount + ", realNum=" + realNum + "]";
	}
}