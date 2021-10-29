package com.exam.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.exam.security.AuthcFilter;
import com.exam.security.CustomModularRealmAuthenticator;
import com.exam.security.ShiroRealm;

/**
 * shiro相关配置
 * 
 * @author Yifeng Wang
 */
@Configuration
public class ShiroConfig {
	
    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
        DelegatingFilterProxy filter = new DelegatingFilterProxy();
        filter.setTargetFilterLifecycle(true);
        FilterRegistrationBean<DelegatingFilterProxy> bean = new FilterRegistrationBean<>(filter);
        bean.addUrlPatterns("/*");
        bean.setName("shiroFilter");
        return bean;
    }
	/**
	 * 	自定义过滤器，拦截所有/api请求
	 */
	@Bean(name = "authc")
	public AuthcFilter authcFilter() {
		return new AuthcFilter();
	}

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件过滤器
     * 1.配置shiro安全管理器接口securityManage;
     * 2.shiro 连接约束配置filterChainDefinitions;
	 */
	@Bean("shiroFilter")
	public ShiroFilterFactoryBean shiroFilter() {
		// shiroFilterFactoryBean对象
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

		// 配置shiro安全管理器 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(this.securityManager());
		// filterChainDefinitions拦截器
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置不会被拦截的链接 从上向下顺序判断
		filterChainDefinitionMap.put("/static/**", "anon");
		filterChainDefinitionMap.put("/templates/**", "anon");
		// 以下请求都必须通过自定义过滤器authc认证通过才可以访问
//		filterChainDefinitionMap.put("/api/**", "authc");  //TODO 需要释放
		
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 	账号密码realm
	 */
	@Bean
	public Realm realm() {
		ShiroRealm realm = new ShiroRealm();
		//md5加密
        HashedCredentialsMatcher hcm = new HashedCredentialsMatcher();
        hcm.setHashAlgorithmName("MD5");
        realm.setCredentialsMatcher(hcm);
		return realm;
	}

	
	@Bean
	public AbstractAuthenticator abstractAuthenticator() {
		// 自定义模块化认证器，用于解决多realm抛出异常问题
		ModularRealmAuthenticator authenticator = new CustomModularRealmAuthenticator();
		// 认证策略：AtLeastOneSuccessfulStrategy(默认)，AllSuccessfulStrategy，FirstSuccessfulStrategy
		authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
		// 加入realms
		ArrayList<Realm> realms = new ArrayList<Realm>();
		realms.add(realm());
		authenticator.setRealms(realms);
		return authenticator;
	}

	/**
	 * shiro安全管理器设置realm认证
	 */
	@Bean
	public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		ArrayList<Realm> realms = new ArrayList<Realm>();
		realms.add(realm());
		securityManager.setRealms(realms);
		
		/*
		// 自定义缓存实现，可以使用redis
        securityManager.setCacheManager(shiroCacheManager());		
        // 自定义session管理，可以使用redis
        securityManager.setSessionManager(sessionManager());
        // 注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());
        */
		
        // 认证器
        securityManager.setAuthenticator(abstractAuthenticator());
        
        return securityManager;
	}
	
}
