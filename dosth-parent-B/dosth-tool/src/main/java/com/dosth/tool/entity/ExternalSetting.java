package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 设备设置
 * 
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class ExternalSetting extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '外部访问地址'")
	@PageTableTitle(value = "外部访问地址")
	private String url;

	@Column(columnDefinition = "varchar(36) COMMENT '补料方法名'")
	@PageTableTitle(value = "补料方法名")
	private String feedMethod;

	@Column(columnDefinition = "varchar(36) COMMENT '借出方法名'")
	@PageTableTitle(value = "借出方法名")
	private String borrowMethod;

	@Column(columnDefinition = "varchar(36) COMMENT '归还方法名'")
	@PageTableTitle(value = "归还方法名")
	private String backMethod;

	@Column(columnDefinition = "varchar(36) COMMENT '获取Token'")
	@PageTableTitle(value = "获取Token")
	private String tokenMethod;

	@Column(columnDefinition = "varchar(36) COMMENT '其它方法名1'")
	@PageTableTitle(value = "其它方法名1")
	private String method1;

	@Column(columnDefinition = "varchar(36) COMMENT '其它方法名2'")
	@PageTableTitle(value = "其它方法名2")
	private String method2;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFeedMethod() {
		return feedMethod;
	}

	public void setFeedMethod(String feedMethod) {
		this.feedMethod = feedMethod;
	}

	public String getBorrowMethod() {
		return borrowMethod;
	}

	public void setBorrowMethod(String borrowMethod) {
		this.borrowMethod = borrowMethod;
	}

	public String getBackMethod() {
		return backMethod;
	}

	public void setBackMethod(String backMethod) {
		this.backMethod = backMethod;
	}

	public String getTokenMethod() {
		return tokenMethod;
	}

	public void setTokenMethod(String tokenMethod) {
		this.tokenMethod = tokenMethod;
	}

	public String getMethod1() {
		return method1;
	}

	public void setMethod1(String method1) {
		this.method1 = method1;
	}

	public String getMethod2() {
		return method2;
	}

	public void setMethod2(String method2) {
		this.method2 = method2;
	}

}