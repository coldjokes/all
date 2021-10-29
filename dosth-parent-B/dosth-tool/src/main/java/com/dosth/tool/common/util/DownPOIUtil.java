package com.dosth.tool.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.dosth.tool.entity.EquDetailSta;

public class DownPOIUtil {
		 
	public static void downPoi(HttpServletResponse response,
			Map<EquDetailSta, String> map) throws Exception {
		String fname = "workbook" + getTimeStamp();// Excel文件名
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename="
				+ fname + ".xls"); // 设定输出文件头,该方法有两个参数，分别表示应答头的名字和值。
		response.setContentType("application/msexcel");
	}
 
	/**
	 * 该方法用来产生一个时间字符串（即：时间戳）
	 * @return
	 */
	public static String getTimeStamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:MM:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}