package com.dosth.toolcabinet.util;

import javax.servlet.http.HttpServletRequest;

import com.dosth.toolcabinet.dto.AccountUserInfo;

/**
 * @description 基础Controller
 * @author guozhidong
 *
 */
public class BaseController {
	/**
	 * @description 获取权限帐户用户信息
	 * @param request
	 * @return
	 */
	protected AccountUserInfo getAccountInfo(HttpServletRequest request) {
		AccountUserInfo accountInfo = (AccountUserInfo) request.getSession().getAttribute("accountInfo");
		return accountInfo;
	}
}