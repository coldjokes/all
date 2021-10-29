package com.cnbaosi.modbus;

/**
 * @description modbus服务端配置
 * 
 * @author guozhidong
 *
 */
public class ModbusServerConfig {

	/** Modbus服务器地址 */
	private String host = "localhost";
	/** Modbus服务器端口 */
	private int port = 502;

	public ModbusServerConfig(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}
}