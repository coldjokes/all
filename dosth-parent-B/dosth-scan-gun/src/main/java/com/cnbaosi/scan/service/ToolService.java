package com.cnbaosi.scan.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.app.dto.AppRecycleReview;

/**
 * @description 工具访问接口
 * 
 * @author guozhidong
 *
 */
@FeignClient("service-tool")
public interface ToolService {
	
	@RequestMapping("/feign/getVer")
	public String getVer();
	
	@RequestMapping("/app/recycle/code_info")
	public AppRecycleReview recycleInfo(@RequestParam("code") String code);


}