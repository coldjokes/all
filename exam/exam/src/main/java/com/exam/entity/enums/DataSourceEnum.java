package com.exam.entity.enums;

/**
 * 数据库来源
 * @author Yifeng Wang  
 */
public enum DataSourceEnum {
	DEFAULT("default"), //默认数据库
	OUTER("OUTER"); // 外部数据库

    private String value;

    DataSourceEnum(String value){
    	this.value = value;
	}

    public String getValue() {
        return value;
    }
}
