package com.dosth.admin.common.shiro;

import java.util.List;

import org.apache.shiro.authc.SimpleAuthenticationInfo;

import com.dosth.admin.entity.Account;
import com.dosth.common.shiro.ShiroAccount;

/**
 * 定义shirorealm所需数据的接口
 * 
 * @author guozhidong
 *
 */
public interface IShiro {
	/**
	 * 根据登录名获取登录用户
	 */
	Account account(String loginName);

	/**
	 * 根据系统账户获取Shiro账户
	 */
	ShiroAccount shiroAccount(Account account);

	/**
	 * 根据角色Id获取权限列表
	 */
	List<String> findPermissionsByRoleId(String roleId);

	/**
	 * 根据角色Id获取角色名称
	 */
	String findRoleNameByRoleId(String roleId);

	/**
	 * 获取shiro的认证信息
	 */
	SimpleAuthenticationInfo info(ShiroAccount shiroAccount, Account account, String realmName);
}