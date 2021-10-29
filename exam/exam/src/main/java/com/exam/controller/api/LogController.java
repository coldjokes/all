package com.exam.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.exam.controller.BaseController;
import com.exam.entity.RestFulResponse;
import com.exam.entity.criteria.LogCriteria;
import com.exam.entity.criteria.common.DateRangeCriteria;
import com.exam.entity.criteria.common.PageCriteria;
import com.exam.entity.criteria.common.SortCriteria;
import com.exam.entity.modal.Log;
import com.exam.serivce.LogService;

/**
  * 日志
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/api/logs")
public class LogController extends BaseController {

	@Autowired
	private LogService logSvc;
	
	
//	@LogRecord("查询日志列表")
	@GetMapping
	public RestFulResponse<Log> getLogs(LogCriteria logCriteria, PageCriteria pageCriteria, DateRangeCriteria dateRangeCriteria, SortCriteria sortCriteria) {
		Page<Log> pagedLogs = logSvc.getLogs(logCriteria, pageCriteria, dateRangeCriteria, sortCriteria);
		return success(pagedLogs.getTotal(), pagedLogs.getRecords());
	}
}

