package com.dosth.admin.service;

import java.util.List;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Menu;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;

/**
 * 菜单Service
 * 
 * @author guozhidong
 *
 */
public interface MenuService extends BaseService<Menu> {

	/**
	 * 根据角色Id获取菜单Id集合
	 * 
	 * @param roleId
	 * @return
	 * @throws BusinessException
	 */
	public List<String> getMenuIdsByRoleId(String roleId) throws BusinessException;

	/**
	 * 获取菜单树集合
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public List<ZTreeNode> menuTreeList() throws BusinessException;

	/**
	 * 根据菜单Id集合获取菜单树
	 * 
	 * @param menuIds
	 * @return
	 * @throws BusinessException
	 */
	public List<ZTreeNode> menuTreeListByMenuIds(List<String> menuIds) throws BusinessException;
}