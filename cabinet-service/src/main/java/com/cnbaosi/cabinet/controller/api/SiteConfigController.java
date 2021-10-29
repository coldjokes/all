package com.cnbaosi.cabinet.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.serivce.CabinetServiceConfigService;
import com.cnbaosi.cabinet.serivce.SettingService;
import com.cnbaosi.cabinet.serivce.ShiroService;
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
	private CabinetServiceConfigService configSvc;	
	@Autowired
	private SettingService settingSvc;	
	
	@SuppressWarnings("unchecked")
	@GetMapping
	public RestFulResponse<Map<String, Object>> getSiteConfig() {
		Map<String, Object> result = Maps.newHashMap();
		
		result.put("user", shiroSvc.getUser());
		result.put("config", configSvc.getConfig());
		result.put("setting", settingSvc.getAll());
		return success(1, Lists.newArrayList(result));
	}
}

