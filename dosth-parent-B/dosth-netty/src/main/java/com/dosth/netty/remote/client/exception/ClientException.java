package com.dosth.netty.remote.client.exception;

import com.dosth.netty.remote.exception.SystemException;

@SuppressWarnings("serial")
public class ClientException extends SystemException {

	public ClientException(Exception cause) {
		super(cause);
	}
}