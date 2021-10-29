package com.dosth.common.cache.token;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.data.redis.core.RedisTemplate;

import com.dosth.common.cache.TokenManager;
import com.dosth.common.util.RedisUtil;

/**
 * 分布式环境令牌管理
 * 
 * @author guozhidong
 *
 */
public class RedisTokenManager extends TokenManager {
	/**
	 * 是否需要扩展token过期时间
	 */
	private Set<String> tokenSet = new CopyOnWriteArraySet<String>();

	private RedisTemplate<String, Object> redisTemplate;

	public RedisTokenManager(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void verifyExpired() {
		tokenSet.clear();
	}

	@Override
	public void add(String token, Object object) {
		String key = TokenManager.TOKEN + token; 
		RedisUtil.put(this.redisTemplate, key, object, tokenTimeout);
	}

	@Override
	public Object get(String token) {
		String key = TokenManager.TOKEN + token; 
		Object object = RedisUtil.get(this.redisTemplate, key);
		if (object != null && !tokenSet.contains(key)) {
			tokenSet.add(key);
			RedisUtil.expire(redisTemplate, key, tokenTimeout);
		}
		return object;
	}

	@Override
	public void remove(String token) {
		String key = TokenManager.TOKEN + token; 
		RedisUtil.remove(this.redisTemplate, key);
	}
}