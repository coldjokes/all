package com.dosth.tool.common.state;

import com.dosth.common.constant.IEnumState;

/**
 * 定时任务类型
 * 
 * @author Weifeng.Li
 *
 */
public enum TaskType implements IEnumState {

	USE_RECORD(0, "领用记录"),

	RETURN_RECORD(1, "归还记录"),

	STOCK_RECORD(2, "库存明细"),

	FEED_RECORD(3, "补料记录"),

	LOWER_RECORD(4, "下架记录"),

	USE_SUMMARY(5, "领用汇总"),

	FEED_SUMMARY(6, "补料汇总"),

	INVENTORY_RECORD(7, "盘点记录");

	private Integer code;
	private String message;

	private TaskType(Integer code, String message) {
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
			for (OnOrOff state : OnOrOff.values()) {
				if (state.getCode().equals(code)) {
					return state.getMessage();
				}
			}
			return null;
		}
	}

	@Override
	public String valueOfName(String name) {
		return TaskType.valueOf(name).getMessage();
	}

}
