package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.MatReturnDetail;

/**
 * @description 归还详情定义Service
 * 
 * @author chenlei
 *
 */
public interface MatReturnDetailService extends BaseService<MatReturnDetail> {

	/**
	 * @description 获取归还ID
	 * @return
	 * @throws DoSthException
	 */
	public List<String> getReturnIds(String typeId) throws DoSthException;
}