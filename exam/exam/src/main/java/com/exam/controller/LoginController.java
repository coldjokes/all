package com.exam.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.entity.RestFulResponse;
import com.exam.entity.criteria.LoginCriteria;
import com.exam.entity.modal.User;
import com.exam.serivce.UserService;
import com.google.common.collect.Lists;

/**
 *  登录
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/login")
public class LoginController extends BaseController{

	@Autowired
	private UserService userSvc;
	
	@PostMapping
    public RestFulResponse<User> login(@RequestBody LoginCriteria loginCriteria) {
		
		String username = loginCriteria.getUsername();
		String password = loginCriteria.getPassword();
		
		AuthenticationToken token = new UsernamePasswordToken(username, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
		} catch ( UnknownAccountException e ) { 
			e.printStackTrace();
			return error(400, "未找到该用户！");
		} catch ( IncorrectCredentialsException e ) {
			e.printStackTrace();
			return error(400, "密码与账号不匹配！");
		} catch ( LockedAccountException e ) { 
			e.printStackTrace();
			return error(400, "权限不足，无法完成登录！");
		} catch ( ExcessiveAttemptsException e ) {
			e.printStackTrace();
			return error(400, "请求次数过多，用户被锁定！");
		} catch ( DisabledAccountException e ) {
			e.printStackTrace();
			return error(400, "账号已被禁用，无法登录！");
		} catch ( AuthenticationException e ) {
			e.printStackTrace();
			return error(400, "未知错误，无法完成登录！");
		}
        
        User user = userSvc.getUserByName(username);
        return success(1, Lists.newArrayList(user), "登录成功");
    }

    @DeleteMapping
    public RestFulResponse<String> logout() {
        Subject currentUser = SecurityUtils.getSubject();
        if (currentUser.isAuthenticated()) {
            currentUser.logout();
        }
        return success("登出成功");
    }
}

