package com.dosth.netty.constant;

/**
 * @description Netty配置
 * @author guozhidong
 *
 */
public class NettyConfig {
	/**
	 * @description Netty服务器地址
	 */
	private String host;

	/**
	 * @description Netty服务器端口
	 */
	private int port;
	
	/**
	 * @description 最大Boss线程量
	 */
	private int maxBossThreads = 1;
	
	/***
	 * @description 最大Worker线程量
	 */
	private int maxWorkThreads = 2;

	/**
	 * @description 数据包最大长度
	 */
	private int maxFrameLength = 65535;

	public NettyConfig(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public NettyConfig(String host, int port, int maxBossThreads, int maxWorkThreads) {
		this.host = host;
		this.port = port;
		this.maxBossThreads = maxBossThreads;
		this.maxWorkThreads = maxWorkThreads;
	}

	public NettyConfig(String host, int port, int maxBossThreads, int maxWorkThreads, int maxFrameLength) {
		this.host = host;
		this.port = port;
		this.maxBossThreads = maxBossThreads;
		this.maxWorkThreads = maxWorkThreads;
		this.maxFrameLength = maxFrameLength;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public int getMaxBossThreads() {
		return this.maxBossThreads;
	}

	public int getMaxWorkThreads() {
		return this.maxWorkThreads;
	}

	public int getMaxFrameLength() {
		return this.maxFrameLength;
	}
}