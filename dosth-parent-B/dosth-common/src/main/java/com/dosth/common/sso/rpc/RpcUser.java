package com.dosth.common.sso.rpc;

import java.io.Serializable;

/**
 * RPC回传用户对象
 */
@SuppressWarnings("serial")
public class RpcUser implements Serializable {

	// 登录名
	private String account;

	public RpcUser(String account) {
		super();
		this.account = account;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}