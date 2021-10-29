package com.dosth.tool.common.dto;

import java.io.Serializable;

/**
 * @description 暂存柜物料详情
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class SubBoxMatInfo implements Serializable {
	private String matInfoId; // 物料Id
	private String matName; // 物料名称
	private String barCode; // 物料编码
	private String spec; // 物料规格

	public SubBoxMatInfo() {
	}

	public SubBoxMatInfo(String matInfoId, String matName, String barCode, String spec) {
		this.matInfoId = matInfoId;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
	}

	public String getMatInfoId() {
		return this.matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
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
}