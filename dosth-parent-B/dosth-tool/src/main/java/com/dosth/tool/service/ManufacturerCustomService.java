package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.ManufacturerCustom;

/**
 * 供货商客服Service
 * 
 * @author liweifeng
 *
 */
public interface ManufacturerCustomService extends BaseService<ManufacturerCustom> {

	/**
	 * 分页方法
	 * 
	 * @param pageNo         当前页码
	 * @param pageSize       每页大小
	 * @param manufacturerId 供货商ID
	 * @param name           名称/联系人/联系电话
	 * @param status         是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<ManufacturerCustom> getPage(int pageNo, int pageSize, String manufacturerId, String name, String status)
			throws DoSthException;

	/**
	 * @description 根据供货商Id获取客服列表
	 * @param manufacturerId 供货商Id
	 * @return
	 * @throws DoSthException
	 */
	public List<ManufacturerCustom> getCustomListByManufacturerId(String manufacturerId) throws DoSthException;
}