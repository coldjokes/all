package com.dosth.admin.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.mobile.Helper;
import com.dosth.admin.repository.HelperRepository;
import com.dosth.admin.service.HelperService;
import com.dosth.common.exception.DoSthException;

/**
 * 
 * @description 帮助中心Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class HelperServiceImpl implements HelperService {

	@Autowired
	private HelperRepository helperRepository;
	
	@Override
	public void save(Helper helper) throws DoSthException {
		this.helperRepository.save(helper);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Helper get(Serializable id) throws DoSthException {
		return this.helperRepository.getOne(id);
	}

	@Override
	public Helper update(Helper helper) throws DoSthException {
		return this.helperRepository.saveAndFlush(helper);
	}

	@Override
	public void delete(Helper helper) throws DoSthException {
		this.helperRepository.delete(helper);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Helper> getHelperList() throws BusinessException {
		return this.helperRepository.findAll();
	}
}