package com.dosth.app.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("tool-cabinet")
public interface CabinetAppService {
	
	/**
	 * @description 关门信号发送
	 * @param 
	 * @return
	 */
	@RequestMapping("/app/user/send_cmd")
	public String closeDoor(@RequestParam("cmd") String cmd);
	
}
