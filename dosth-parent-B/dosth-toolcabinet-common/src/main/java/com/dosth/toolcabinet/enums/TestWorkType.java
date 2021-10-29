package com.dosth.toolcabinet.enums;

/**
 * @description 测试工作枚举
 * @author guozhidong
 *
 */
public enum TestWorkType {
	IC(1, "IC读写器", 1), PRINT(2, "打印机", 2), SCAN(3, "扫描器", 3), ZCG(4, "暂存柜", 4);

	private int code;
	private String desc;
	private int resVal;

	private TestWorkType(int code, String desc, int resVal) {
		this.code = code;
		this.desc = desc;
		this.resVal = resVal;
	}

	public int getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}

	public int getResVal() {
		return this.resVal;
	}
}