package com.dosth.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.constant.state.LogType;
import com.dosth.admin.entity.OperationLog;
import com.dosth.admin.repository.OperationLogRepository;
import com.dosth.admin.service.OperationLogService;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.util.DateUtil;

/**
 * 操作日志Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class OperationLogServiceImpl implements OperationLogService {

	@Autowired
	private OperationLogRepository operationLogRepository;
	
	@Override
	public OperationLog findOperationLogByAccountId(Long accountId) throws DoSthException {
		return this.operationLogRepository.findOperationLogByAccountId(accountId);
	}

	@Override
	public Page<OperationLog> getPage(int pageNo, int pageSize, String name, String logName, String beginTime,
			String endTime) throws BusinessException {
		Criteria<OperationLog> c = new Criteria<OperationLog>();
		if (name != null && !"".equals(name)) {
			c.add(Restrictions.like("logName", name, true));
		}
		if (logName != null && !"".equals(logName) && !"-1".equals(logName)) {
			c.add(Restrictions.eq("logName", LogType.valueOf(logName), true));
		}
		if (beginTime != null && !"".equals(beginTime)) {
			c.add(Restrictions.gte("createTime",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime)) {
			c.add(Restrictions.lte("createTime",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}
		Page<OperationLog> page = this.operationLogRepository.findAll(c, new PageRequest(pageNo, pageSize));
		//检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if(page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber()+1)) {
			pageNo = page.getTotalPages()-1;
			page = this.operationLogRepository.findAll(c, new PageRequest(pageNo, pageSize));
		}
		return page;
	}

}
