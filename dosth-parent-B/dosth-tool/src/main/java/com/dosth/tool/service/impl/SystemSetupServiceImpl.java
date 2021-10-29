package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.SystemSetup;
import com.dosth.tool.repository.SystemSetupRepository;
import com.dosth.tool.service.SystemSetupService;

/**
 * @description 系统设置Service实现
 * 
 * @author Zhongyan.He
 * 
 * **/
@Service
@Transactional
public class SystemSetupServiceImpl implements SystemSetupService {
	@Autowired
	private SystemSetupRepository systemSetupRepository;

	@Override
	public void save(SystemSetup entity) throws DoSthException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SystemSetup get(Serializable id) throws DoSthException {
		SystemSetup systemSetup = this.systemSetupRepository.findOne(id);
		return systemSetup;
	}

	@Override
	public SystemSetup update(SystemSetup entity) throws DoSthException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(SystemSetup entity) throws DoSthException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<SystemSetup> findAllSystemSetup() {
		return this.systemSetupRepository.findAll();
	}
	
	@Override
	public String getValueByKey(String setupKey) {
		return this.systemSetupRepository.getValueByKey(setupKey);
	}
}
