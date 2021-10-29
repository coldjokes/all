package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.repository.SubBoxRepository;
import com.dosth.tool.service.SubBoxService;

/**
 * 副柜盒子Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class SubBoxServiceImpl implements SubBoxService {

	@Autowired
	private SubBoxRepository subBoxRepository;

	@Override
	public void save(SubBox subBox) throws DoSthException {
		this.subBoxRepository.save(subBox);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public SubBox get(Serializable id) throws DoSthException {
		return this.subBoxRepository.getOne(id);
	}

	@Override
	public SubBox update(SubBox subBox) throws DoSthException {
		return this.subBoxRepository.saveAndFlush(subBox);
	}

	@Override
	public void delete(SubBox subBox) throws DoSthException {
		this.subBoxRepository.delete(subBox);
	}

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public List<SubBox> getSubBoxList(String subCardId) throws DoSthException {
		return this.subBoxRepository.getSubBoxListByCabinetId(subCardId);
	}

	@Override
	public List<SubBox> getUnUsedSubBoxList() throws DoSthException {
		return this.subBoxRepository.getUnUsedSubBoxList();
	}

	@Override
	public Integer getSubBoxNum() throws DoSthException {
		return this.subBoxRepository.getSubBoxNum();
	}

	@Override
	public Page<SubBox> getPage(int pageNo, int pageSize, String equSettingId) {
		Criteria<SubBox> criteria = new Criteria<>();
		if (equSettingId != null && !"".equals(equSettingId)) {
			criteria.add(Restrictions.eq("equSettingId", equSettingId, true));
		}

		List<SubBox> list = this.subBoxRepository.findAll(criteria);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}
}