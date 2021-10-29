package com.dosth.admin.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.SystemInfo;
import com.dosth.admin.repository.SystemInfoRepository;
import com.dosth.admin.service.SystemInfoService;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;

/**
 * 系统信息Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class SystemInfoServiceImpl implements SystemInfoService {

	@Autowired
	private SystemInfoRepository systemInfoRepository;
	
	@Override
	public void save(SystemInfo systemInfo) throws DoSthException {
		systemInfo.setSystemName(systemInfo.getSystemName());
		systemInfo.setUrl(systemInfo.getUrl());
		systemInfo.setStatus(UsingStatus.ENABLE);
		this.systemInfoRepository.save(systemInfo);
	}

	@Override
	public SystemInfo update(SystemInfo systemInfo) throws DoSthException {
		return this.systemInfoRepository.saveAndFlush(systemInfo);
	}

	@Override
	public void delete(SystemInfo systemInfo) throws DoSthException {
		this.systemInfoRepository.delete(systemInfo);
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public SystemInfo get(Serializable id) throws DoSthException {
		return this.systemInfoRepository.findOne(id);
	}

	@Override
	public SystemInfo findSystemInfoById(String id) throws DoSthException {
		return this.systemInfoRepository.findSystemInfoById(id);
	}

	@Override
	public Page<SystemInfo> getPage(int pageNo, int pageSize, String name)
			throws BusinessException {
		Criteria<SystemInfo> criteria = new Criteria<SystemInfo>();
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("systemName", name, true));
		}
		Page<SystemInfo> page = this.systemInfoRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		//检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if(page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber()+1)) {
			pageNo = page.getTotalPages()-1;
			page = this.systemInfoRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		}
		return page;
	}
}
