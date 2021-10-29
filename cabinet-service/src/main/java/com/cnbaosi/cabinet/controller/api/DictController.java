package com.cnbaosi.cabinet.controller.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.modal.Dict;
import com.cnbaosi.cabinet.serivce.DictService;
import com.google.common.collect.Lists;

/**
 * 字典
 * 
 * @author Yifeng Wang  
 */
@RestController
@RequestMapping("/api/dicts")
public class DictController extends BaseController {

	@Autowired
	private DictService dictSvc;
	
//	@LogRecord("查询字典")
	@SuppressWarnings("unchecked")
	@GetMapping
	public RestFulResponse<Map<String, List<Dict>>> getLogs() {
		return success(1, Lists.newArrayList(dictSvc.getDicts()));
	}
}

