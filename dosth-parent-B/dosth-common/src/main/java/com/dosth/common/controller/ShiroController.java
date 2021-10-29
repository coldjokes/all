package com.dosth.common.controller;

import org.apache.shiro.session.InvalidSessionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.common.cache.TokenManager;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.util.CookieUtil;

/**
 * ShiroController
 * 
 * @author guozhidong
 *
 */
public class ShiroController extends BaseController {
	@Autowired
	protected TokenManager tokenManager;

	/**
	 * 获取redis共享登录信息
	 * 
	 * @return
	 */
	protected ShiroAccount getShiroAccount() {
		String token = CookieUtil.getCookie(super.getHttpServletRequest(), TokenManager.TOKEN);
		Object account = this.tokenManager.get(token);
		if (account == null) {
			throw new InvalidSessionException();
		}
		return (ShiroAccount) account;
	}
}