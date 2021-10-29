package com.cnbaosi.dto;

import java.io.Serializable;
import java.util.Arrays;

import com.cnbaosi.enums.ReturnMsgType;
import com.cnbaosi.util.SerialTool;

/**
 * @description 消息体
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Message implements Serializable {
	// 消息类型
	private ReturnMsgType type;
	// 数据
	private byte[] data;
	// HexString
	private String hexString;
	// 自定义信息
	private String customMsg;

	public Message(byte[] data) {
		this.data = data;
		setHexString(SerialTool.bytesToHexString(data));
	}

	public Message(byte[] data, String hexString) {
		this.data = data;
		this.hexString = hexString;
	}

	public ReturnMsgType getType() {
		return this.type;
	}

	public void setType(ReturnMsgType type) {
		this.type = type;
	}

	public byte[] getData() {
		return this.data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getHexString() {
		return this.hexString;
	}

	public void setHexString(String hexString) {
		this.hexString = hexString;
	}

	public String getCustomMsg() {
		if (customMsg == null || "".equals(customMsg)) {
			return type != null ? type.getDesc() : "未定义消息类型";
		}
		return this.customMsg;
	}

	public void setCustomMsg(String customMsg) {
		this.customMsg = customMsg;
	}

	@Override
	public String toString() {
		return "Message [name="+ (type != null ? type.name() : "未知类型") +",type=" +  (type != null ? type.getDesc() : "未定义消息类型")  + ", data=" + Arrays.toString(data) + ", hexString=" + hexString + ", customMsg="
				+ (customMsg == null || "".equals(customMsg) ? type != null ? type.getDesc() : "未定义消息类型" : customMsg) + "]";
	}
}