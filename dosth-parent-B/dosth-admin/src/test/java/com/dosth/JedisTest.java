package com.dosth;

import java.util.Iterator;
import java.util.Set;

import com.dosth.common.cache.TokenManager;
import com.dosth.common.shiro.ShiroAccount;

import redis.clients.jedis.Jedis;

public class JedisTest {

	public static void main(String[] args) {
		String token =  "7b0d9004-cdcc-41e9-aed1-539e3570effa";
		Jedis jedis = new Jedis("localhost", 6379);
		jedis.auth("123");
		try {
			jedis.connect();
//			jedis.flushAll();
			jedis.set((TokenManager.TOKEN + token).getBytes(), new ShiroAccount().toString().getBytes());
			jedis.set(token.getBytes(), new ShiroAccount().toString().getBytes());
			Set<String> set = jedis.keys("*");
			Iterator<String> items = set.iterator();
			while (items.hasNext()) {
				String key = items.next();
				System.out.println(key);
			}
			Object obj = jedis.get("\\r \\t $d9ae540c-3173-49ec-9eb5-176e14757dda");
			System.out.println(obj);
			jedis.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
}