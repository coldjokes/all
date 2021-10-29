package com.dosth.admin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.repository.NoticeRepository;
import com.dosth.admin.service.NoticeService;

/**
 * 通知Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeRepository noticeRepository;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public List<Map<String, Object>> list(Map<String, Object> condition) throws BusinessException {
		this.noticeRepository.findAll();
		return null;
	}
}