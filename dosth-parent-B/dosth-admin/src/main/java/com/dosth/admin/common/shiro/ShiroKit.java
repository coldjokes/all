package com.dosth.admin.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.util.ToolUtil;

/**
 * Shiro工具类
 * 
 * @author guozhidong
 *
 */
public class ShiroKit {

	private static final String NAMES_DELIMETER = ",";

	/**
	 * 加盐参数
	 */
	public final static String hashAlgorithmName = "MD5";

	/**
	 * 循环次数
	 */
	public final static int hashIterations = 1024;

	/**
	 * shiro密码加密工具
	 * 
	 * @param credentials
	 *            密码
	 * @param saltSource
	 *            密码盐
	 * @return 加密结果
	 */
	public static String md5(String credentials, String saltSource) {
		ByteSource salt = new Md5Hash(saltSource);
		return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
	}

	/**
	 * 获取随机盐
	 * 
	 * @param length
	 *            长度
	 * @return
	 */
	public static String getRandomSalt(int length) {
		return ToolUtil.getRandomString(5);
	}

	/**
	 * 获取当前Subject
	 * 
	 * @return
	 */
	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	/**
	 * 获取封装的ShiroAccount
	 * 
	 * @return
	 */
	public static ShiroAccount getAccount() {
		if (isGuest()) {
			return null;
		} else {
			return (ShiroAccount) getSubject().getPrincipals().getPrimaryPrincipal();
		}
	}

	/**
	 * 验证当前用户是否为"访客",即未认证(包含未记住)的用户
	 * 
	 * @return true:访客,否则 false
	 */
	public static boolean isGuest() {
		return !isUser();
	}

	/**
	 * 认证通过或已记住的用户,与guest搭配使用
	 * 
	 * @return true:用户,否则false
	 */
	public static boolean isUser() {
		return getSubject() != null && getSubject().getPrincipal() != null;
	}

	/**
	 * 从shiro获取session
	 */
	public static Session getSession() {
		return getSubject().getSession();
	}

	/**
	 * 获取shiro指定的sessionKey
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttr(String key) {
		Session session = getSession();
		return session != null ? (T) session.getAttribute(key) : null;
	}

	/**
	 * 设置shiro指定的sessionKey
	 */
	public static void setSessionAttr(String key, Object value) {
		Session session = getSession();
		session.setAttribute(key, value);
	}

	/**
	 * 移除shiro指定的sessionKey
	 */
	public static void removeSessionAttr(String key) {
		Session session = getSession();
		if (session != null) {
			session.removeAttribute(key);
		}
	}

	/**
	 * 验证当前用户是否属于该角色
	 * 
	 * @param roleName
	 *            角色名称
	 * @return 属于该角色:true,否则false
	 */
	public static boolean hasRole(String roleName) {
		return getSubject() != null && roleName != null && roleName.length() > 0 && getSubject().hasRole(roleName);
	}
	
	/**
	 * 与hasRole标签逻辑相反,当用户不属于该角色时验证通过
	 * 
	 * @param roleName 角色名称
	 * 
	 * @return 不属于该角色:true,否则false
	 */
	public static boolean lacksRole(String roleName) {
		return !hasRole(roleName);
	}
	
	/**
	 * 验证当前用户是否属于以下任意一个角色
	 * @param roleNames 角色列表
	 * @return 属于:true,否则false
	 */
	public static boolean hasAnyRoles(String roleNames) {
		boolean hasAnyRole = false;
		Subject subject = getSubject();
		if (subject != null && roleNames != null && roleNames.length() > 0) {
			for (String role : roleNames.split(NAMES_DELIMETER)) {
				if (subject.hasRole(role.trim()) ) {
					hasAnyRole = true;
					break;
				}
			}
		}
		return hasAnyRole;
	}
	
	/**
	 * 验证当前用户是否属于以下所有角色
	 * @param roleNames 角色列表
	 * @return 属于:true,否则false
	 */
	public static boolean hasAllRoles(String roleNames) {
		boolean hasAllRole = true;
		Subject subject = getSubject();
		if (subject != null && roleNames != null && roleNames.length() > 0) {
			for (String role : roleNames.split(NAMES_DELIMETER)) {
				if (!subject.hasRole(role.trim())) {
					hasAllRole = false;
					break;
				}
			}
		}
		return hasAllRole;
	}
	
	/**
	 * 验证当前用户是否拥有指定权限
	 * @param permission 权限名
	 * @return 拥有权限:true,否则false
	 */
	public static boolean hasPermission(String permission) {
		return getSubject() != null && permission != null
				&& permission.length() > 0 && getSubject().isPermitted(permission);
	}
	
	/**
	 * 验证用户是否没有指定权限
	 * @param permission 权限名
	 * @return 未拥有权限:true,否则false
	 */
	public static boolean lacksPermission(String permission) {
		return !hasPermission(permission);
	}
	
	/**
	 * 已认证通过的用户(与user标签的区别,不包含已记住的用户)
	 * @return
	 */
	public static boolean isAuthenticated() {
		return getSubject() != null && getSubject().isAuthenticated();
	}
	
	/**
	 * 未认证通过用户,与authenticted标签对应(与guest标签的区别,该标签包含已记住用户)
	 * @return 没有通过身份验证:true,否则false
	 */
	public static boolean noAuthenticated() {
		return !isAuthenticated();
	}
	
	/**
	 * 输出当前用户信息,通常为登录账号信息
	 * @return 当前用户信息
	 */
	public static String principal() {
		if (getSubject() != null) {
			Object principal = getSubject().getPrincipal();
			return principal.toString();
		}
		return "";
	}
}