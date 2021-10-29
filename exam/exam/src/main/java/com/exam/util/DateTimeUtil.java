package com.exam.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author Yifeng Wang  
 */

public class DateTimeUtil {

	public static String FMT_YMDHMS = "yyyyMMddHHmmss";
	public static String FMT_YMDHMS2 = "yyyy-MM-dd HH:mm:ss";
	public static String FMT_YMDHM = "yyyy-MM-dd HH:mm";
	public static String FMT_YMDHMSSSS = "yyyyMMddHHmmssSSS"; // 精确到毫秒数
	public static String FMT_YMDH = "yyyyMMddHH";
	public static String FMT_YMD = "yyyyMMdd";
	public static String FMT_YMD2 = "yyyy-MM-dd";
	public static String FMT_YYYYMM = "yyyyMM";
	public static String FMT_YYYY = "yyyy";
	public static String FMT_MM = "MM";
	
	public static Date now() {
		return new Date();
	}
	
    public static String format(String fmt) {
        return format(fmt, new Date());
    }

    public static String format(String fmt, Date date) {
        return new SimpleDateFormat(fmt).format(date);
    }
    
    
    
    //获取今天的开始时间
    public static Date getStartOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    //获取今天的结束时间
    public static Date getEndOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    //获取上一年今天的开始时间
    public static Date getBeforeYearOfDayStart(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.YEAR, -1);
        return c.getTime();
    }

    //获取上一年今天的结束时间
    public static Date getBeforeYearOfDayEnd(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        c.add(Calendar.YEAR, -1);
        return c.getTime();
    }
}

