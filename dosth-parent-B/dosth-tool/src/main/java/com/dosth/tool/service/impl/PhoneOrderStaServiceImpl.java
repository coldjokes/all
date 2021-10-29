package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.mobile.PhoneOrder;
import com.dosth.tool.entity.mobile.PhoneOrderSta;
import com.dosth.tool.repository.PhoneOrderStaRepository;
import com.dosth.tool.service.PhoneOrderStaService;

/**
 * 
 * @description 手机预约状态Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class PhoneOrderStaServiceImpl implements PhoneOrderStaService {

	@Autowired
	private PhoneOrderStaRepository phoneOrderStaRepository;
	
	@Override
	public void save(PhoneOrderSta phoneOrderSta) throws DoSthException {
		this.phoneOrderStaRepository.save(phoneOrderSta);
	}

	@Override
	public PhoneOrderSta get(Serializable id) throws DoSthException {
		PhoneOrderSta phoneOrderSta = this.phoneOrderStaRepository.findOne(id);
		return phoneOrderSta;
	}

	@Override
	public PhoneOrderSta update(PhoneOrderSta phoneOrderSta) throws DoSthException {
		return this.phoneOrderStaRepository.saveAndFlush(phoneOrderSta);
	}

	@Override
	public void delete(PhoneOrderSta phoneOrderSta) throws DoSthException {
		this.phoneOrderStaRepository.delete(phoneOrderSta);
	}

	@Override
	public List<PhoneOrder> getFinishedOrderListByOrderId(String orderId) {
		return this.phoneOrderStaRepository.getFinishedOrderListByOrderId(orderId);
	}
}
