package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.MatReturnDetail;
import com.dosth.tool.repository.MatReturnDetailRepository;
import com.dosth.tool.service.MatReturnDetailService;

/**
 * 
 * @description 物料归还详情Service实现
 * @author chenlei
 *
 */
@Service
@Transactional
public class MatReturnDetailServiceImpl implements MatReturnDetailService {

	@Autowired
	private MatReturnDetailRepository matReturnDetailRepository;

	@Override
	public void save(MatReturnDetail entity) throws DoSthException {
		this.matReturnDetailRepository.save(entity);
	}

	@Override
	public MatReturnDetail get(Serializable id) throws DoSthException {
		MatReturnDetail matReturnDetail = this.matReturnDetailRepository.findOne(id);
		return matReturnDetail;
	}

	@Override
	public MatReturnDetail update(MatReturnDetail entity) throws DoSthException {
		MatReturnDetail matReturnDetail = this.matReturnDetailRepository.saveAndFlush(entity);
		return matReturnDetail;
	}

	@Override
	public void delete(MatReturnDetail entity) throws DoSthException {
		this.matReturnDetailRepository.delete(entity);
	}

	@Override
	public List<String> getReturnIds(String typeId) throws DoSthException {
		List<MatReturnDetail> detailList = this.matReturnDetailRepository.getReturnIds(typeId);
		List<String> ids = new ArrayList<>();
		for(MatReturnDetail detail : detailList) {
			ids.add(detail.getMatReturnBackId());
		}
		return ids;
	}

}