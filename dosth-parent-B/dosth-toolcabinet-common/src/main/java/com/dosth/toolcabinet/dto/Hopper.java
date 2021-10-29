package com.dosth.toolcabinet.dto;

import java.io.Serializable;
import java.util.Map;

import com.dosth.toolcabinet.enums.AddrType;

/**
 * 料斗
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class Hopper implements Serializable {
	// 料斗Host
	private String host;
	// 端口
	private int port;
	// 下位机地址
	private int slaveId;
	// 状态集合
	private Map<AddrType, Integer> statusMap;

	public Hopper(String host, int port, int slaveId) {
		this.host = host;
		this.port = port;
		this.slaveId = slaveId;
	}

	public Map<AddrType, Integer> getStatusMap() {
		return this.statusMap;
	}

	public void setStatusMap(Map<AddrType, Integer> statusMap) {
		this.statusMap = statusMap;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public int getSlaveId() {
		return this.slaveId;
	}
}