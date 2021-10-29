package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.entity.TimeTask;

/**
 * 定时任务相关方法
 * 
 * @author Weifeng.Li
 *
 */
public interface TimeTaskService extends BaseService<TimeTask> {

	/**
	 * 分页查询
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	Page<TimeTask> getPage(int pageNo, int pageSize);

	/**
	 * 根据任务状态查询定时任务
	 * 
	 * @param status
	 * @return
	 */
	List<TimeTask> getListByStatus(OnOrOff status);
}
