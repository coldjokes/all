package com.dosth.toolcabinet.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MiscUtil {

	//获取文件最后更新时间
	public static String getFileLastUpdate(String path) {
		String label = "未知";
		File f = new File("D:/resource/Toolcabinet.jar");
		if(f.exists()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd.HHmmss");
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(f.lastModified());
			label = sdf.format(cal.getTime());
		}
		return label;
	}
}
