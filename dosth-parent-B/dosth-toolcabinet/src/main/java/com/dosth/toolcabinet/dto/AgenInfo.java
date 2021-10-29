package com.dosth.toolcabinet.dto;

import java.io.Serializable;

/**
 * @description 代理商信息
 * @author Zhidong.Guo
 *
 */
@SuppressWarnings("serial")
public class AgenInfo implements Serializable {
	private String name;
	private String title;
	private String content;
	private String stock;
	private String phone1;
	private String phone2;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStock() {
		return this.stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getPhone1() {
		return this.phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return this.phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
}