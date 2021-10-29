package com.exam.entity.enums;

/**
 * 
 * 
 * @author Yifeng Wang  
 */

public interface BaseEnum {
	/**
	 * 获取编码
	 * 
	 * @return
	 */
	Integer getCode();

	/**
	 * 获取消息
	 * 
	 * @return
	 */
	String getText();

	/**
	 * 通过Code获取枚举名称
	 * 
	 * @param code
	 * @return
	 */
	String getTextByCode(Integer code);
	
	/**
	 * 通过name获取枚举名称
	 * @param name
	 * @return
	 */
	Integer getCodeByText(String text);
}

