package com.exam.entity.enums;

/**
 *   日志枚举
 * 
 * @author Yifeng Wang  
 */
public enum RoleEnum implements BaseEnum {
	
	 SUPERADMIN(1, "超级管理员"), 
	 ADMIN(2, "管理员"),
	 STAFF(3, "操作员");
	
	private Integer code;
    private String text;

    private RoleEnum(int code, String text) {
 		this.code = code;
 		this.text = text;
 	}

	@Override
	public Integer getCode() {
		return this.code;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public String getTextByCode(Integer code) {
		if (code == null) {
			return null;
		} else {
			for (RoleEnum role : RoleEnum.values()) {
				if (role.getCode().equals(code)) {
					return role.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return RoleEnum.valueOf(text).getCode();
	}
}
