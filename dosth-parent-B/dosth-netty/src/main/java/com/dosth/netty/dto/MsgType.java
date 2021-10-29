package com.dosth.netty.dto;

/**
 * @description 消息类型
 * @author guozhidong
 *
 */
public enum MsgType {
	REQUEST(1, "客户端请求"), RESPONSE(2, "服务端响应"), TXT(3, "文本信息"), SOUND(4, "声音信息"), PRINTSCAN(5, "手机二维码扫描仪启用停用通讯"),
	LATTICEVALUE(6, "格子数量"), SUBALERTINFO(7, "暂存柜弹出信息"),BORROWPOST(8, "领用记录推送"),
	PING(101, "客户端PING"), PONG(102, "服务端PONG");

	private int code;
	private String name;

	private MsgType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}
}