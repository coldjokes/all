package com.dosth.toolcabinet.dto;

public class NoticeMgrInfo {

	// 刀具柜id
	private String equSettingId;
	// 数量
	private Integer num;
	// 计数
	private Integer count;
	// 预警值
	private Integer warnValue;
	// 邮箱
	private String email;
	// 类型
	private String noticeType;

	public NoticeMgrInfo() {
		super();
	}

	public NoticeMgrInfo(String equSettingId, Integer num, Integer count, Integer warnValue, String noticeType) {
		super();
		this.equSettingId = equSettingId;
		this.num = num;
		this.count = count;
		this.warnValue = warnValue;
		this.noticeType = noticeType;
	}

	public String getEquSettingId() {
		return equSettingId;
	}

	public void setEquSettingId(String equSettingId) {
		this.equSettingId = equSettingId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getWarnValue() {
		return warnValue;
	}

	public void setWarnValue(Integer warnValue) {
		this.warnValue = warnValue;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(String noticeType) {
		this.noticeType = noticeType;
	}

}
