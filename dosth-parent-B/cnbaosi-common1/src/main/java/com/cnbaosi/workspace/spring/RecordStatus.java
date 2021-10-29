package com.cnbaosi.workspace.spring;

/**
 * @description 取料状态
 * @author guozhidong
 *
 */
public class RecordStatus {
	// 取料数量
	private int amount;
	// 是否完成,默认未完成
	private Boolean isFinish = false;

	public RecordStatus() {
		setIsFinish(false);
	}

	public RecordStatus(int amount) {
		setIsFinish(false);
		this.amount = amount;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Boolean getIsFinish() {
		return this.isFinish;
	}

	public void setIsFinish(Boolean isFinish) {
		this.isFinish = isFinish;
	}
}