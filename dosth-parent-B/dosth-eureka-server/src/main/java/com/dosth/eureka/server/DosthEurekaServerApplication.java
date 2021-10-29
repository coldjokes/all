package com.dosth.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Dosth Eureka Server启动类
 * 
 * @author guozhidong
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class DosthEurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(DosthEurekaServerApplication.class, args);
	}
}