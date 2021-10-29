package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 补料明细
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignFeedingDetail implements Serializable {
	private String staId; // 货道ID
	private Integer rowNo; // 行号
	private Integer colNo; // 列号
	private FeignMat mat; // 物料
	private Integer feedingNum; // 补料数量

	public FeignFeedingDetail() {
	}

	public FeignFeedingDetail(String staId, Integer rowNo, Integer colNo, FeignMat mat, Integer feedingNum) {
		this.staId = staId;
		this.rowNo = rowNo;
		this.colNo = colNo;
		this.mat = mat;
		this.feedingNum = feedingNum;
	}

	public String getStaId() {
		return staId;
	}

	public void setStaId(String staId) {
		this.staId = staId;
	}

	public Integer getRowNo() {
		return this.rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public Integer getColNo() {
		return this.colNo;
	}

	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}

	public FeignMat getMat() {
		return this.mat;
	}

	public void setMat(FeignMat mat) {
		this.mat = mat;
	}

	public Integer getFeedingNum() {
		return this.feedingNum;
	}

	public void setFeedingNum(Integer feedingNum) {
		this.feedingNum = feedingNum;
	}
}