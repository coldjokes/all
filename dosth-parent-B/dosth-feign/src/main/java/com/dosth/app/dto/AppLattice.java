package com.dosth.app.dto;

import java.io.Serializable;

/**
 * Engine槽位
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class AppLattice implements Serializable {
	/** 格子Id */
	private String id;
	/** 列号 */
	private int colNo;
	/** 刀具信息 */
	private AppKnifes knife;

	public AppLattice() {
	}

	public AppLattice(String id, int colNo, AppKnifes knife) {
		this.id = id;
		this.colNo = colNo;
		this.knife = knife;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getColNo() {
		return this.colNo;
	}

	public void setColNo(int colNo) {
		this.colNo = colNo;
	}

	public AppKnifes getKnife() {
		return this.knife;
	}

	public void setKnife(AppKnifes knife) {
		this.knife = knife;
	}
}