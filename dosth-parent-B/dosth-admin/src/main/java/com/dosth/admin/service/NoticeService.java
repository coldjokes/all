package com.dosth.admin.service;

import java.util.List;
import java.util.Map;

import com.dosth.admin.common.exception.BusinessException;

/**
 * 通知Service
 * 
 * @author guozhidong
 *
 */
public interface NoticeService {

	/**
	 * 通过条件查询通知列表
	 * 
	 * @param condition
	 *            查询条件
	 * @return
	 * @throws BusinessException
	 */
	public List<Map<String, Object>> list(Map<String, Object> condition) throws BusinessException;
}