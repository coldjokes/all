package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.constant.EnumDoor;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.EquDetailService;

/**
 * @description 设备详情Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class EquDetailServiceImpl implements EquDetailService {

	@Autowired
	private EquDetailRepository equDetailRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;

	@Override
	public EquDetail add(EquDetail equDetail) {
		return this.equDetailRepository.save(equDetail);
	}

	@Override
	public void save(EquDetail info) throws DoSthException {
		this.equDetailRepository.save(info);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public EquDetail get(Serializable id) throws DoSthException {
		return this.equDetailRepository.findOne(id);
	}

	@Override
	public EquDetail update(EquDetail info) throws DoSthException {
		return this.equDetailRepository.saveAndFlush(info);
	}

	@Override
	public void delete(EquDetail info) throws DoSthException {
		this.equDetailRepository.delete(info);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<EquDetail> getEquDetailListBySettingId(String cabinetId) {
		return this.equDetailRepository.getEquDetailListBySettingId(cabinetId);
	}

	@Override
	public EnumDoor getDoor(String ip) {
		List<EquDetail> list = equDetailRepository.getDoor(ip);
		EquSetting info = this.equSettingRepository.getOne(list.get(0).getEquSettingId());
		return EnumDoor.valueOf(info.getDoor().name());
	}

}