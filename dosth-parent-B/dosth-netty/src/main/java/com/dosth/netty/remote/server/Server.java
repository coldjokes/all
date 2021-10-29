package com.dosth.netty.remote.server;

public interface Server {
	String SYSTEM_MESSAGE_ID = "-1";

	/**
	 * @description 启动
	 */
	void start();

	/**
	 * @description 停止
	 */
	void stop();
}