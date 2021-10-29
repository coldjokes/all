package com.dosth.admin.common.shiro.check;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.common.listener.ConfigListener;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.support.CollectionKit;
import com.dosth.common.support.HttpKit;
import com.dosth.common.util.SpringContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限自定义检查
 */
@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class PermissionCheckFactory implements ICheck {

	public static ICheck me() {
		return SpringContextHolder.getBean(ICheck.class);
	}

	@Override
	public boolean check(Object[] permissions) {
		ShiroAccount account = ShiroKit.getAccount();
		if (null == account) {
			return false;
		}
		String join = CollectionKit.join(permissions, ",");
		if (ShiroKit.hasAnyRoles(join)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean checkAll() {
		HttpServletRequest request = HttpKit.getRequest();
		ShiroAccount account = ShiroKit.getAccount();
		if (null == account) {
			return false;
		}
		String requestURI = request.getRequestURI().replaceFirst(ConfigListener.getConf().get("contextPath"), "");
		String[] str = requestURI.split("/");
		if (str.length >= 3) {
			requestURI = "/" + str[1] + "/" + str[2];
		}
		if (ShiroKit.hasPermission(requestURI)) {
			return true;
		}
		return false;
	}
}