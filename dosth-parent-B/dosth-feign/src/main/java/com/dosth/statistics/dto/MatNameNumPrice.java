package com.dosth.statistics.dto;

import java.io.Serializable;

import com.dosth.statistics.enums.MonthEnum;

/**
 * 
 * @description 物料统计对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MatNameNumPrice implements Serializable {
	private String matName; // 物料名称
	private MonthEnum monthEnum; // 借出所属月份
	private Integer num; // 借出数量
	private Float price; // 物料价格

	public MatNameNumPrice(String matName, MonthEnum monthEnum, Integer num, Float price) {
		this.matName = matName;
		this.monthEnum = monthEnum;
		this.num = num;
		this.price = price;
	}

	public String getMatName() {
		return this.matName;
	}

	public MonthEnum getMonthEnum() {
		return this.monthEnum;
	}

	public Integer getNum() {
		return this.num;
	}

	public Float getPrice() {
		return this.price;
	}
}