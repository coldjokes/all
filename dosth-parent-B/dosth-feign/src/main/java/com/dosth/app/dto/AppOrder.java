package com.dosth.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dosth.constant.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(value = "AppOrder", description = "订单信息")
public class AppOrder implements Serializable {

	// 订单主键
	@ApiModelProperty(name = "orderId", value = "订单主键")
	private String orderId;

	// 订单状态:0 待使用, 1 已使用,-1 已失效
	@ApiModelProperty(name = "orderType", value = "订单状态:0 待使用, 1 已使用,-1 已失效")
	private String orderType;

	// 订单创建时间
	@ApiModelProperty(name = "orderCreatedTime", value = "订单创建时间")
	@JsonFormat(pattern = Constants.DATE_FORMAT, timezone = "GMT+8")
	private Date orderCreatedTime;

	// 订单二维码地址,全路径
	@ApiModelProperty(name = "orderCodeUrl", value = "订单二维码地址,全路径")
	private String orderCodeUrl;

	@ApiModelProperty(name = "orders", value = "订单详情")
	private List<AppOrderItem> orders; // 订单详情

	public AppOrder() {
	}

	public AppOrder(String orderId, String orderType, Date orderCreatedTime, String orderCodeUrl) {
		this.orderId = orderId;
		this.orderType = orderType;
		this.orderCreatedTime = orderCreatedTime;
		this.orderCodeUrl = orderCodeUrl;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getOrderCreatedTime() {
		return orderCreatedTime;
	}

	public void setOrderCreatedTime(Date orderCreatedTime) {
		this.orderCreatedTime = orderCreatedTime;
	}

	public String getOrderCodeUrl() {
		return orderCodeUrl;
	}

	public void setOrderCodeUrl(String orderCodeUrl) {
		this.orderCodeUrl = orderCodeUrl;
	}

	public List<AppOrderItem> getOrders() {
		if (this.orders == null) {
			this.orders = new ArrayList<>();
		}
		return this.orders;
	}

	public void setOrders(List<AppOrderItem> orders) {
		this.orders = orders;
	}
}