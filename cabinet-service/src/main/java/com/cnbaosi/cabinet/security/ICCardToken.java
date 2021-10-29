package com.cnbaosi.cabinet.security;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * ic卡 token
 * 
 * @author Yifeng Wang  
 */

public class ICCardToken implements AuthenticationToken{
	
	private static final long serialVersionUID = 1L;

	private String icCard;
	
	public ICCardToken(String icCard){
		this.icCard = icCard;
	}
	
	@Override
	public Object getPrincipal() {
		return icCard;
	}

	@Override
	public Object getCredentials() {
		return icCard;
	}

	public String getIcCard() {
		return icCard;
	}

	public void setIcCard(String icCard) {
		this.icCard = icCard;
	}
}

