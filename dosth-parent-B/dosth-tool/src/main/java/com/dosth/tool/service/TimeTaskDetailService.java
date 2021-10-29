package com.dosth.tool.service;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.util.OpTip;

/**
 * 定时任务详情相关方法
 * 
 * @author Weifeng.Li
 *
 */
public interface TimeTaskDetailService extends BaseService<TimeTaskDetail> {

	/**
	 * 查询定时任务详情
	 * 
	 * @return
	 */
	TimeTaskDetail getDetail();

	/**
	 * 更新定时任务详情
	 * 
	 * @param timeTaskDetail
	 * @return
	 */
	OpTip saveAndFlush(TimeTaskDetail timeTaskDetail);

}
