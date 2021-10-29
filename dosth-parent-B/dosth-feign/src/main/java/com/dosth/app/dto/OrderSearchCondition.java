package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 订单查询条件
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "OrderSearchCondition", description = "订单查询条件")
public class OrderSearchCondition implements Serializable {
	@ApiModelProperty(name = "page", value = "分页条件")
	private PageCondition page;
	@ApiModelProperty(name = "params", value = "订单查询参数")
	private OrderSearchParams params;

	public OrderSearchCondition() {
	}

	public OrderSearchCondition(PageCondition page, OrderSearchParams params) {
		this.page = page;
		this.params = params;
	}

	public PageCondition getPage() {
		return this.page;
	}

	public void setPage(PageCondition page) {
		this.page = page;
	}

	public OrderSearchParams getParams() {
		return this.params;
	}

	public void setParams(OrderSearchParams params) {
		this.params = params;
	}
}