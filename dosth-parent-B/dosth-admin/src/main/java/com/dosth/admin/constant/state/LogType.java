package com.dosth.admin.constant.state;

import com.dosth.common.constant.IEnumState;

/**
 * 日志类型
 * 
 * @author guozhidong
 *
 */
public enum LogType implements IEnumState {

	LOGIN(1, "登录日志"), LOGIN_FAIL(2, "登录失败日志"), EXIT(3, "退出日志"), EXCEPTION(-1, "异常日志"), BUSSINESS(4, "业务日志");

	private Integer code;
	private String message;

	private LogType(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String valueOfCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (LogType logType : LogType.values()) {
				if (logType.getCode().equals(code)) {
					return logType.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return LogType.valueOf(name).getMessage();
	}
}