package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(value = "AppRecycleReview", description = "回收审核")
public class AppRecycleReview implements Serializable {

	// 状态
	@ApiModelProperty(name = "status", value = "状态")
	private Boolean status;

	// 提示信息
	@ApiModelProperty(name = "msg", value = "提示信息")
	private String msg;

	// 借用详情
	@ApiModelProperty(name = "data", value = "借用详情")
	private AppBorrowInfo data; 

	public AppRecycleReview() {
	}

	public AppRecycleReview(boolean status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public AppBorrowInfo getData() {
		return data;
	}

	public void setData(AppBorrowInfo data) {
		this.data = data;
	}

}
