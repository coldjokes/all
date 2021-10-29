package com.dosth.admin;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.dosth.DosthApplicationTests;
import com.dosth.common.cache.TokenManager;
import com.dosth.common.shiro.ShiroAccount;

import redis.clients.jedis.Jedis;

public class RedisTemplateTest extends DosthApplicationTests {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private TokenManager tokenManager;
	

	ShiroAccount shiroAccount1;
	ShiroAccount shiroAccount2;
	
	@Before
	public void before() {
		System.out.println(this.redisTemplate);
		System.out.println(this.tokenManager);
		shiroAccount1 = new ShiroAccount();
		shiroAccount1.setId("10001");
		shiroAccount1.setDeptId("101");
		shiroAccount1.setLoginName("admin");
		shiroAccount1.setName("张三");
		shiroAccount1.setRoleList(new ArrayList<String>());
		shiroAccount1.setRoleNames(new ArrayList<String>());
		

		shiroAccount2 = new ShiroAccount();
		shiroAccount2.setId("10002");
		shiroAccount2.setDeptId("102");
		shiroAccount2.setLoginName("test");
		shiroAccount2.setName("李四");
		shiroAccount2.setRoleList(new ArrayList<String>());
		shiroAccount2.setRoleNames(new ArrayList<String>());
	}
	
	@Test
	public void test() {
		String token1 = "68bcbf08-e5a2-4cbe-a4b5-c5288a857d9f";
		this.redisTemplate.opsForValue().set(token1, shiroAccount1);
		ShiroAccount tmp1 = (ShiroAccount) this.redisTemplate.opsForValue().get(token1);
		System.out.println("^^^^^^^" + tmp1.getLoginName());
	}
	
	@Test
	public void test2() {
		String token2 = "78bcbf08-e5a2-4cbe-a4b5-c5288a857d9f";
		this.tokenManager.add(token2, shiroAccount2);
		ShiroAccount tmp2 = (ShiroAccount) this.tokenManager.get(token2);
		System.out.println("^^^^^^^" + tmp2.getLoginName());
	}
	
	@Test
	public void clear() {
		Jedis jedis = new Jedis("localhost", 6379);
		jedis.auth("123");
		try {
			jedis.connect();
			jedis.flushAll();
			jedis.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			jedis.close();
		}
	}
}
