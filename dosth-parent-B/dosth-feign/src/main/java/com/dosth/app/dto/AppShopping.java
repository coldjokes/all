package com.dosth.app.dto;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 预约详情
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppShopping", description = "预约详情")
public class AppShopping implements Serializable {
	@ApiModelProperty(name = "data", value = "预约详情清单")
	private List<AppCart> data;

	public List<AppCart> getData() {
		return this.data;
	}

	public void setData(List<AppCart> data) {
		this.data = data;
	}
}