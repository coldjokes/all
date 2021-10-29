package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.Unit;

/**
 * 单位管理Service
 * 
 * @author guozhidong
 *
 */
public interface UnitService extends BaseService<Unit> {
	/**
	 * 分页方法
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param name
	 *            名称
	 * @param status
	 *            是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<Unit> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException;

	/**
	 * 创建单位树
	 * 
	 * @param params
	 * @return
	 * @throws DoSthException
	 */
	public List<ZTreeNode> tree(Map<String, String> params) throws DoSthException;
}