package com.dosth.toolcabinet;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.dosth.toolcabinet.config.CabinetConfig;

/**
 * @description 等待tool启动完成
 * @author chenlei
 */
@Component
@Order(1) //如果多个自定义ApplicationRunner，用来标明执行顺序
public class DosthToolcabinetRunnerCheck implements ApplicationRunner {
	private static final Logger logger = LoggerFactory.getLogger(DosthToolcabinetApplication.class);
	
	@Autowired
	private CabinetConfig cabinetConfig;
	
	@Override	
	public void run(ApplicationArguments args) throws Exception {
		testServerConnect();
	}
	
	public void testServerConnect() {
        boolean result = false;
        Socket socket = null;
        while(true) {
	        try {
	        	Thread.sleep(1000); //1s检查一次
	        	logger.info("服务器IP:"+ cabinetConfig.getNettyServerHost());
	        	socket  = new Socket(cabinetConfig.getNettyServerHost(), cabinetConfig.getToolServerPort()); //连接服务器
	            result = socket.isConnected();
	            if(result) {
	            	logger.info("====tool服务注册成功!!!");
	            	Thread.sleep(1000);
	            	break;
	            }
	        } catch (Exception e) {
	        	logger.info(">>>>等待tool服务注册...");
	        }finally {
	        	if(socket != null) {
	        		try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	}
	        }
        }
    }
}