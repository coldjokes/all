package com.cnbaosi.thirdfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @description 第三方外部请求
 * @author guozhidong
 *
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class CnBaosiThirdFeignApplication {
	public static void main(String[] args) {
		SpringApplication.run(CnBaosiThirdFeignApplication.class, args);
	}
}