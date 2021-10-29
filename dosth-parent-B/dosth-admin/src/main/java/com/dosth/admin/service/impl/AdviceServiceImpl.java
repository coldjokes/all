package com.dosth.admin.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.entity.mobile.Advice;
import com.dosth.admin.repository.AdviceRepository;
import com.dosth.admin.service.AdviceService;
import com.dosth.common.exception.DoSthException;

/**
 * 
 * @description 问题反馈Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class AdviceServiceImpl implements AdviceService {

	@Autowired
	private AdviceRepository adviceRepository;
	
	@Override
	public void save(Advice advice) throws DoSthException {
		this.adviceRepository.save(advice);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Advice get(Serializable id) throws DoSthException {
		return this.adviceRepository.findOne(id);
	}

	@Override
	public Advice update(Advice advice) throws DoSthException {
		return this.adviceRepository.saveAndFlush(advice);
	}

	@Override
	public void delete(Advice advice) throws DoSthException {
		this.adviceRepository.delete(advice);
	}
}