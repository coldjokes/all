package com.dosth.app.dto;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AppSubscribe", description = "预约信息")
public class AppSubscribe {

	// 预约人姓名
	@ApiModelProperty(name = "subscribeName", value = "预约人姓名")
	private String subscribeName;

	// 预约时间
	@ApiModelProperty(name = "subscribeCreatedTime", value = "预约时间")
	private Date subscribeCreatedTime;

	// 预约件数
	@ApiModelProperty(name = "subscribeTotal", value = "预约件数")
	private int subscribeTotal;

	public AppSubscribe() {
	}

	public AppSubscribe(String subscribeName, Date subscribeCreatedTime, int subscribeTotal) {
		this.subscribeName = subscribeName;
		this.subscribeCreatedTime = subscribeCreatedTime;
		this.subscribeTotal = subscribeTotal;
	}

	public String getSubscribeName() {
		return subscribeName;
	}

	public void setSubscribeName(String subscribeName) {
		this.subscribeName = subscribeName;
	}

	public Date getSubscribeCreatedTime() {
		return subscribeCreatedTime;
	}

	public void setSubscribeCreatedTime(Date subscribeCreatedTime) {
		this.subscribeCreatedTime = subscribeCreatedTime;
	}

	public int getSubscribeTotal() {
		return subscribeTotal;
	}

	public void setSubscribeTotal(int subscribeTotal) {
		this.subscribeTotal = subscribeTotal;
	}

}
