package com.dosth.tool.common.dto;

import java.io.Serializable;

/**
 * @description 物料关联详情
 * @author chenlei
 *
 */
@SuppressWarnings("serial")
public class MatAssociationInfo implements Serializable {
	
	private String id; // 物料id
	
	private String matEquName; // 物料名称
	
	private String barCode; // 物料编码
	
	private String spec; // 物料规格
	
	private int flag; // 标志位
	
	private String icon; // 图标

	public MatAssociationInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMatEquName() {
		return matEquName;
	}

	public void setMatEquName(String matEquName) {
		this.matEquName = matEquName;
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

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}