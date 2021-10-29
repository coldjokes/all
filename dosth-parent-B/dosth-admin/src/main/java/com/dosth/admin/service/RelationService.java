package com.dosth.admin.service;

import java.util.List;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.common.node.MenuNode;

/**
 * 角色菜单关系Service
 * 
 * @author guozhidong
 *
 */
public interface RelationService {

	/**
	 * 根据角色Id获取菜单节点
	 * 
	 * @param roleIdList
	 *            角色Id集合
	 * @return
	 * @throws BusinessException
	 */
	public List<MenuNode> getMenusByRoleIds(List<String> roleIdList) throws BusinessException;
}