package com.dosth.common.util;

import java.util.List;

/**
 * sql语句工具类
 * 
 * @author guozhidong
 *
 */
public class SqlUtil {

	/**
	 * 根据参数个数输出对应的?
	 * 
	 * @param list
	 * @return
	 * @author guozhidong
	 * @date 2017年12月15日
	 */
	public static String parse(List<?> list) {
		StringBuilder str = new StringBuilder();
		if (list != null && list.size() > 0) {
			str.append("?");
			for (int i = 1; i < list.size(); i++) {
				str.append(",?");
			}
		}
		return str.toString();
	}
}