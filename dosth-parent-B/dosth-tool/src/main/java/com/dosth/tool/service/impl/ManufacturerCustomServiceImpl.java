package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.ManufacturerCustom;
import com.dosth.tool.repository.ManufacturerCustomRepository;
import com.dosth.tool.service.ManufacturerCustomService;

/**
 * 供货商客服Service实现
 * @author liweifeng
 *
 */
@Service
@Transactional
public class ManufacturerCustomServiceImpl implements ManufacturerCustomService {

	@Autowired
	private ManufacturerCustomRepository manufacturerCustomRepository;
	
	@Override
	public Page<ManufacturerCustom> getPage(int pageNo, int pageSize, String manufacturerId, String name, String status) throws DoSthException {
		Criteria<ManufacturerCustom> criteria = new Criteria<>();
		if(manufacturerId != null && !"".equals(manufacturerId)) {
			criteria.add(Restrictions.eq("manufacturerId", manufacturerId, true));
		}
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.or(Restrictions.like("contactName", name, true),
					Restrictions.like("contactPhone", name, true)));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}
		return this.manufacturerCustomRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
	}
	
	@Override
	public void save(ManufacturerCustom manufacturerCustom) throws DoSthException {
		this.manufacturerCustomRepository.save(manufacturerCustom);
	}

	@Override
	public ManufacturerCustom get(Serializable id) throws DoSthException {
		ManufacturerCustom manufacturerCustom = this.manufacturerCustomRepository.findOne(id);
		return manufacturerCustom;
	}

	@Override
	public ManufacturerCustom update(ManufacturerCustom manufacturerCustom) throws DoSthException {
		return this.manufacturerCustomRepository.saveAndFlush(manufacturerCustom);
	}

	@Override
	public void delete(ManufacturerCustom manufacturerCustom) throws DoSthException {
		this.manufacturerCustomRepository.delete(manufacturerCustom);
	}

	@Override
	public List<ManufacturerCustom> getCustomListByManufacturerId(String manufacturerId) throws DoSthException {
		return this.manufacturerCustomRepository.getCustomListByManufacturerId(manufacturerId, UsingStatus.ENABLE);
	}
}