package com.dosth.toolcabinet.enums;

/**
 * 
 * @description 借出类型枚举类
 * @author guozhidong
 *
 */
public enum EnumBorrowType {
	GRID("0", "全部"), MATTYPE("1", "类型"), REQREF("2", "设备"), PROCREF("3", "工序"), PARTS("4", "零件"), CUSTOM("5", "自定义"),
	RETURN("6", "暂存/归还"), ADMIN("7", "管理员");
	/**
	 * code
	 */
	private String code;
	/**
	 * 描述
	 */
	private String desc;

	private EnumBorrowType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return this.code;
	}

	public String getDesc() {
		return this.desc;
	}
	
	public String valueOfName(String name) {
		return EnumBorrowType.valueOf(name).getCode();
	}
}