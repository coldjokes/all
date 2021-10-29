package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 申请物料详情
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ApplyMatDetail implements Serializable {
	private String matId; // 物料Id
	private String barCode; // 物料编码
	private String matName; // 物料名称
	private String spec; // 规格
	private String borrowType; // 借出类型
	private Float borrowNum; // 借出数量
	private Integer remainNum; // 剩余数量
	
	private Integer realNum; // 实取数量

	public ApplyMatDetail() {
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getMatName() {
		return this.matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getBorrowType() {
		return this.borrowType;
	}

	public void setBorrowType(String borrowType) {
		this.borrowType = borrowType;
	}

	public Float getBorrowNum() {
		return this.borrowNum;
	}

	public void setBorrowNum(Float borrowNum) {
		this.borrowNum = borrowNum;
	}

	public Integer getRemainNum() {
		return this.remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}

	public Integer getRealNum() {
		return this.realNum;
	}

	public void setRealNum(Integer realNum) {
		this.realNum = realNum;
	}
}