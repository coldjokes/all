package com.dosth.tool.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.dosth.common.cache.TokenManager;
import com.dosth.common.cache.token.RedisTokenManager;
import com.dosth.common.util.RedisUtil;

/**
 * 工具系统配置
 * 
 * @author guozhidong
 *
 */
@Configuration
public class ToolConfigure extends WebMvcConfigurerAdapter{

	@Value("${spring.redis.timeout:1800000}")
	private Integer timeout = 1800000; // redis有效期,单位毫秒,默认30分钟
	@Autowired
	private ToolProperties toolProperties;
	
	private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/"+ToolProperties.PREFIX +"/**").addResourceLocations("file:" + toolProperties.getUploadPath());
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        super.addResourceHandlers(registry);
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