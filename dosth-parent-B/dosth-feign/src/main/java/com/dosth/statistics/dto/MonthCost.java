package com.dosth.statistics.dto;

import java.io.Serializable;

import com.dosth.statistics.enums.MonthEnum;

/**
 * 
 * @description 月度成本
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MonthCost implements Serializable {
	// 月份
	private MonthEnum monthEnum;
	// 成本
	private Double cost;

	public MonthCost() {
	}

	public MonthCost(MonthEnum monthEnum, Double cost) {
		this.monthEnum = monthEnum;
		this.cost = cost;
	}

	public MonthEnum getMonthEnum() {
		return this.monthEnum;
	}

	public void setMonthEnum(MonthEnum monthEnum) {
		this.monthEnum = monthEnum;
	}

	public Double getCost() {
		return this.cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
}