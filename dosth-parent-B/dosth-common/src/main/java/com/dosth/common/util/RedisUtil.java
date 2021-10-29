package com.dosth.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis工具类
 * 
 * @author guozhidong
 *
 */
public class RedisUtil {

	private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	// 默认有效期,30分钟默认秒数
	private static final long EXPIRE_DEFAULT = 30 * 60;

	/**
	 * 创建Jedis连接工厂
	 * 
	 * @param host
	 * @param port
	 * @param password
	 * @param timeout
	 * @return
	 */
	public static JedisConnectionFactory createJedisConnectionFactory(String host, int port, String password,
			int timeout) {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(host);
		factory.setPort(port);
		factory.setPassword(password);
		factory.setTimeout(timeout);
		factory.afterPropertiesSet();
		return factory;
	}

	/**
	 * 创建缓存管理器
	 * 
	 * @param redisTemplate
	 *            redis模板
	 * @return 缓存管理器
	 */
	public static RedisCacheManager createRedisCacheManager(RedisTemplate<String, Serializable> redisTemplate,
			int timeout) {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(timeout);
		return cacheManager;
	}

	/**
	 * 创建redis模板
	 * 
	 * @param factory
	 *            redis连接工厂
	 * @return redis模板
	 */
	public static RedisTemplate<String, Object> createRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		// 设置key的序列化规则和value的序列化规则
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	/**
	 * 创建StringRedis模板
	 * 
	 * @param factory
	 *            redis连接工厂
	 * @return StringRedis模板
	 */
	public static StringRedisTemplate createStringRedisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(factory);
		return stringRedisTemplate;
	}

	/**
	 * 获取value对象并延长有效时间
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param expireTimes
	 *            延长有效时间;单位:秒,默认值:1800
	 * @return
	 * @throws CacheException
	 */
	public synchronized static Object get(RedisTemplate<String, Object> redisTemplate, String key, long... expireTimes) {
		Object value = null;
		try {
			long expireTime = EXPIRE_DEFAULT;
			if (expireTimes != null && expireTimes.length > 0) {
				expireTime = expireTimes[0];
			}
			value = redisTemplate.opsForValue().get(key);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Get key [{}] error: {}", new Object[] { key, e });
		}
		return value;
	}

	/**
	 * 修改redis key有效时间
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param expireTimes
	 *            延长有效时间;单位:秒,默认值:1800
	 */
	public synchronized static boolean expire(RedisTemplate<String, Object> redisTemplate, String key,
			long... expireTimes) {
		try {
			long expireTime = EXPIRE_DEFAULT;
			if (expireTimes != null && expireTimes.length > 0) {
				expireTime = expireTimes[0];
			}
			return redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Get key [{}] error: {}", new Object[] { key, e });
		}
		return false;
	}

	/**
	 * 获取value
	 * 
	 * @param redisTemplate
	 * @param key
	 * @return
	 * @throws CacheException
	 */
	public synchronized static Object get(RedisTemplate<String, Object> redisTemplate, String key) {
		Object value = null;
		try {
			value = redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error("Get key [{}] error: {}", new Object[] { key, e });
		}
		return value;
	}

	/**
	 * 设置value
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param value
	 * @param expireTimes
	 *            有效时间;单位:秒,默认值:1800
	 * @return
	 */
	public synchronized static Object put(RedisTemplate<String, Object> redisTemplate, String key, Object value,
			long... expireTimes) {
		Object v = null;
		try {
			long expireTime = EXPIRE_DEFAULT;
			if (expireTimes != null && expireTimes.length > 0) {
				expireTime = expireTimes[0];
			}
			v = get(redisTemplate, key);
			redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("Set key [{}] error: {}", new Object[] { key, e });
		}
		return v;
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @return
	 * @throws CacheException
	 */
	public synchronized static Object remove(RedisTemplate<String, Object> redisTemplate, String key) {
		Object value = null;
		try {
			value = get(redisTemplate, key);
			redisTemplate.delete(key);
		} catch (Exception e) {
			logger.error("Remove key [{}] error: {}", new Object[] { key, e });
		}
		return value;
	}

	/**
	 * 清除
	 */
	public synchronized static void clear(RedisTemplate<String, Object> redisTemplate) {
		try {
			redisTemplate.delete(keys(redisTemplate));
		} catch (Exception e) {
			logger.error("Clear error: {}", new Object[] { e });
		}
	}

	/**
	 * 匹配键值集合大小
	 * 
	 * @param redisTemplate
	 * @param patterns
	 *            匹配条件 默认所有
	 * @return
	 */
	public synchronized static int size(RedisTemplate<String, Object> redisTemplate, String... patterns) {
		return keys(redisTemplate).size();
	}

	/**
	 * 罗列匹配条件的键值集合
	 * 
	 * @param redisTemplate
	 * @param patterns
	 *            匹配条件 默认所有
	 * @return
	 */
	public synchronized static Set<String> keys(RedisTemplate<String, Object> redisTemplate, String... patterns) {
		String pattern = "*";
		if (patterns != null && patterns.length < 1) {
			pattern = patterns[0];
		}
		return redisTemplate.keys(pattern);
	}

	/**
	 * 匹配键值集合转换成list
	 * 
	 * @param redisTemplate
	 * @param patterns
	 *            匹配条件 默认所有
	 * @return
	 */
	public synchronized static Collection<Object> values(RedisTemplate<String, Object> redisTemplate,
			String... patterns) {
		Set<String> set = keys(redisTemplate, patterns);
		List<Object> list = new ArrayList<>();
		set.stream().forEach(s -> {
			list.add(get(redisTemplate, s));
		});
		return list;
	}
}