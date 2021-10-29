package com.dosth.toolcabinet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.comm.plc.PLCUtil;

/**
 * APPController
 * 
 * @author liweifeng
 *
 */
@RestController
@RequestMapping("/app")
public class AppCabinetController {
	
	private static final Logger logger = LoggerFactory.getLogger(AppCabinetController.class);
	
	//关门成功
	private static String CLOSE_SUCCESS = "1";
	//关门失败
	private static String CLOSE_FAILD = "0";
	
	
	/**
	 * @description 关门
	 * @param 
	 * @return
	 */
	@RequestMapping("/user/send_cmd")
	public String closeDoor(@RequestParam("cmd") String cmd) {
		String result = CLOSE_FAILD;
		try {
			boolean isRunCmdWell = PLCUtil.closeDoorWithStatusChecking(cmd);
			if (isRunCmdWell) {
				result = CLOSE_SUCCESS;
				logger.info("门已经关了");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}