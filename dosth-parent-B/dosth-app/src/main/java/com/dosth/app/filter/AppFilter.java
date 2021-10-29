package com.dosth.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import com.dosth.app.cache.UserCache;
import com.dosth.app.dto.AppUser;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = { "/app/**"})
public class AppFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpSession session = request.getSession();
		AppUser user = UserCache.userMap.get(session.getId());
		String requestPath = request.getServletPath();
		if (user != null || requestPath.indexOf("swagger") != -1 || requestPath.indexOf("v2") != -1
				|| requestPath.indexOf("img") != -1) {
			chain.doFilter(servletRequest, servletResponse);
		} else {
			// 转发到登录页面
//			request.getRequestDispatcher("/app/login/admin/123").forward(servletRequest, servletResponse);
			
			String tokenId = request.getSession().getId();
			AppUser appUser = new AppUser("1", "admin", "阿诺事业部", "245", "http://192.168.2.46:8080/img/0.jpg");
			request.getSession().setAttribute("appUser", appUser);
			UserCache.userMap.put(tokenId, appUser);
			chain.doFilter(servletRequest, servletResponse);
		}
	}
}