package com.cnbaosi.cabinet.serivce.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.cabinet.entity.modal.Computer;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.serivce.ComputerService;
import com.cnbaosi.cabinet.serivce.ShiroService;

/**
 * shiro方法实现
 * 
 * @author Yifeng Wang  
 */
@Service
public class ShiroServiceImpl implements ShiroService{

	private static final String COMPUTER = "computerId";
	
	@Autowired
	private ComputerService computerSvc;
	
	@Override	
	public Subject getSubject() {
		return SecurityUtils.getSubject();
	}
	
	@Override	
	public Session getSession() {
		return getSubject().getSession();
	}

	@Override	
	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(String key) {
		Session session = getSession();
		return session != null ? (T) session.getAttribute(key) : null;
	}

	@Override	
	public void setSessionAttr(String key, Object value) {
		Session session = getSession();
		session.setAttribute(key, value);
	}

	@Override
	public void removeSessionAttr(String key) {
		Session session = getSession();
		if (session != null) {
			session.removeAttribute(key);
		}
	}
	
    @Override
    public User getUser() {
        Subject currentUser = SecurityUtils.getSubject();
        User user = (User)currentUser.getPrincipal();
//        if(user == null) { //TODO bug 如果不登陆，默认给一个用户
//        	user = new User();
//        	user.setRole(0);
//        } else {
//        }
        return user;
    }

	@Override
	public String getComputerId() {
		return getSessionAttr(COMPUTER);
	}

	@Override
	public void setComputerId(String identifyCode) {
		Computer com = computerSvc.getComputerByIdentifyCode(identifyCode);
		if(com != null) {
			setSessionAttr(COMPUTER, com.getId());
		}
	}
	
}

