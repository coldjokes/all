package com.dosth.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @description 库存提醒
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class StockTip extends MatInfo implements Serializable {
	private List<StockDetail> detailList; // 库存明细列表

	public List<StockDetail> getDetailList() {
		return this.detailList;
	}

	public void setDetailList(List<StockDetail> detailList) {
		this.detailList = detailList;
	}
}