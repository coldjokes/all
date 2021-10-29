package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@SuppressWarnings("serial")
@ApiModel(value = "AppCloseDoor", description = "机柜关门")
public class AppCloseDoor implements Serializable {
	
	// 状态
	@ApiModelProperty(name = "status", value = "状态")
	private Boolean status;

	// 回传指令
	@ApiModelProperty(name = "cmd", value = "回传指令")
	private String cmd;

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

}
