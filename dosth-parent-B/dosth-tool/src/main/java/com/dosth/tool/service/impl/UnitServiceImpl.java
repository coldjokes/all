package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.dosth.tool.entity.Unit;
import com.dosth.tool.repository.UnitRepository;
import com.dosth.tool.service.UnitService;

/**
 * 单位管理Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService {

	@Autowired
	private UnitRepository unitRepository;
	
	@Override
	public void save(Unit unit) throws DoSthException {
		this.unitRepository.save(unit);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Unit get(Serializable id) throws DoSthException {
		Unit unit = this.unitRepository.findOne(id);
		return unit;
	}

	@Override
	public Unit update(Unit unit) throws DoSthException {
		return this.unitRepository.saveAndFlush(unit);
	}

	@Override
	public void delete(Unit unit) throws DoSthException {
		unit.setStatus(UsingStatus.DISABLE);
		update(unit);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<Unit> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException {
		Criteria<Unit> criteria = new Criteria<Unit>();
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("unitName", name.trim(), true));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}
		return this.unitRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> tree(Map<String, String> params) throws DoSthException {
		Criteria<Unit> criteria = new Criteria<Unit>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<Unit> list = this.unitRepository.findAll(criteria);
		List<ZTreeNode> treeNode = new ArrayList<>();
		for (Unit unit : list) {
			treeNode.add(new ZTreeNode(String.valueOf(unit.getId()), String.valueOf(unit.getId()), unit.getUnitName()));
		}
		return treeNode;
	}
}