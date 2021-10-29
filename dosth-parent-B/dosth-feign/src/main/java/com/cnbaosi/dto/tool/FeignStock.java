package com.cnbaosi.dto.tool;

import java.io.Serializable;

/**
 * @description 库位信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class FeignStock implements Serializable {
	private FeignStockMat feignStockMat; // 当前库存物料
	private Integer maxStockNum; // 库位最大存储量
	private Integer curStockNum; // 库位当前存储量
	private Integer supplyNum; // 库位待补数量

	public FeignStock() {
	}

	public FeignStockMat getFeignStockMat() {
		return this.feignStockMat;
	}

	public void setFeignStockMat(FeignStockMat feignStockMat) {
		this.feignStockMat = feignStockMat;
	}

	public Integer getMaxStockNum() {
		return this.maxStockNum;
	}

	public void setMaxStockNum(Integer maxStockNum) {
		this.maxStockNum = maxStockNum;
	}

	public Integer getCurStockNum() {
		return this.curStockNum;
	}

	public void setCurStockNum(Integer curStockNum) {
		this.curStockNum = curStockNum;
	}

	public Integer getSupplyNum() {
		return this.supplyNum;
	}

	public void setSupplyNum(Integer supplyNum) {
		this.supplyNum = supplyNum;
	}
}