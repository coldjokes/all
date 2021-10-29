package com.dosth.common.cache;

import java.util.Timer;
import java.util.TimerTask;

/**
 * token管理器
 * 
 * @author guozhidong
 *
 */
public abstract class TokenManager {
	public static final String TOKEN = "dosth-shiro";

	// 令牌有效期,单位为秒,默认30分钟
	protected int tokenTimeout;

	public void setTokenTimeout(int tokenTimeout) {
		this.tokenTimeout = tokenTimeout;
	}

	private final Timer timer = new Timer(true);

	// 每分钟执行一次
	public TokenManager() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				verifyExpired();
			}
		}, 60 * 1000, 60 * 1000);
	}

	/**
	 * 验证失效token
	 */
	public abstract void verifyExpired();

	/**
	 * 信息存入
	 * 
	 * @param token
	 * @param object
	 */
	public abstract void add(String token, Object object);

	/**
	 * 获取并有效则延长session生命周期
	 * 
	 * @param token
	 * @return
	 */
	public abstract Object get(String token);

	/**
	 * 移除
	 * 
	 * @param token
	 */
	public abstract void remove(String token);
}