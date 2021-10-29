package com.dosth.toolcabinet.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @description 权限过滤器
 * @author guozhidong
 *
 */
@Component
@WebFilter(urlPatterns = {"/*"}, filterName = "authFilter")
public class AuthFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpSession session = request.getSession();
		String requestPath = request.getServletPath();
		logger.info("请求路径~~" + requestPath);
		// 设置session过去标识
		if (request.getSession().getAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG) == null) {
			request.getSession().setAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG, "Y");
		}
		// 设置session永久有效
		request.getSession(false).setMaxInactiveInterval(-1);	
		if (requestPath.endsWith("faceLogin") || requestPath.endsWith("login") || requestPath.endsWith("logout") || requestPath.endsWith("checkUser")
				|| requestPath.indexOf("static") != -1 || requestPath.indexOf("modbus") != -1
				|| requestPath.indexOf("Modbus") != -1 || requestPath.indexOf("getAccountInfo") != -1|| requestPath.indexOf("shutdown") != -1 || requestPath.indexOf("app") != -1
				|| requestPath.indexOf("checkDoorStatus") != -1 || requestPath.indexOf("checkSession") != -1) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			// 单个领料、购物车领料、前台补料、IC绑定、脸谱绑定(永不失效)
			if (requestPath.indexOf("startBorr") != -1 || requestPath.indexOf("sendCartToServer") != -1 || requestPath.indexOf("initFeed") != -1 || requestPath.indexOf("icBind") != -1
					|| requestPath.indexOf("faceBind") != -1) {
				request.getSession().setAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG, "N");
			}
			if (session.getAttribute("accountInfo") != null || "N".equals(String.valueOf(request.getAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG)))) { 
				chain.doFilter(servletRequest, servletResponse);
			} else {
				try {
					PrintWriter writer = servletResponse.getWriter();
					writer.write("<script type='text/javascript'>"
							+"function getTopWinow(){"
							+"    var p = window;"
							+"    while(p != p.parent){"
							+"        p = p.parent;"
							+"    }"
							+"    return p;"
							+"}"
							+ " var top = getTopWinow();"
							+"top.location.href='/login';"
							+ "</script>");
					writer.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void destroy() {
	}
}