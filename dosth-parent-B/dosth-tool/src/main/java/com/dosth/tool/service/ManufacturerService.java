package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignSupplier;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.Manufacturer;

/**
 * 供货商Service
 * 
 * @author guozhidong
 *
 */
public interface ManufacturerService extends BaseService<Manufacturer> {

	/**
	 * 分页方法
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param name
	 *            名称/联系人/联系电话
	 * @param status
	 *            是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<Manufacturer> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException;

	/**
	 * 创建供货商Tree
	 * 
	 * @param params
	 * @return
	 * @throws DoSthException
	 */
	public List<ZTreeNode> tree(Map<String, String> params) throws DoSthException;

	/**
	 * @description 供应商同步
	 * @param supplierList 供应商列表
	 * @return
	 */
	public OpTip syncSupplier(List<FeignSupplier> supplierList);

	/**
	 * @description 更新供应商或联系人状态
	 * @param type 供应商或联系人 1 供应商 2 联系人
	 * @param manufacturerId 供应商或联系人Id
	 * @param status 状态 1 启用 0 停用
	 */
	public Tip updateStatus(String type, String manufacturerId, String status);
}