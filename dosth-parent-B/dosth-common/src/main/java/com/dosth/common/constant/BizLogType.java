package com.dosth.common.constant;

/**
 * 业务日志类型
 * 
 * @author guozhidong
 *
 */
public enum BizLogType implements IEnumState {

	ALL(0, null), // 全部日志
	BUSSINESS(1, "业务日志"), EXCEPTION(2, "异常日志");

	private Integer code;
	private String message;

	private BizLogType(Integer code, String message) {
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
			for (BizLogType bizLogType : BizLogType.values()) {
				if (bizLogType.getCode().equals(code)) {
					return bizLogType.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return BizLogType.valueOf(name).getMessage();
	}
}