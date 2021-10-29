package com.cnbaosi.dto.tool;

import java.io.Serializable;
import java.util.List;

/**
 * @description 远程补料单
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignSupplyList implements Serializable {
	private String wareHouseAlias; // 仓库别名
	private List<FeignSupplyMat> supplyMatList; // 补料单详情

	public String getWareHouseAlias() {
		return this.wareHouseAlias;
	}

	public void setWareHouseAlias(String wareHouseAlias) {
		this.wareHouseAlias = wareHouseAlias;
	}

	public List<FeignSupplyMat> getSupplyMatList() {
		return this.supplyMatList;
	}

	public void setSupplyMatList(List<FeignSupplyMat> supplyMatList) {
		this.supplyMatList = supplyMatList;
	}
}