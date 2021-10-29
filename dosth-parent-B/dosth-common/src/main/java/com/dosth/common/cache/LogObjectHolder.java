package com.dosth.common.cache;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.dosth.common.util.SpringContextHolder;

/**
 * 被修改的bean临时存放的地方
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Component
@Scope(scopeName = WebApplicationContext.SCOPE_SESSION)
public class LogObjectHolder implements Serializable {

	private Object object = null;

	public Object get() {
		return this.object;
	}

	public void set(Object object) {
		this.object = object;
	}

	public static LogObjectHolder me() {
		LogObjectHolder bean = SpringContextHolder.getBean(LogObjectHolder.class);
		return bean;
	}
}