package com.cnbaosi.cabinet.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * username卡 token
 * 
 * @author Yifeng Wang  
 */

public class UsernameToken implements AuthenticationToken{
	
	private static final long serialVersionUID = 1L;

	private String username;
	
	public UsernameToken(String username){
		this.username = username;
	}
	
	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public Object getCredentials() {
		return username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

