package com.dosth.admin.aop;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.admin.common.exception.BizExceptionEnum;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.common.exception.InvalidKaptchaException;
import com.dosth.admin.common.log.LogTaskFactory;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.common.base.tips.ErrorTip;
import com.dosth.common.log.LogManager;
import com.dosth.common.support.HttpKit;

import java.lang.reflect.UndeclaredThrowableException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.UnknownSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 * 
 * @author guozhidong
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private DosthProperties dosthProperties;

	/**
	 * 拦截业务异常
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorTip notFount(BusinessException e) {
		LogManager.me().executeLog(LogTaskFactory.exceptionLog(ShiroKit.getAccount().getId(), e));
		HttpKit.getRequest().setAttribute("tip", e.getMessage());
		logger.error("业务异常:", e);
		return new ErrorTip(e.getCode(), e.getMessage());
	}

	/**
	 * 用户未登录
	 */
	@ExceptionHandler(AuthenticationException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String unAuth(AuthenticationException e) {
		logger.error("用户未登陆：", e);
		return "/login.html";
	}

	/**
	 * 账号被冻结
	 */
	@ExceptionHandler(DisabledAccountException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String accountLocked(DisabledAccountException e, Model model) {
		String loginName = HttpKit.getRequest().getParameter("loginName");
		LogManager.me().executeLog(LogTaskFactory.loginLog(loginName, "账号被冻结", HttpKit.getIp()));
		model.addAttribute("tips", "账号被冻结");
		return "/login.html";
	}

	/**
	 * 账号密码错误
	 */
	@ExceptionHandler(CredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String credentials(CredentialsException e, Model model) {
		String loginName = HttpKit.getRequest().getParameter("loginName");
		LogManager.me().executeLog(LogTaskFactory.loginLog(loginName, "账号密码错误", HttpKit.getIp()));
		model.addAttribute("logo", this.dosthProperties.getLogo());
		model.addAttribute("tips", "账号密码错误");
		return "/login.html";
	}

	/**
	 * 验证码错误
	 */
	@ExceptionHandler(InvalidKaptchaException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String credentials(InvalidKaptchaException e, Model model) {
		String loginName = HttpKit.getRequest().getParameter("loginName");
		LogManager.me().executeLog(LogTaskFactory.loginLog(loginName, "验证码错误", HttpKit.getIp()));
		model.addAttribute("tips", "验证码错误");
		return "/login.html";
	}

	/**
	 * 无权访问该资源
	 */
	@ExceptionHandler(UndeclaredThrowableException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorTip credentials(UndeclaredThrowableException e) {
		HttpKit.getRequest().setAttribute("tip", "权限异常");
		logger.error("权限异常!", e);
		return new ErrorTip(BizExceptionEnum.NO_PERMITION.getCode(), BizExceptionEnum.NO_PERMITION.getMessage());
	}

	/**
	 * 拦截未知的运行时异常
	 */
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorTip notFount(RuntimeException e) {
		try {
			LogManager.me().executeLog(LogTaskFactory.exceptionLog(ShiroKit.getAccount().getId(), e));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		HttpKit.getRequest().setAttribute("tip", "服务器未知运行时异常");
		logger.error("运行时异常:", e);
		return new ErrorTip(BizExceptionEnum.SERVER_ERROR.getCode(), BizExceptionEnum.SERVER_ERROR.getMessage());
	}

	/**
	 * session失效的异常拦截
	 */
	@ExceptionHandler(InvalidSessionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String sessionTimeout(InvalidSessionException e, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		model.addAttribute("tips", "session超时");
		model.addAttribute("logo", this.dosthProperties.getLogo());
		logger.info("session超时");
		this.assertAjax(request, response);
		return "/login.html";
	}

	/**
	 * session异常
	 */
	@ExceptionHandler(UnknownSessionException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String sessionTimeout(UnknownSessionException e, Model model, HttpServletRequest request,
			HttpServletResponse response) {
		model.addAttribute("tips", "session异常");
		model.addAttribute("logo", this.dosthProperties.getLogo());
		logger.info("session异常");
		this.assertAjax(request, response);
		return "/login.html";
	}

	private void assertAjax(HttpServletRequest request, HttpServletResponse response) {
		if (request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
			// 如果是ajax请求响应头会有，x-requested-with
			response.setHeader("sessionstatus", "timeout");// 在响应头设置session状态
		}
	}
}