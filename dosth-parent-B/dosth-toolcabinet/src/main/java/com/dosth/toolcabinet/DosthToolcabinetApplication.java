package com.dosth.toolcabinet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

/**
 * 工具柜启动类
 * 
 * @author guozhidong
 *
 */
@EnableFeignClients
@EnableEurekaClient
@ComponentScan("com.dosth")
@SpringBootApplication
public class DosthToolcabinetApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosthToolcabinetApplication.class, args);
	}
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}