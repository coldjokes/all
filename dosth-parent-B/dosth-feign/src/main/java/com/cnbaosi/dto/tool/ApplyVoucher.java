package com.cnbaosi.dto.tool;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @description 申请单
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ApplyVoucher implements Serializable {
	// 申请单号
	private String applyNo;
	// 申请物料详情列表
	private List<ApplyMatDetail> detailList;

	public ApplyVoucher() {
	}

	public String getApplyNo() {
		return this.applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public List<ApplyMatDetail> getDetailList() {
		if (this.detailList == null) {
			this.detailList = new ArrayList<>();
		}
		return this.detailList;
	}

	public void setDetailList(List<ApplyMatDetail> detailList) {
		this.detailList = detailList;
	}
}