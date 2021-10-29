package com.dosth.common.cache.token;

import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.common.cache.TokenManager;

/**
 * 单实例环境令牌管理
 * 
 * @author guozhidong
 *
 */
public class LocalTokenManager extends TokenManager {

	private static final Logger logger = LoggerFactory.getLogger(LocalTokenManager.class);

	// 令牌存储结构
	private final ConcurrentHashMap<String, DummyUser> tokenMap = new ConcurrentHashMap<>();

	@Override
	public void verifyExpired() {
		Date now = new Date();
		String token;
		DummyUser dummyUser;
		for (Entry<String, DummyUser> entry: tokenMap.entrySet()) {
			token = entry.getKey();
			dummyUser = entry.getValue();
			// 当前时间大于过期时间
			if (now.compareTo(dummyUser.expired) > 0) {
				// 已过期,清除对应token
				tokenMap.remove(token);
				logger.debug("token:" + token + "已失效");
			}
		}
	}

	@Override
	public void add(String token, Object object) {
		DummyUser obj = new DummyUser();
		obj.object = object;
		this.extendExpiredTime(obj);
		tokenMap.putIfAbsent(token, obj);
	}

	@Override
	public Object get(String token) {
		DummyUser dummyUser = tokenMap.get(token);
		if (dummyUser == null) {
			return null;
		}
		this.extendExpiredTime(dummyUser);
		return dummyUser.object;
	}

	@Override
	public void remove(String token) {
		tokenMap.remove(token);
	}

	/**
	 * 扩展过期时间
	 * @param dummyUser
	 */
	private void extendExpiredTime(DummyUser dummyUser) {
		dummyUser.expired = new Date(new Date().getTime() + tokenTimeout * 1000);
	}

	/**
	 * 复合结构体
	 * 
	 * @DESC 包含loginUser与过期时间expried两个成员
	 *
	 */
	private class DummyUser {
		private Object object;
		private Date expired; // 过期时间
	}
}