package com.dosth.websocket.dto;

import com.dosth.websocket.constant.WsMsgType;

/**
 * WebSocket消息
 * 
 * @author guozhidong
 *
 */
public class WsMsg {
	/**
	 * 消息类型
	 */
	private WsMsgType msgType;
	/**
	 * 消息内容
	 */
	private Object msgContent;

	public WsMsg(WsMsgType msgType, Object msgContent) {
		this.msgType = msgType;
		this.msgContent = msgContent;
	}

	public WsMsgType getMsgType() {
		return this.msgType;
	}

	public Object getMsgContent() {
		return this.msgContent;
	}
}