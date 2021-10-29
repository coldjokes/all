package com.dosth.tool.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.repository.EquDetailRepository;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.service.SmartCabinetService;

/**
 * @Desc 智能刀具柜设置Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class SmartCabinetServiceImpl implements SmartCabinetService {

	@Autowired
	private EquDetailRepository equDetailRepository;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	
	@Override
	public EquDetail getEquDetail(String equDetailId) throws DoSthException {
		EquDetail detail = this.equDetailRepository.getOne(equDetailId);
		return detail;
	}

	@Override
	public List<EquDetailSta> getStaList(String equDetailId) throws DoSthException {
		List<EquDetailSta> staList = this.equDetailStaRepository.getStaListByDetailId(equDetailId);
		return staList;
	}
}