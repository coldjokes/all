package com.dosth;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.dosth.netty.constant.NettyConfig;
import com.dosth.netty.remote.server.Server;
import com.dosth.netty.server.NettyServer;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.util.BorrowPostUtil;
import com.dosth.tool.common.util.FeedingEmailUtil;
import com.dosth.tool.netty.ServerHandler;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @description 工具管理启动类
 * @author guozhidong
 *
 */
@EnableFeignClients
@EnableEurekaClient
@EnableCaching
@SpringBootApplication
@EnableSwagger2
public class ToolApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ToolApplication.class, args);
	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ToolApplication.class);
	}

	@Autowired
	private ToolProperties toolProperties;
	
	@Autowired
	private FeedingEmailUtil feedingEmailUtil;
	
	@Autowired
	private BorrowPostUtil borrowPostUtil;
	
	private static Server nettyServer;

	@PostConstruct
	public void init() {
		// 需要开启一个新的线程来执行netty server 服务器
		new Thread(new Runnable() {
			public void run() {
				nettyServer = new NettyServer(new NettyConfig(toolProperties.getNettyServerHost(), toolProperties.getNettyServerPort()), new ServerHandler("TOOL-SERVER"));
				nettyServer.start();
			}
		}).start();
		
		// 补料邮件工具类启动
		this.feedingEmailUtil.start();
		
		// 取料推送工具类启动
		this.borrowPostUtil.start();
	}

	@PreDestroy
	public void close() {
		if (nettyServer != null) {
			nettyServer.stop();
		}
	}
}