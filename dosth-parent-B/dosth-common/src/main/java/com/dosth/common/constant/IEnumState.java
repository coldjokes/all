package com.dosth.common.constant;

/**
 * 枚举类抽象接口
 * 
 * @author guozhidong
 *
 */
public interface IEnumState {
	/**
	 * 获取编码
	 * 
	 * @return
	 */
	public Integer getCode();

	/**
	 * 获取消息
	 * 
	 * @return
	 */
	public String getMessage();

	/**
	 * 通过Code获取枚举名称
	 * 
	 * @param code
	 * @return
	 */
	public String valueOfCode(Integer code);
	
	/**
	 * 通过name获取枚举名称
	 * @param name
	 * @return
	 */
	public String valueOfName(String name);
}