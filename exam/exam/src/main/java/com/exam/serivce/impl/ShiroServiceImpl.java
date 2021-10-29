package com.exam.serivce.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.exam.entity.modal.User;
import com.exam.serivce.ShiroService;

/**
 * shiro方法实现
 * 
 * @author Yifeng Wang  
 */
@Service
public class ShiroServiceImpl implements ShiroService{
	
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
        return user;
    }
	
}

