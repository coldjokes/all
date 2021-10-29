package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.common.dto.MatAssociationInfo;
import com.dosth.tool.entity.MatCategory;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.util.OpTip;

/**
 * 物料关联设置Service
 * 
 * @author chenlei
 */
public interface MatCategoryService extends BaseService<MatCategoryTree> {

	/**
	 * @description 创建tree
	 */
	public List<ZTreeNode> categoryTree() throws DoSthException;

	/**
	 * @description 增加节点
	 */
	public MatCategoryTree nodeAdd(String pId, String level, String name, String path) throws DoSthException;

	/**
	 * @description 更改节点名
	 */
	public void editName(String nodeId, String name) throws DoSthException;

	/**
	 * @description 删除节点
	 */
	public void nodeDel(String nodeId) throws DoSthException;

	/**
	 * @description 过滤物料信息
	 */
	public List<MatCategory> dataFilter(String accountId, String typeId) throws DoSthException;

	/**
	 * @description 获取选中项目
	 */
	public List<MatAssociationInfo> getDipCheck(String typeTreeId) throws DoSthException;

	/**
	 * 下架确认
	 * 
	 * @param matIds
	 * @return
	 */
	public OpTip matCheck(String matIds);

	/**
	 * 根据物料ID删除物料关联信息
	 * 
	 * @param matId
	 */
	public void deleteBymatId(String matId);
	
	/**
	 * @description 校验名称是否重复
	 * @param categoryTreePId 上级Id
	 * @param categoryTreeId 本级Id
	 * @param newName 新名称
	 * @return
	 */
	public com.cnbaosi.dto.OpTip checkName(String categoryTreePId, String categoryTreeId, String newName);
}