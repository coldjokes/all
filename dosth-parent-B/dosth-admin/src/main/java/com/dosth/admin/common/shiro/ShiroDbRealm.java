package com.dosth.admin.common.shiro;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.dosth.admin.common.shiro.factory.ShiroFactory;
import com.dosth.admin.entity.Account;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.util.ToolUtil;

public class ShiroDbRealm extends AuthorizingRealm {

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		IShiro shiroFactory = ShiroFactory.me();
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		Account account = shiroFactory.account(token.getUsername());
		ShiroAccount shiroAccount = shiroFactory.shiroAccount(account);
		SimpleAuthenticationInfo info = shiroFactory.info(shiroAccount, account, super.getName());
		return info;
	}

	/**
	 * 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		IShiro shiroFactory = ShiroFactory.me();
		ShiroAccount shiroAccount = (ShiroAccount) principals.getPrimaryPrincipal();
		List<String> roleList = shiroAccount.getRoleList();
		
		Set<String> permissionSet = new HashSet<>();
		Set<String> roleNameSet = new HashSet<>();
		List<String> permissions = null;
		String roleName = null;
		for (String roleId : roleList) {
			permissions = shiroFactory.findPermissionsByRoleId(roleId);
			if (permissions != null) {
				for (String permission : permissions) {
					if (ToolUtil.isNotEmpty(permission)) {
						permissionSet.add(permission);
					}
				}
			}
			roleName = shiroFactory.findRoleNameByRoleId(roleId);
			roleNameSet.add(roleName);
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(permissionSet);
		info.addRoles(roleNameSet);
		return info;
	}

	/**
	 * 设置认证加密方式
	 */
	@Override
	public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
		HashedCredentialsMatcher md5CredentialsMatcher = new HashedCredentialsMatcher();
		md5CredentialsMatcher.setHashAlgorithmName(ShiroKit.hashAlgorithmName);
		md5CredentialsMatcher.setHashIterations(ShiroKit.hashIterations);
		super.setCredentialsMatcher(md5CredentialsMatcher);
	}
}