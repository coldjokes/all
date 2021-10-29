package com.exam.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * 获取当前项目环境
 * 
 * @author Yifeng Wang  
 */
@Configuration
public class ProfileConfig {
	//多环境
	public static final String PROFILE_DEV = "dev"; //开发环境
	public static final String PROFILE_TEST = "test"; //测试环境
	public static final String PROFILE_PROD = "prod"; //生产环境

    @Autowired
    private ApplicationContext context;

    public String getActiveProfile() {
        return context.getEnvironment().getActiveProfiles()[0];
    }
}

