package com.dosth.netty.dto;

import java.util.Arrays;

import com.dosth.netty.constant.ConstantValue;

/**
 * @description 阿诺协议数据包格式
 * <pre>
 * +——----——+——-----——+——----——+ 
 * |协议开始标志|  长度             |   数据       | 
 * +——----——+——-----——+——----——+ 
 * 1.协议开始标志head_data,为int类型的数据,16进制表示为0X76 
 * 2.传输数据的长度contentLength,int类型 
 * 3.要传输的数据
 * </pre>
 * @author guozhidong
 *
 */
public class AhnoProtocol {
	/**
	 * @description 消息的开头信息
	 */
	private int head_data = ConstantValue.HEAD_DATA;
	/**
	 * @description 消息的长度
	 */
	private int contentLength;

	/**
	 * @description 消息的内容
	 */
	private byte[] content;

	/**
	 * @description 构造方法
	 * 
	 * @param contentLength 消息数据的长度
	 * @param content       消息的数据
	 */
	public AhnoProtocol(int contentLength, byte[] content) {
		this.contentLength = contentLength;
		this.content = content;
	}

	public int getContentLength() {
		return this.contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public int getHead_data() {
		return this.head_data;
	}

	@Override
	public String toString() {
		return "AhnoProtocol [head_data=" + head_data + ", contentLength=" + contentLength + ", content="
				+ Arrays.toString(content) + "]";
	}
}