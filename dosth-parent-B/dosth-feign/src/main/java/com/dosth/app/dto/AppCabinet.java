package com.dosth.app.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AppCabinet", description = "机柜信息")
public class AppCabinet {

	// 机柜id
	@ApiModelProperty(name = "cupboardId", value = "机柜id")
	private String cupboardId;

	// 机柜名称
	@ApiModelProperty(name = "cupboardName", value = "机柜名称")
	private String cupboardName;

	// 机柜位置
	@ApiModelProperty(name = "cupboardPosition", value = "机柜位置")
	private String cupboardPosition;

	// 机柜库存
	@ApiModelProperty(name = "cupboardTotal", value = "机柜库存")
	private int cupboardTotal;

	// 预约人数
	@ApiModelProperty(name = "cupboardSubscribeTotal", value = "预约人数")
	private int cupboardSubscribeTotal;

	// 图片
	@ApiModelProperty(name = "cabinetImg", value = "图片")
	private String cabinetImg;

	public AppCabinet() {
	}

	public AppCabinet(String cupboardId, String cupboardName, String cupboardPosition, int cupboardTotal,
			int cupboardSubscribeTotal) {
		this.cupboardId = cupboardId;
		this.cupboardName = cupboardName;
		this.cupboardPosition = cupboardPosition;
		this.cupboardTotal = cupboardTotal;
		this.cupboardSubscribeTotal = cupboardSubscribeTotal;
	}

	public String getCupboardId() {
		return cupboardId;
	}

	public void setCupboardId(String cupboardId) {
		this.cupboardId = cupboardId;
	}

	public String getCupboardName() {
		return cupboardName;
	}

	public void setCupboardName(String cupboardName) {
		this.cupboardName = cupboardName;
	}

	public String getCupboardPosition() {
		return cupboardPosition;
	}

	public void setCupboardPosition(String cupboardPosition) {
		this.cupboardPosition = cupboardPosition;
	}

	public int getCupboardTotal() {
		return cupboardTotal;
	}

	public void setCupboardTotal(int cupboardTotal) {
		this.cupboardTotal = cupboardTotal;
	}

	public int getCupboardSubscribeTotal() {
		return cupboardSubscribeTotal;
	}

	public void setCupboardSubscribeTotal(int cupboardSubscribeTotal) {
		this.cupboardSubscribeTotal = cupboardSubscribeTotal;
	}

	public String getCabinetImg() {
		return this.cabinetImg;
	}

	public void setCabinetImg(String cabinetImg) {
		this.cabinetImg = cabinetImg;
	}
}