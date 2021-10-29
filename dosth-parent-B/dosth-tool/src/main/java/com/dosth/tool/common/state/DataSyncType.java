package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * @description 数据同步类型
 * @author chenlei
 *
 */
public enum DataSyncType implements IEnumState {
	
	BORROW(1, "借出数据同步"), FEED(2, "补料数据同步"), BACK(2, "归还数据同步");
	
	private Integer code;
	private String message;

	private DataSyncType(Integer code, String message) {
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
			for (FeedType type : FeedType.values()) {
				if (type.getCode().equals(code)) {
					return type.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return FeedType.valueOf(name).getMessage();
	}
}