package com.dosth.common.intercept;

import org.apache.shiro.session.InvalidSessionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.common.cache.TokenManager;
import com.dosth.common.controller.BaseController;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.support.HttpKit;
import com.dosth.common.util.CookieUtil;

/**
 * 验证session超时的拦截器
 * 
 * @author guozhidong
 *
 */
@Aspect
@Component
public class SessionTimeoutInterceptor extends BaseController {

	@Autowired
	private TokenManager tokenManager;
	
	@Pointcut("execution(* com.dosth.*.controller.*.*(..))")
	public void cutService() {
	}

	@Around("cutService()")
	public Object sessionTimeoutValidate(ProceedingJoinPoint point) throws Throwable {

		String servletPath = HttpKit.getRequest().getServletPath();

		if (servletPath.equals("/kaptcha") || servletPath.equals("/login")
				 || servletPath.equals("/picLogin") || servletPath.equals("/loginPic")
				|| servletPath.equals("/global/sessionError")
				|| servletPath.indexOf("/feign") != -1|| servletPath.indexOf("/app") != -1
				|| servletPath.indexOf("/thirdfeign") != -1) {
			return point.proceed();
		} else if ("/logout".equals(servletPath)) {
			String tokenId = CookieUtil.getCookie(super.getHttpServletRequest(), TokenManager.TOKEN);
			this.tokenManager.remove(tokenId);
			return point.proceed();
		}	else {
			String tokenId = CookieUtil.getCookie(super.getHttpServletRequest(), TokenManager.TOKEN);
	        Object account = (ShiroAccount) this.tokenManager.get(tokenId);
			if (account == null) {
//				ShiroKit.getSubject().logout();
				throw new InvalidSessionException();
			}
			return point.proceed();
		}
	}
}