package com.dosth.toolcabinet.dto;

import java.io.Serializable;
/**
 * @description 物料信息
 * 
 * @Author guozhidong
 */
@SuppressWarnings("serial")
public class MatDetail implements Serializable {
	// 物料Id
	private String matId;
	// 物料名称
	private String matName;
	// 编号
	private String barCode;
	// 型号规格
	private String spec;
	// 包装数量(单包装数量)
	private Integer packNum;
	// 剩余数量
	private Integer remainNum;
	// 借出类型编码
	private String borrowTypeCode;
	// 物料图片
	private String pic;
	 //当前类别tree的id
	private String categoryTreeId;
	 //所有包含此物料类别tree的id ，用,分隔
	private String categoryTreeIds;

	public MatDetail() {
	}

	public MatDetail(String matId, String matName, String barCode, String spec, Integer packNum,
			Integer remainNum, String borrowTypeCode, String pic) {
		this.matId = matId;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.remainNum = remainNum;
		this.borrowTypeCode = borrowTypeCode;
		this.pic = pic;
	}
	public MatDetail(String matId, String matName, String barCode, String spec, Integer packNum,
			Integer remainNum, String borrowTypeCode, String pic, String categoryTreeId, String categoryTreeIds) {
		this.matId = matId;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.remainNum = remainNum;
		this.borrowTypeCode = borrowTypeCode;
		this.pic = pic;
		this.categoryTreeId = categoryTreeId;
		this.categoryTreeIds = categoryTreeIds;
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getMatName() {
		return this.matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Integer getPackNum() {
		return this.packNum;
	}

	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}

	public Integer getRemainNum() {
		return this.remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}

	public String getBorrowTypeCode() {
		return this.borrowTypeCode;
	}

	public void setBorrowTypeCode(String borrowTypeCode) {
		this.borrowTypeCode = borrowTypeCode;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getCategoryTreeId() {
		return categoryTreeId;
	}

	public void setCategoryTreeId(String categoryTreeId) {
		this.categoryTreeId = categoryTreeId;
	}

	public String getCategoryTreeIds() {
		return categoryTreeIds;
	}

	public void setCategoryTreeIds(String categoryTreeIds) {
		this.categoryTreeIds = categoryTreeIds;
	}
}