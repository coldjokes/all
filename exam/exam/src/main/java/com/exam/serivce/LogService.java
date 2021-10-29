package com.exam.serivce;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.exam.entity.criteria.LogCriteria;
import com.exam.entity.criteria.common.DateRangeCriteria;
import com.exam.entity.criteria.common.PageCriteria;
import com.exam.entity.criteria.common.SortCriteria;
import com.exam.entity.modal.Log;

/**
 *  日志相关方法
 * 
 * @author Yifeng Wang  
 */
public interface LogService extends IService<Log> {


	/**
	 * 新增日志
	 * @param log
	 * @return
	 */
	boolean addLog(Log log);
	
	/**
	 * 查询日志列表
	 * @param logCriteria
	 * @param pageCriteria
	 * @param dateRangeCriteria
	 * @param sortCriteria
	 * @return
	 */
	Page<Log> getLogs(LogCriteria logCriteria, PageCriteria pageCriteria, DateRangeCriteria dateRangeCriteria, SortCriteria sortCriteria);
}

