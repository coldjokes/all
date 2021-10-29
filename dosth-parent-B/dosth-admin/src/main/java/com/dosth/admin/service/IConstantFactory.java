package com.dosth.admin.service;

import java.util.List;

import com.dosth.admin.entity.Dict;

/**
 * 常用生产工厂的接口
 * 
 * @author guozhidong
 *
 */
public interface IConstantFactory {

	/**
	 * 根据用户Id获取用户名称
	 * 
	 * @param userId
	 * @return
	 */
	public String getLoginNameById(Long userId);

	/**
	 * 根据用户Id获取用户账号
	 * 
	 * @param userId
	 * @return
	 */
	public String getUserAccountById(Long userId);

	/**
	 * 通过账户Id获取角色名称
	 * 
	 * accountId 账户Id
	 * 
	 * @return
	 */
	public String getRoleName(Long accountId);

	/**
	 * 通过角色id获取角色名称
	 * 
	 * @param roleId
	 * @return
	 */
	public String getSingleRoleName(Long roleId);

	/**
	 * 通过角色id获取角色英文名称
	 * 
	 * @param roleId
	 * @return
	 */
	public String getSingleRoleTip(Long roleId);

	/**
	 * 获取部门名称
	 * 
	 * @param deptId
	 * @return
	 */
	public String getDeptName(Long deptId);

	/**
	 * 获取菜单的名称(多个)
	 * 
	 * @param menuIds
	 * @return
	 */
	public String getMenuNames(String menuIds);

	/**
	 * 获取菜单的名称
	 * 
	 * @param menuId
	 * @return
	 */
	public String getMenuName(Long menuId);

	/**
	 * 通过编号获取菜单名称
	 * 
	 * @param code
	 * @return
	 */
	public String getMenuNameByCode(String code);

	/**
	 * 获取字典名称
	 * 
	 * @param dictId
	 * @return
	 */
	public String getDictName(Long dictId);

	/**
	 * 获取通知标题
	 * 
	 * @param dictId
	 * @return
	 */
	public String getNoticeTitle(Long dictId);

	/**
	 * 根据字典名称和字典中的值获取对应的名称
	 * 
	 * @param name
	 * @param val
	 * @return
	 */
	public String getDictsByName(String name, Integer val);

	/**
	 * 查询字典
	 * 
	 * @param id
	 * @return
	 */
	public List<Dict> findInDict(Long id);

	/**
	 * 获取被缓存的对象(用户删除业务)
	 * 
	 * @param para
	 * @return
	 */
	public String getCacheObject(String para);

	/**
	 * 获取子部门Id
	 * 
	 * @param deptId
	 * @return
	 */
	public List<Long> getSubDeptId(Long deptId);

	/**
	 * 获取所有父部门Id
	 * 
	 * @param deptId
	 * @return
	 */
	public List<Long> getParentDeptIds(Long deptId);
}