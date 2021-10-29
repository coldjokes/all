package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.LockParam;
import com.dosth.tool.repository.LockParamRepository;
import com.dosth.tool.service.LockParamService;

/**
 * @description 锁控板参数Service实现
 * @author Weifeng.Li
 */
@Service
@Transactional
public class LockParamServiceImpl implements LockParamService {

	@Autowired
	private LockParamRepository lockParamRepository;

	@Override
	public void save(LockParam entity) throws DoSthException {
		this.lockParamRepository.save(entity);
	}

	@Override
	public LockParam get(Serializable id) throws DoSthException {
		return this.lockParamRepository.findOne(id);
	}

	@Override
	public LockParam update(LockParam entity) throws DoSthException {
		return this.lockParamRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(LockParam entity) throws DoSthException {
		this.lockParamRepository.delete(entity);
	}

	@Override
	public List<LockParam> getLockParamByEquSettingId(String equSettingId, CabinetType cabinetType) {
		Criteria<LockParam> criteria = new Criteria<>();
		if (equSettingId != null && !"".equals(equSettingId)) {
			criteria.add(Restrictions.eq("equSettingId", equSettingId, true));
		}
		if (cabinetType != null) {
			criteria.add(Restrictions.eq("cabinetType", cabinetType, true));
		}
		return this.lockParamRepository.findAll(criteria);
	}

	@Override
	public List<Integer> getBoardNoListByEquSettingId(String equSettingId) {
		return this.lockParamRepository.getBoardNoListByEquSettingId(equSettingId);
	}

	@Override
	public List<Integer> getStoreBoardNoListByEquSettingId(String mainCabinetId) {
		List<LockParam> paramList = this.lockParamRepository.getBoardNoListByMainCabinetId(mainCabinetId);
		paramList = paramList.stream().filter(param -> param.getCabinetType().equals(CabinetType.STORE_CABINET)).collect(Collectors.toList());
		List<Integer> boardNoList = new ArrayList<>();
		for (LockParam param : paramList) {
			boardNoList.add(param.getBoardNo());
		}
		return boardNoList;
	}
}