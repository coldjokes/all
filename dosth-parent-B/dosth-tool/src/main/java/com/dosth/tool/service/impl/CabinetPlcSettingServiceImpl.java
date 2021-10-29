package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.CabinetPlcSetting;
import com.dosth.tool.entity.PlcSetting;
import com.dosth.tool.repository.CabinetPlcSettingRepository;
import com.dosth.tool.repository.PlcSettingRepository;
import com.dosth.tool.service.CabinetPlcSettingService;

/**
 * @Description 刀具柜plc参数设置Service实现
 * @Author guozhidong
 */
@Service
@Transactional
public class CabinetPlcSettingServiceImpl implements CabinetPlcSettingService {

	@Autowired
	private PlcSettingRepository plcSettingRepository;
	
	@Autowired
	private CabinetPlcSettingRepository cabinetPlcSettingRepository;

	@Override
	public void save(CabinetPlcSetting setting) throws DoSthException {
		this.cabinetPlcSettingRepository.save(setting);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public CabinetPlcSetting get(Serializable id) throws DoSthException {
		return this.cabinetPlcSettingRepository.getOne(id);
	}

	@Override
	public CabinetPlcSetting update(CabinetPlcSetting setting) throws DoSthException {
		return this.cabinetPlcSettingRepository.saveAndFlush(setting);
	}

	@Override
	public void delete(CabinetPlcSetting setting) throws DoSthException {

	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public List<CabinetPlcSetting> getCabinetPlcSettingListByCabinetId(String cabinetId) {
		List<CabinetPlcSetting> list = new ArrayList<>();
		Criteria<PlcSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<PlcSetting> settingList = this.plcSettingRepository.findAll(criteria);
		List<CabinetPlcSetting> cpsList;
		for(PlcSetting setting: settingList) {
			cpsList = this.cabinetPlcSettingRepository.getCabinetPlcSettingListByCabinetId(cabinetId, setting.getId());
			if (cpsList != null && cpsList.size() > 0) {
				list.add(cpsList.get(0));
			} else {
				list.add(new CabinetPlcSetting(setting));
			}
		}
		return list;
	}
}