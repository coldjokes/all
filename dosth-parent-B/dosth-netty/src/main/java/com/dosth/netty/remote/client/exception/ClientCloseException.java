package com.dosth.netty.remote.client.exception;

import com.dosth.netty.remote.exception.SystemException;

@SuppressWarnings("serial")
public class ClientCloseException extends SystemException {

	private static final String MESSAGE = "Can't close this client, beacuse the client didn't connet a server.";
	
	public ClientCloseException() {
		super(MESSAGE);
	}
}