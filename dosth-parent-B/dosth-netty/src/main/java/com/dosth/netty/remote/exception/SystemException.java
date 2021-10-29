package com.dosth.netty.remote.exception;

@SuppressWarnings("serial")
public class SystemException extends RuntimeException {
	protected SystemException(String errorMessage, final Object... args) {
		super(String.format(errorMessage, args));
	}

	protected SystemException(String errorMessage, Exception cause, Object... args) {
		super(String.format(errorMessage, args), cause);
	}

	protected SystemException(Exception cause) {
		super(cause.getMessage(), cause);
	}
}