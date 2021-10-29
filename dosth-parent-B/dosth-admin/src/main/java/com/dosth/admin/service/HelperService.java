package com.dosth.admin.service;

import java.util.List;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.mobile.Helper;
import com.dosth.common.servcie.BaseService;

/**
 * 
 * @description 帮助中心Service
 * @author guozhidong
 *
 */
public interface HelperService extends BaseService<Helper> {
	/**
	 * @description 获取帮助中心列表
	 * @return
	 * @throws BusinessException
	 */
	public List<Helper> getHelperList() throws BusinessException;
}