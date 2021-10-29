package com.dosth.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AppBorrowInfo", description = "刀具借用信息")
public class AppBorrowInfo {

	// 刀具编号
	@ApiModelProperty(name = "knifeId", value = "刀具编号")
	private String knifeId;

	// 刀具名称
	@ApiModelProperty(name = "knifeName", value = "刀具名称")
	private String knifeName;

	// 用户名
	@ApiModelProperty(name = "username", value = "用户名")
	private String username;
	
	@ApiModelProperty(name = "time", value = "时间")
	//@JsonFormat(pattern = Constants.DATE_FORMAT, timezone = "GMT+8")
	private String time;

	// 借出数量
	@ApiModelProperty(name = "num", value = "借出数量")
	private String num;

	// 图片
	@ApiModelProperty(name = "knifeImage", value = "图片")
	private String knifeImage;
	
	// 包装单位
	@ApiModelProperty(name = "unitName", value = "包装单位")
	private String unitName;

	public AppBorrowInfo() {
	}

	public AppBorrowInfo(String knifeId, String knifeName, String username, String time, String num, String knifeImage, String unitName) {
		this.knifeId = knifeId;
		this.knifeName = knifeName;
		this.username = username;
		this.time = time;
		this.num = num;
		this.knifeImage = knifeImage;
		this.unitName = unitName;
	}

	public String getKnifeId() {
		return knifeId;
	}

	public void setKnifeId(String knifeId) {
		this.knifeId = knifeId;
	}

	public String getKnifeName() {
		return knifeName;
	}

	public void setKnifeName(String knifeName) {
		this.knifeName = knifeName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getKnifeImage() {
		return knifeImage;
	}

	public void setKnifeImage(String knifeImage) {
		this.knifeImage = knifeImage;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
