package com.exam.serivce.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.exam.entity.criteria.LogCriteria;
import com.exam.entity.criteria.common.DateRangeCriteria;
import com.exam.entity.criteria.common.PageCriteria;
import com.exam.entity.criteria.common.SortCriteria;
import com.exam.entity.modal.Log;
import com.exam.entity.modal.User;
import com.exam.mapper.LogMapper;
import com.exam.serivce.LogService;
import com.exam.serivce.ShiroService;
import com.exam.util.StringUtil;

/**
 *  日志方法实现类
 * 
 * @author Yifeng Wang  
 */

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService{

	@Autowired
	private ShiroService shiroSvc;
	
	@Override
	public boolean addLog(Log log) {
		User user = shiroSvc.getUser();
		String username;
		if(user != null) {
			username = user.getUsername();
		} else {
			username = "无登陆访问";
		}
		log.setUsername(username);
		return super.insert(log);
	}

	@Override
	public Page<Log> getLogs(LogCriteria logCriteria, PageCriteria pageCriteria, DateRangeCriteria dateRangeCriteria, SortCriteria sortCriteria) {
		String username = logCriteria.getUsername();
		Integer logType = logCriteria.getLogType();
		Long startTime = dateRangeCriteria.getStartTime();
		Long endTime = dateRangeCriteria.getEndTime();
		Integer curPage = pageCriteria.getCurPage();
		Integer pageSize = pageCriteria.getPageSize();
		String sortField = sortCriteria.getSortField();
		String sortOrder = sortCriteria.getSortOrder();
		
		EntityWrapper<Log> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(username)) {
			wrapper.like("username", username);
		}
		if(logType != null && logType != -1) {
			wrapper.eq("log_type", logType);
		}
		if(startTime != null && endTime != null) {
			wrapper.ge("create_time", new Date(startTime)).le("create_time", new Date(endTime));
		}
		
		if(sortField != null) {
			wrapper.orderBy(StringUtil.camelToUnderline(sortField), sortOrder.equals(SortCriteria.AES));
		} else {
			wrapper.orderBy("create_time", false);
		}
		
		Page<Log> page = new Page<>(curPage, pageSize);
		
		return selectPage(page, wrapper);
	}

}

