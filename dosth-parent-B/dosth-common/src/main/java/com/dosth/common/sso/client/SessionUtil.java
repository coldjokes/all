package com.dosth.common.sso.client;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.WebUtils;

import com.dosth.common.shiro.ShiroAccount;

/**
 * 当前已登录用户Session工具
 * 
 * @author guozhidong
 *
 */
public class SessionUtil {
	/**
	 * 用户信息
	 */
	public static final String SESSION_USER = "_sessionUser";

	/**
	 * 用户权限
	 */
	public static final String SESSION_USER_PERMISSION = "_sessionUserPermission";

	public static ShiroAccount getShiroAccount(HttpServletRequest request) {
		return (ShiroAccount) WebUtils.getSessionAttribute(request, SESSION_USER);
	}

	public static void setShiroAccount(HttpServletRequest request, ShiroAccount shiroAccount) {
		WebUtils.setSessionAttribute(request, SESSION_USER, shiroAccount);
	}

	public static SessionPermission getSessionPermission(HttpServletRequest request) {
		return (SessionPermission) WebUtils.getSessionAttribute(request, SESSION_USER_PERMISSION);
	}

	public static void setSessionPermission(HttpServletRequest request, SessionPermission sessionPermission) {
		WebUtils.setSessionAttribute(request, SESSION_USER_PERMISSION, sessionPermission);
	}
	
	public static void invalidate(HttpServletRequest request){
		setShiroAccount(request, null);
		setSessionPermission(request, null);
	}
}