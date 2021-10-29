package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @description 归还类型
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ReBackType implements Serializable {
	private String code; // 编码
	private String name; // 名称

	public ReBackType() {
	}

	public ReBackType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}
}