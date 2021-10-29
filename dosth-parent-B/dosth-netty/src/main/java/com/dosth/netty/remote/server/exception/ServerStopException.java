package com.dosth.netty.remote.server.exception;

import com.dosth.netty.remote.exception.SystemException;

@SuppressWarnings("serial")
public class ServerStopException extends SystemException {

	private static final String MESSAGE = "Can't stop this server, because the server didn't start yet.";
	
	public ServerStopException() {
		super(MESSAGE);
	}
}