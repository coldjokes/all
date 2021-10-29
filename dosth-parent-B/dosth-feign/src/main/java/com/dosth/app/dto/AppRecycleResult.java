package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(value = "AppRecycleResult", description = "审核结果")
public class AppRecycleResult implements Serializable {
	// 状态
	@ApiModelProperty(name = "status", value = "状态")
	private Boolean status;

	// 提示信息
	@ApiModelProperty(name = "msg", value = "提示信息")
	private String msg;


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
}
