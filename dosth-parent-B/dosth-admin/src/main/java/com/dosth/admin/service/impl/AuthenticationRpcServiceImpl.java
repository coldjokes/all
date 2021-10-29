package com.dosth.admin.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dosth.common.cache.TokenManager;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.sso.rpc.AuthenticationRpcService;
import com.dosth.common.sso.rpc.RpcPermission;
import com.dosth.common.sso.rpc.RpcUser;
import com.dosth.common.util.StringUtil;

/**
 * 身份认证授权服务接口实现
 * 
 * @author guozhidong
 */
@Service
public class AuthenticationRpcServiceImpl implements AuthenticationRpcService {

	@Resource
	private TokenManager tokenManager;

	@Override
	public boolean validate(String token) {
		return this.tokenManager.get(token) != null;
	}

	@Override
	public RpcUser findAuthInfo(String token) {
		ShiroAccount shiroAccount = (ShiroAccount) this.tokenManager.get(token);
		if (shiroAccount != null) {
			return new RpcUser(shiroAccount.getLoginName());
		}
		return null;
	}

	@Override
	public List<RpcPermission> findPermissionList(String token, String appCode) {
		if (StringUtil.isBlank(token)) {
			return new ArrayList<>(); // .permissionService.findListById(appCode, null);
		} else {
			ShiroAccount shiroAccount = (ShiroAccount) this.tokenManager.get(token);
			if (shiroAccount != null) {
				return new ArrayList<>(); // permissionService.findListById(appCode, shiroAccount.getId());
			} else {
				return new ArrayList<RpcPermission>(0);
			}
		}
	}
}