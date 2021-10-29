package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 补料信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignSupplyMat implements Serializable {
	private String barCode; // 物料Code
	private Integer supplyNum; // 物料补料数量

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public Integer getSupplyNum() {
		return this.supplyNum;
	}

	public void setSupplyNum(Integer supplyNum) {
		this.supplyNum = supplyNum;
	}
}