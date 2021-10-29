package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.TimeTask;
import com.dosth.tool.repository.TimeTaskRepository;
import com.dosth.tool.service.TimeTaskService;

/**
 * 定时任务相关方法实现类
 * 
 * @author Weifeng.Li
 *
 */
@Service
@Transactional
public class TimeTaskServiceImpl implements TimeTaskService {

	@Autowired
	private TimeTaskRepository timeTaskRepository;

	@Override
	public void save(TimeTask entity) throws DoSthException {
		this.timeTaskRepository.save(entity);
	}

	@Override
	public TimeTask get(Serializable id) throws DoSthException {
		return this.timeTaskRepository.findOne(id);
	}

	@Override
	public TimeTask update(TimeTask entity) throws DoSthException {
		return this.timeTaskRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(TimeTask entity) throws DoSthException {
		this.timeTaskRepository.delete(entity);
	}

	@Override
	public Page<TimeTask> getPage(int pageNo, int pageSize) {
		List<TimeTask> list = this.timeTaskRepository.findAll();
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}

	@Override
	public List<TimeTask> getListByStatus(OnOrOff status) {
		Criteria<TimeTask> criteria = new Criteria<>();
		if (status != null) {
			criteria.add(Restrictions.eq("status", status, true));
		}
		List<TimeTask> list = this.timeTaskRepository.findAll(criteria);
		return list;
	}

}
