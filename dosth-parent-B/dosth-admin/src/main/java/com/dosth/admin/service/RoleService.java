package com.dosth.admin.service;

import java.util.List;
import java.util.Map;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Roles;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;

/**
 * 角色Service
 * 
 * @author guozhidong
 *
 */
public interface RoleService extends BaseService<Roles> {
	/**
	 * 创建角色树
	 * 
	 * @param paramMap
	 *            条件集合
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public List<ZTreeNode> tree(Map<String, String> paramMap) throws BusinessException;

	/**
	 * 是否存在子级角色
	 * 
	 * @param pId
	 * @return
	 * @throws BusinessException
	 */
	public boolean isHaveChildren(Long pId) throws BusinessException;

	/**
	 * 添加角色信息
	 * 
	 * @param Role
	 *            角色
	 * @param menuIds
	 *            权限菜单集合
	 * @throws BusinessException
	 */
	public void save(Roles Role, String menuIds) throws BusinessException;

	/**
	 * 添加角色信息
	 * 
	 * @param Role
	 *            角色
	 * @param menuIds
	 *            权限菜单集合
	 * @throws BusinessException
	 */
	public Roles update(Roles Role, String menuIds) throws BusinessException;

	/**
	 * 创建角色树
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public List<ZTreeNode> createRoleTree() throws BusinessException;
}