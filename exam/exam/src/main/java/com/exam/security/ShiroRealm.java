package com.exam.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.exam.entity.enums.StatusEnum;
import com.exam.entity.modal.User;
import com.exam.serivce.UserService;

/**
 *  shiro 自定义 realm
 * 
 * @author Yifeng Wang  
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userSvc;
    
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    /**
	 * 登录认证
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
    	UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
        User dbUser = userSvc.getUserByName(username);
        if(dbUser != null) {
        	Integer status = dbUser.getStatus();

        	if(status == StatusEnum.OFF.getCode()) {
        		throw new DisabledAccountException();
        	}

        	return new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), this.getName());
		} else {
			throw new UnknownAccountException();
		}
    }

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	return new SimpleAuthorizationInfo();
	}
}
