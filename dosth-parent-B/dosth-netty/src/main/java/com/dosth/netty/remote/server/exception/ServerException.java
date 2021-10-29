package com.dosth.netty.remote.server.exception;

import com.dosth.netty.remote.exception.SystemException;

@SuppressWarnings("serial")
public class ServerException extends SystemException {

	private String slaveId;
	
	public ServerException(String slaveId, Exception cause) {
		super(cause);
		this.slaveId = slaveId;
	}

	public String getSlaveId() {
		return this.slaveId;
	}
}