package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.tool.repository.RestitutionTypeRepository;
import com.dosth.tool.service.RestitutionTypeService;
import com.dosth.toolcabinet.enums.ReturnBackType;

/**
 * 归还类型定义Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class RestitutionTypeServiceImpl implements RestitutionTypeService {
	
	@Autowired
	private RestitutionTypeRepository restitutionTypeRepository;
	
	@Override
	public void save(RestitutionType restitutionType) throws DoSthException {
		this.restitutionTypeRepository.save(restitutionType);
	}

	@Override
	public RestitutionType update(RestitutionType restitutionType) throws DoSthException {
		return this.restitutionTypeRepository.saveAndFlush(restitutionType);
	}

	@Override
	public void delete(RestitutionType restitutionType) throws DoSthException {
		this.restitutionTypeRepository.delete(restitutionType);
	}
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public RestitutionType get(Serializable id) throws DoSthException {
		RestitutionType restitutionType = this.restitutionTypeRepository.findOne(id);
		return restitutionType;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<RestitutionType> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException {
		Criteria<RestitutionType> criteria = new Criteria<RestitutionType>();
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("restName", name.trim(), true));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}
		return this.restitutionTypeRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
	}

	@Override
	public List<ZTreeNode> reTypeTree() throws DoSthException {
		List<ZTreeNode> treeList = new ArrayList<>();
		for (ReturnBackType type : ReturnBackType.values()) {
			treeList.add(new ZTreeNode(type.name(), type.name(), type.getDesc()));
		}
		return treeList;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<RestitutionType> getReTypeList() throws DoSthException {
		return this.restitutionTypeRepository.findAll();
	}

	@Override
	public List<RestitutionType> getGroupList(ReturnBackType type) throws DoSthException {
		return this.restitutionTypeRepository.getNormalList(type, UsingStatus.ENABLE);
	}
}