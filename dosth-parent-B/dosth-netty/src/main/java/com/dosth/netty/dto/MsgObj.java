package com.dosth.netty.dto;

import java.io.Serializable;

/**
 * @description 消息对象
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MsgObj implements Serializable {
	// 消息类型
	private MsgType msgType;
	// 消息体
	private Object content;

	public MsgObj() {
	}

	public MsgObj(MsgType msgType, Object content) {
		this.msgType = msgType;
		this.content = content;
	}

	public MsgType getMsgType() {
		return this.msgType;
	}

	public void setMsgType(MsgType msgType) {
		this.msgType = msgType;
	}

	public Object getContent() {
		return this.content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}