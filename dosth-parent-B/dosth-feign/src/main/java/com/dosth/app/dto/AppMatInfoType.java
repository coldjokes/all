package com.dosth.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AppMatInfoType", description = "物料类型")
public class AppMatInfoType {

	// 类型ID
	@ApiModelProperty(name = "kindId", value = "类型ID")
	private String kindId;

	// 类型名称
	@ApiModelProperty(name = "kindName", value = "类型名称")
	private String kindName;

	public AppMatInfoType() {
	}

	public AppMatInfoType(String kindId, String kindName) {
		this.kindId = kindId;
		this.kindName = kindName;
	}

	public String getKindId() {
		return kindId;
	}

	public void setKindId(String kindId) {
		this.kindId = kindId;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

}
