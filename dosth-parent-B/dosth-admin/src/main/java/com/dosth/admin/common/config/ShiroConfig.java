package com.dosth.admin.common.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.realm.CachingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.admin.common.shiro.ShiroDbRealm;
import com.dosth.common.cache.TokenManager;
import com.dosth.common.cache.token.RedisTokenManager;
import com.dosth.common.util.RedisUtil;

/**
 * Shiro权限管理的配置
 * 
 * @author guozhidong
 *
 */
@Configuration
public class ShiroConfig {
	
	@Value("${spring.redis.timeout:1800000}")
	private Integer timeout = 1800000; // redis有效期,单位毫秒,默认30分钟
	
	@Bean  
    public EhCacheManager ehCacheManager(){  
        EhCacheManager ehcacheManager = new EhCacheManager();  
        ehcacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");  
        return ehcacheManager;  
    }  

	/**
	 * 项目自定义的Realm
	 */
	@Bean
	public Realm shiroDbRealm(EhCacheManager ehCacheManager) {
		CachingRealm realm = new ShiroDbRealm();
		realm.setCacheManager(ehCacheManager);
		return realm;
	}
	
	/**
	 * Shiro生命周期处理器:
	 * 用于在实现了Intializable接口的Shiro bean初始化时调用了Initializable接口回调(例如:UserRealm)
	 * 在实现了Destroyable接口的Shiro bean销毁时调用Destroyable接口回调(例如:DefaultSecurityManager)
	 */
	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}
	
	@Bean  
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){  
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();  
        creator.setProxyTargetClass(true);  
        return creator;  
    } 
	
	/**
	 * 安全管理器
	 */
	@Bean
	public DefaultWebSecurityManager securityManager(Realm realm, CacheManager ehCacheManager, SessionManager sessionManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//设置realm  
        securityManager.setRealm(realm);  
        securityManager.setCacheManager(ehCacheManager);
        securityManager.setSessionManager(sessionManager);
        securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}
	
	/**
	 * session管理器
	 */
	@Bean
	public DefaultWebSessionManager sessionManager(DosthProperties dosthProperties) {
		DefaultWebSessionManager manager = new DefaultWebSessionManager();
//		manager.setSessionValidationInterval(dosthProperties.getSessionValidationInterval() * 1000);
		manager.setGlobalSessionTimeout(dosthProperties.getSessionInvalidateTime() * 1000);
		manager.setDeleteInvalidSessions(true);
		manager.setSessionValidationSchedulerEnabled(true);
		Cookie cookie = new SimpleCookie(ShiroHttpSession.DEFAULT_SESSION_ID_NAME);
		cookie.setName(TokenManager.TOKEN);
		cookie.setHttpOnly(true);
		manager.setSessionIdCookie(cookie);
		return manager;
	}
	
	/**
	 * rememberMe管理器
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager manager = new CookieRememberMeManager();
		manager.setCipherKey(Base64.decode("ZG9zdGg="));
		manager.setCookie(this.rememberMeCookie());
		return manager;
	}
	
	/**
	 * 记住密码Cookie
	 */
	private SimpleCookie rememberMeCookie() {
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setHttpOnly(true);
		simpleCookie.setMaxAge(7 * 24 * 60 * 60);
		return simpleCookie;
	}
	
	/**
	 * Shiro的过滤器链
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
		shiroFilter.setSecurityManager(securityManager);
		
		/**
		 * 默认的登录访问url
		 */
		shiroFilter.setLoginUrl("/login");
		/**
		 * 登录后跳转的url
		 */
		shiroFilter.setSuccessUrl("/");
		/**
		 * 没有权限跳转的url
		 */
		shiroFilter.setUnauthorizedUrl("/global/error");
		
		 /**
         * 配置shiro拦截器链
         *
         * anon  不需要认证
         * authc 需要认证
         * user  验证通过或RememberMe登录的都可以
         *
         * 当应用开启了rememberMe时,用户下次访问时可以是一个user,但不会是authc,因为authc是需要重新认证的
         *
         * 顺序从上到下,优先级依次降低
         *
         */
		Map<String, String> hashMap = new LinkedHashMap<>();
		hashMap.put("/static/**", "anon");
		hashMap.put("/login", "anon");
		hashMap.put("/picLogin", "anon"); // 脸谱采集登录页面
		hashMap.put("/loginPic", "anon"); // 脸谱采集登录
		hashMap.put("/feign/**", "anon"); // 远程接口忽略
		hashMap.put("/thirdfeign/**", "anon"); // 第三方远程接口忽略
		hashMap.put("/app/**", "anon"); // 远程接口忽略
		hashMap.put("/global/sessionError", "anon");
		hashMap.put("/kaptcha", "anon");
		hashMap.put("/**", "user");
		shiroFilter.setFilterChainDefinitionMap(hashMap);
		return shiroFilter;
	}
	
	/**
	 * 在方法中注入 securityManager,进行代理控制
	 */
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean(DefaultWebSecurityManager securityManager) {
		MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
		bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
		bean.setArguments(new Object[] { securityManager });
		return bean;
	}
	
	/**
	 * 启用shiro授权注解拦截方式,AOP式方法级权限检查
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		return RedisUtil.createRedisTemplate(redisConnectionFactory);
	}

	@Bean
	public TokenManager tokenManager(RedisTemplate<String, Object> redisTemplate) {
		TokenManager tokenManager = new RedisTokenManager(redisTemplate);
		// 设置redis管理器有效时间
		tokenManager.setTokenTimeout(this.timeout / 1000);
		return tokenManager;
	}
}