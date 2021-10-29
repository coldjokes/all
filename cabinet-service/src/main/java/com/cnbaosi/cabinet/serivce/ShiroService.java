package com.cnbaosi.cabinet.serivce;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.cnbaosi.cabinet.entity.modal.User;

/**
 * shiro相关方法
 * 
 * @author Yifeng Wang  
 */

public interface ShiroService {
	
	/**
	 * 获取当前Subject
	 */
	Subject getSubject();
	
	/**
	 * 从shiro获取session
	 */
	Session getSession();

	/**
	 * 获取shiro指定的sessionKey
	 */
	<T> T getSessionAttr(String key);

	/**
	 * 设置shiro指定的sessionKey
	 */
	void setSessionAttr(String key, Object value);

	/**
	 * 移除shiro指定的sessionKey
	 */
	void removeSessionAttr(String key);	
	/**
	 * 获取shiro中的user
	 * @return
	 */
	User getUser();
	
	/**
	 * 从session中获取机器id
	 * @return
	 */
	String getComputerId();
	
	/**
	 * 将机器id放入session
	 * @param identifyCode
	 */
	void setComputerId(String identifyCode);
}

