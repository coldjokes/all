package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.toolcabinet.enums.ReturnBackType;

/**
 * @description 归还类型定义Service
 * 
 * @author liweifeng
 *
 */
public interface RestitutionTypeService extends BaseService<RestitutionType> {

	/**
	 * @Desc 创建tree[归还类型]
	 */
	public List<ZTreeNode> reTypeTree() throws DoSthException;

	/**
	 * @description 分页方法
	 * 
	 * @param pageNo   当前页码
	 * @param pageSize 每页大小
	 * @param name     名称
	 * @param status   是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<RestitutionType> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException;
	
	/**
	 * @description 获取归还类型列表
	 * @return
	 * @throws DoSthException
	 */
	public List<RestitutionType> getReTypeList() throws DoSthException;
	
	/**
	 * @description 归还类型列表
	 * @return
	 * @throws DoSthException
	 */
	public List<RestitutionType> getGroupList(ReturnBackType type) throws DoSthException;
}