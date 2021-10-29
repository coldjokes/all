package com.dosth.app.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @description 分页条件对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@ApiModel(value = "AppRecycleInput", description = "回收审核入参")
public class AppRecycleInput implements Serializable {

	// 二维码
	@ApiModelProperty(name = "code", value = "二维码")
	private String code;

	// 类型
	@ApiModelProperty(name = "type", value = "审核类型")
	private String type;
	
	// 对应数量
	@ApiModelProperty(name = "num", value = "对应数量")
	private int num;
	
	// 对应内容
	@ApiModelProperty(name = "content", value = "对应内容")
	private String content;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}