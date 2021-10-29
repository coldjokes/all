package com.cnbaosi.dto.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 补料单
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignFeedingList implements Serializable {
	private String wareHouseAlias; // 仓库别名
	private String userName; // 补料人名
	private String feedType; // 补料类型（MAT:按照物料补料；STA:按照货道补料）
	private List<FeignFeedingDetail> detailList; // 补料明细清单


	public String getWareHouseAlias() {
		return wareHouseAlias;
	}

	public void setWareHouseAlias(String wareHouseAlias) {
		this.wareHouseAlias = wareHouseAlias;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public List<FeignFeedingDetail> getDetailList() {
		if (this.detailList == null) {
			this.detailList = new ArrayList<>();
		}
		return this.detailList;
	}

	public void setDetailList(List<FeignFeedingDetail> detailList) {
		this.detailList = detailList;
	}
}