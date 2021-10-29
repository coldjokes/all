package com.dosth.tool.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.PlcSetting;
import com.dosth.tool.repository.PlcSettingRepository;
import com.dosth.tool.service.PlcSettingService;

/**
 * [LC设置Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class PlcSettingServiceImpl implements PlcSettingService {
	
	@Autowired
	private PlcSettingRepository plcSettingRepository;

	@Override
	public void save(PlcSetting plcSetting) throws DoSthException {
		plcSetting.setPlcName(plcSetting.getPlcName());
		plcSetting.setAddress(plcSetting.getAddress());
		plcSetting.setDefaultValue(plcSetting.getDefaultValue());
		plcSetting.setRemark(plcSetting.getRemark());
		plcSetting.setStatus(UsingStatus.ENABLE);
		this.plcSettingRepository.save(plcSetting);
	}

	@Override
	public PlcSetting update(PlcSetting plcSetting) throws DoSthException {
		return this.plcSettingRepository.saveAndFlush(plcSetting);
	}

	@Override
	public void delete(PlcSetting plcSetting) throws DoSthException {
		this.plcSettingRepository.delete(plcSetting);
	}

	@Override
	public PlcSetting get(Serializable id) throws DoSthException {
		PlcSetting plcSetting = this.plcSettingRepository.getOne(id);
		return plcSetting;
	}
	
	@Override
	public Page<PlcSetting> getPage(int pageNo, int pageSize, String plcName, String status) 
			throws DoSthException {
		Criteria<PlcSetting> criteria = new Criteria<PlcSetting>();
		if (plcName != null && !"".equals(plcName)) {
			criteria.add(Restrictions.like("plcName", plcName, true));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}
		return this.plcSettingRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
	}

}
