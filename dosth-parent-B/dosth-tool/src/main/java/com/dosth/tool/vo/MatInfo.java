package com.dosth.tool.vo;

import java.io.Serializable;

/**
 * @description 物料信息
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MatInfo implements Serializable {
	// Id
	private String matId;
	// 编号
	private String barCode;
	// 名称
	private String matName;
	// 规格
	private String spec;

	public MatInfo() {
	}

	public MatInfo(String matId, String barCode, String matName, String spec) {
		this.matId = matId;
		this.barCode = barCode;
		this.matName = matName;
		this.spec = spec;
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
}