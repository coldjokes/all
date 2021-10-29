package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 订单查询参数
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "OrderSearchParams", description = "订单查询参数")
public class OrderSearchParams implements Serializable {
	@ApiModelProperty(name = "orderType", value = "查询范围")
	private String orderType; // 0 待使用， 1 已使用， -1 已失效,。 可以为空，为空则为所有

	public String getOrderType() {
		if (this.orderType == null) {
			this.orderType = "";
		}
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
}