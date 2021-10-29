package com.dosth.toolcabinet.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.dosth.toolcabinet.enums.PrintInfoType;

/**
 * @description 归还打印信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ReturnBackPrintInfo implements Serializable {
	/**
	 * @description 二维码信息
	 * <p> 物料借出Id,归还类型Id,应归还数量,损坏数量,丢失数量,帐户Id</p>
	 */
	private Map<PrintInfoType, String> infoMap; // 归还信息
	
	private String matUseBillId; // 物料借出Id
	
	private String accountId; // 归还人员
	
	private String isGetNewOne; // 是否以旧换新
	
	private String matInfoId; // 物料Id
	
	private String returnType; // 归还类型

	public ReturnBackPrintInfo() {
	}
	
	public ReturnBackPrintInfo(String matUseBillId, String accountId, String isGetNewOne, String matInfoId, String returnType) {
		this.matUseBillId = matUseBillId;
		this.accountId = accountId;
		this.isGetNewOne = isGetNewOne;
		this.matInfoId = matInfoId;
		this.returnType = returnType;
	}

	public Map<PrintInfoType, String> getInfoMap() {
		if (this.infoMap == null) {
			this.infoMap = new HashMap<>();
		}
		return this.infoMap;
	}

	public void setInfoMap(Map<PrintInfoType, String> infoMap) {
		this.infoMap = infoMap;
	}

	public String getMatUseBillId() {
		return matUseBillId;
	}

	public void setMatUseBillId(String matUseBillId) {
		this.matUseBillId = matUseBillId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIsGetNewOne() {
		return isGetNewOne;
	}

	public void setIsGetNewOne(String isGetNewOne) {
		this.isGetNewOne = isGetNewOne;
	}

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
}