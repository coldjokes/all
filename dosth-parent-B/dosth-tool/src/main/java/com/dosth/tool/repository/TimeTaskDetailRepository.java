package com.dosth.tool.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.TimeTaskDetail;

public interface TimeTaskDetailRepository extends BaseRepository<TimeTaskDetail, String> {

	@Query("select d.cronExpression from TimeTaskDetail d where id = 1")
	public String getCron();

	/**
	 * @description 定时任务-清理用户
	 */
	@Modifying
	@Transactional
	@Query("update TimeTaskDetail set userName = 'admin', accountId = '1,'")
	public void initTimeTaskDetail();
}