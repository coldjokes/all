package com.dosth.toolcabinet.enums;

/**
 * @description 副柜操作类型
 * @Author guozhidong
 */
public enum SubCabinetOpType {

	TMPSTOR("1", "暂存"), BORRBOX("2", "取料");
	/**
	 * code
	 */
	private String code;
	/**
	 * 描述
	 */
	private String desc;

	private SubCabinetOpType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
}