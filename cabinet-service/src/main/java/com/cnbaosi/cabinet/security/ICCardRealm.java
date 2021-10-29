package com.cnbaosi.cabinet.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.cnbaosi.cabinet.entity.enums.StatusEnum;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.serivce.UserService;

/**
 *  shiro 自定义 realm
 * 
 * @author Yifeng Wang  
 */
public class ICCardRealm extends AuthorizingRealm {

    @Autowired
    private UserService userSvc;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ICCardToken;
    }
    
    /**
	 * 登录认证
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
    	ICCardToken upToken = (ICCardToken) token;
        String icCard = upToken.getIcCard();
        
        User dbUser = userSvc.getUserByIcCard(icCard);
        if (dbUser != null) {
        	
        	Integer status = dbUser.getStatus();
        	if(status == StatusEnum.OFF.getCode()) {
        		throw new DisabledAccountException();
        	}
        	
            return new SimpleAuthenticationInfo(dbUser, dbUser.getIcCard(), this.getName());
        } else {
			throw new UnknownAccountException();
		}
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo();
    }
}
