package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * 
 * @Description 借出类型
 * @Author guozhidong
 */
@SuppressWarnings("serial")
public class RpcBorrowType implements Serializable {
	// Code
	private String code;
	// 名称
	private String name;

	public RpcBorrowType() {
	}

	public RpcBorrowType(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}