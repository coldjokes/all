package com.dosth.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;

/**
 * 渲染工具类
 * 
 * @author guozhidong
 *
 */
public class RenderUtil {
	/**
	 * 渲染json对象
	 */
	public static void renderJson(HttpServletResponse response, Object jsonObject) {
		try {
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.write(JSON.toJSONString(jsonObject));
		} catch (IOException e) {
			throw new DoSthException(DoSthExceptionEnum.WRITE_ERROR);
		}
	}
}