package com.dosth.tool.service.impl;

import java.io.Serializable;
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
import com.dosth.dto.ExtraBoxNum;
import com.dosth.tool.entity.ExtraBoxNumSetting;
import com.dosth.tool.repository.ExtraBoxNumSettingRepository;
import com.dosth.tool.service.ExtraBoxNumSettingService;

@Service
@Transactional
public class ExtraBoxNumSettingServiceImpl implements ExtraBoxNumSettingService {

	@Autowired
	private ExtraBoxNumSettingRepository extraBoxNumSettingRepository;

	@Override
	public void save(ExtraBoxNumSetting extraBoxNumSetting) throws DoSthException {
		this.extraBoxNumSettingRepository.save(extraBoxNumSetting);
	}

	@Override
	public ExtraBoxNumSetting update(ExtraBoxNumSetting extraBoxNumSetting) throws DoSthException {
		return this.extraBoxNumSettingRepository.saveAndFlush(extraBoxNumSetting);
	}

	@Override
	public void delete(ExtraBoxNumSetting extraBoxNumSetting) throws DoSthException {
		this.extraBoxNumSettingRepository.delete(extraBoxNumSetting);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public ExtraBoxNumSetting get(Serializable id) throws DoSthException {
		ExtraBoxNumSetting extraBoxNumSetting = this.extraBoxNumSettingRepository.findOne(id);
		return extraBoxNumSetting;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public String getAccount(String accountId) throws DoSthException {
		String id = this.extraBoxNumSettingRepository.findUserByAccountId(accountId);
		return id;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<ExtraBoxNumSetting> getPage(int pageNo, int pageSize, String userName) throws DoSthException {
		Criteria<ExtraBoxNumSetting> criteria = new Criteria<ExtraBoxNumSetting>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		if (userName != null && !"".equals(userName)) {
			criteria.add(Restrictions.like("user.userName", userName, true));
		}
		Page<ExtraBoxNumSetting> page = this.extraBoxNumSettingRepository.findAll(criteria,
				new PageRequest(pageNo, pageSize));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.extraBoxNumSettingRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		}
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Integer getExtraBoxNumByAccountId(String accountId) {
		String extraBoxNum = this.extraBoxNumSettingRepository.getExtraBoxNumByAccountId(accountId);
		if (extraBoxNum != null && !"".equals(extraBoxNum)) {
			return Integer.valueOf(extraBoxNum);
		}
		return 0;
	}

	@Override
	public List<ExtraBoxNumSetting> findAll() {
		return this.extraBoxNumSettingRepository.findAll();
	}

	@Override
	public List<ExtraBoxNumSetting> getNumListByAccountId(String accountId) {
		Criteria<ExtraBoxNumSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		criteria.add(Restrictions.eq("accountId", accountId, true));
		List<ExtraBoxNumSetting> a = this.extraBoxNumSettingRepository.findAll(criteria);
		return a;
	}

	@Override
	public void updateExtraBoxNum(String accountId, String extraBoxNum) {
		List<ExtraBoxNumSetting> extraBoxNumList = this.getNumListByAccountId(accountId);
		if (extraBoxNumList != null && extraBoxNumList.size() > 0) {
			ExtraBoxNumSetting extra = extraBoxNumList.get(0);
			extra.setAccountId(accountId);
			extra.setExtraBoxNum(extraBoxNum);
			extra.setStatus(UsingStatus.ENABLE);
			this.extraBoxNumSettingRepository.saveAndFlush(extra);
		} else {
			ExtraBoxNumSetting extra = new ExtraBoxNumSetting();
			extra.setAccountId(accountId);
			extra.setExtraBoxNum(extraBoxNum);
			extra.setStatus(UsingStatus.ENABLE);
			this.extraBoxNumSettingRepository.save(extra);
		}
	}

	@Override
	public ExtraBoxNum getExtraBoxNum(String accountId) {
		ExtraBoxNum extraBoxNum = null;
		List<ExtraBoxNumSetting> extraBoxNumList = this.getNumListByAccountId(accountId);
		if (extraBoxNumList != null && extraBoxNumList.size() > 0) {
			ExtraBoxNumSetting extra = extraBoxNumList.get(0);
			extraBoxNum = new ExtraBoxNum(extra.getAccountId(), extra.getExtraBoxNum());
		}
		return extraBoxNum;
	}

	@Override
	public void delExtraBoxNum(String accountId) {
		List<ExtraBoxNumSetting> extraBoxNumList = this.getNumListByAccountId(accountId);
		if (extraBoxNumList != null && extraBoxNumList.size() > 0) {
			for (ExtraBoxNumSetting extraBoxNum : extraBoxNumList) {
				this.extraBoxNumSettingRepository.delete(extraBoxNum);
			}
		}
	}
}