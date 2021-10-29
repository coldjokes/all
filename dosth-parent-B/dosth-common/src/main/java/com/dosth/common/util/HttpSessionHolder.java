package com.dosth.common.util;

import javax.servlet.http.HttpSession;

/**
 * 非Controller中获取当前session工具类
 * 
 * @author guozhidong
 *
 */
public class HttpSessionHolder {

	private static ThreadLocal<HttpSession> tl = new ThreadLocal<>();

	public static void put(HttpSession session) {
		tl.set(session);
	}

	public static HttpSession get() {
		return tl.get();
	}

	public static void remove() {
		tl.remove();
	}
}