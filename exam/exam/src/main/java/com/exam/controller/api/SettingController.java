package com.exam.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exam.aop.log.LogRecord;
import com.exam.controller.BaseController;
import com.exam.entity.RestFulResponse;
import com.exam.entity.modal.Setting;
import com.exam.serivce.SettingService;
import com.google.common.collect.Lists;

/**
 * 
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/api/setting")
public class SettingController extends BaseController {

	@Autowired
	private SettingService settingSvc;	
	
	@SuppressWarnings("unchecked")
	@LogRecord("获取所有配置")
	@GetMapping
	public RestFulResponse<Map<String, Object>> setting() {
		return success(1, Lists.newArrayList(settingSvc.getAll()));
	}
	
	
	@LogRecord("更新配置")
	@PutMapping
	public RestFulResponse<String> updateSetting(@RequestBody List<Setting> list) {
		return actionResult(settingSvc.updateAll(list));
	}
}

