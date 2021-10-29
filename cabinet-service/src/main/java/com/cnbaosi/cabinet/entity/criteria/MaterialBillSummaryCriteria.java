package com.cnbaosi.cabinet.entity.criteria;

/**
 * 出入库汇总表
 * 
 * @author Weifeng Li
 */
public class MaterialBillSummaryCriteria {

	private String text; // 物料名称、编号、规格
	private Integer hoursOfDay; //小时/天
	private Integer daysOfWeek; //天/周

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public Integer getHoursOfDay() {
		return hoursOfDay;
	}

	public void setHoursOfDay(Integer hoursOfDay) {
		this.hoursOfDay = hoursOfDay;
	}

	public Integer getDaysOfWeek() {
		return daysOfWeek;
	}

	public void setDaysOfWeek(Integer daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
}
