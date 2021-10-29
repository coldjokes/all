package com.dosth.toolcabinet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @description 前台网页启动
 * @author Zhidong.Guo
 *
 */
@Component
@Order(3)
public class DosthToolcabinetWebStart implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(DosthToolcabinetWebStart.class);

	@Value("${server.port}")
	private int port;

	@Override
	public void run(String... args) throws Exception {
		logger.info("项目启动完成,即将打开浏览器~~~~~");
//		try {
//			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://localhost:" + port);
//			Runtime.getRuntime().exec("CMD /K start D:/resource/login.bat " + port);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}