package com.cnbaosi.dto.tool;

/**
 *  
 * 
 * @author chenlei  
 */
public class FeignStaDetail {

	private String staId; //单元格id
	private Integer rowNo; //单元格行号
	private Integer colNo; //单元格列号
	private Integer curNum; //单元格库存
	private Integer waitNum; //待补数量
	private Integer maxReserve; //单元格最大存储
	private String status; //单元格状态 ENABLE启用  DISABLE禁用
	private FeignMat matInfo; //物料信息
	private Integer warnVal; //警号阈值
	
	
	public String getStaId() {
		return staId;
	}
	public void setStaId(String staId) {
		this.staId = staId;
	}
	public Integer getRowNo() {
		return rowNo;
	}
	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
	public Integer getColNo() {
		return colNo;
	}
	public void setColNo(Integer colNo) {
		this.colNo = colNo;
	}
	public Integer getCurNum() {
		return curNum;
	}
	public void setCurNum(Integer curNum) {
		this.curNum = curNum;
	}
	public Integer getWaitNum() {
		return waitNum;
	}
	public void setWaitNum(Integer waitNum) {
		this.waitNum = waitNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public FeignMat getMatInfo() {
		return matInfo;
	}
	public void setMatInfo(FeignMat matInfo) {
		this.matInfo = matInfo;
	}
	public Integer getMaxReserve() {
		return maxReserve;
	}
	public void setMaxReserve(Integer maxReserve) {
		this.maxReserve = maxReserve;
	}
	public Integer getWarnVal() {
		return warnVal;
	}
	public void setWarnVal(Integer warnVal) {
		this.warnVal = warnVal;
	}
}

