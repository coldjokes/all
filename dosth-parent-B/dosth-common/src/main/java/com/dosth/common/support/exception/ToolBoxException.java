package com.dosth.common.support.exception;

import com.dosth.common.support.StrKit;

/**
 * 工具异常
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class ToolBoxException extends RuntimeException {
	public ToolBoxException(Throwable e) {
		super(e.getMessage(), e);
	}
	
	public ToolBoxException(String message) {
		super(message);
	}
	
	public ToolBoxException(String messageTemplate, Object... params) {
		super(StrKit.format(messageTemplate, params));
	}
	
	public ToolBoxException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
	public ToolBoxException(Throwable throwable, String messageTemplate, Object... params) {
		super(StrKit.format(messageTemplate, params), throwable);
	}
}