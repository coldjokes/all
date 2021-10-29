package com.cnbaosi.cabinet.entity.enums;

/**
 * 数据来源
 * @author Yifeng Wang  
 */
public enum SourceEnum implements BaseEnum {
	
	SYSTEM(1, "系统新增") 
	,FILE(2, "文件导入")
//	,OUTER(3, "外部系统")
	;
	
	private Integer code;
    private String text;

    private SourceEnum(int code, String text) {
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
			for (SourceEnum source : SourceEnum.values()) {
				if (source.getCode().equals(code)) {
					return source.getText();
				}
			}
			return null;
		}
	}

	@Override
	public Integer getCodeByText(String text) {
		return SourceEnum.valueOf(text).getCode();
	}
}
