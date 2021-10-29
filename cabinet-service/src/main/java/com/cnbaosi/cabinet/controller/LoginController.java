package com.cnbaosi.cabinet.controller;

import org.apache.commons.lang3.StringUtils;
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

import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.LoginCriteria;
import com.cnbaosi.cabinet.entity.modal.FaceFileBean;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.security.ICCardToken;
import com.cnbaosi.cabinet.security.UsernameToken;
import com.cnbaosi.cabinet.serivce.ShiroService;
import com.cnbaosi.cabinet.serivce.UserService;
import com.github.pagehelper.util.StringUtil;
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
	private ShiroService shiroSvc;
	@Autowired
	private UserService userSvc;
	
	@PostMapping
    public RestFulResponse<User> login(@RequestBody LoginCriteria loginCriteria) {
		
		String username = loginCriteria.getUsername();
		String password = loginCriteria.getPassword();
		String icCard = loginCriteria.getIcCard();
		boolean isApp = loginCriteria.getIsApp();
		
		AuthenticationToken token = null;
		//app登录后，记录柜体id
		if(isApp) {
			shiroSvc.setComputerId(loginCriteria.getIdentifyCode());
		}
		if(StringUtils.isBlank(icCard)){
			token = new UsernamePasswordToken(username, password);
		} else {
			token = new ICCardToken(icCard);
		}
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
        
        User user;
        if(StringUtil.isNotEmpty(icCard)) {
        	user = userSvc.getUserByIcCard(icCard);
        }else {
        	user = userSvc.getUserByName(username);
        }
        
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
    
    /**
     * 人脸注册
     */
    @PostMapping("/face/register")
    public RestFulResponse<String> faceRegister(@RequestBody FaceFileBean faceBean) {
    	boolean bindResult = userSvc.faceRegister(faceBean);
    	return actionResult(bindResult);
    }

    /**
     * 人脸登陆
     */
    @PostMapping("/face/login")
    public RestFulResponse<User> faceLogin(@RequestBody FaceFileBean faceBean) {
    	User user = userSvc.faceLogin(faceBean);
    	if(user != null) {
    		AuthenticationToken token = new UsernameToken(user.getUsername());
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
    		return success(1, Lists.newArrayList(user), "登录成功");
    	} else {
    		return error(1, "登录失败");
    	}
    }
    
}

