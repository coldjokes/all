package com.exam.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.controller.BaseController;
import com.exam.entity.RestFulResponse;
import com.exam.serivce.SettingService;
import com.exam.serivce.ShiroService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/api/siteConfig")
public class SiteConfigController extends BaseController {

	@Autowired
	private ShiroService shiroSvc;	
	@Autowired
	private SettingService settingSvc;	
	
	@SuppressWarnings("unchecked")
	@GetMapping
	public RestFulResponse<Map<String, Object>> getSiteConfig() {
		Map<String, Object> result = Maps.newHashMap();
		
		result.put("user", shiroSvc.getUser());
		result.put("setting", settingSvc.getAll());
		return success(1, Lists.newArrayList(result));
	}
}

