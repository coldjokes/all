package com.dosth.netty.remote.client;

import java.net.InetSocketAddress;

import com.dosth.netty.dto.AhnoProtocol;

/**
 * @description 客户端
 * 
 * @author guozhidong
 *
 */
public interface Client {
	/**
	 * @description 连接
	 */
	public void connect();

	/**
	 * @description 发送请求
	 * 
	 * @param protocol
	 */
	public void send(AhnoProtocol protocol);

	/**
	 * @description 获取远程地址
	 */
	public InetSocketAddress getRemoteAddress();

	/**
	 * @description 关闭
	 */
	public void close();
}