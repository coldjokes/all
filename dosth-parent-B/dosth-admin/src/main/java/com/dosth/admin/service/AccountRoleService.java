package com.dosth.admin.service;

import java.util.List;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.AccountRole;

/**
 * 账户角色关系Service
 * 
 * @author guozhidong
 *
 */
public interface AccountRoleService {

	/**
	 * 更新角色
	 * 
	 * @param accountId
	 * @param roleIds
	 * @throws BusinessException
	 */
	public void updateRoles(String accountId, String roleIds) throws BusinessException;

	/**
	 * 根据账户Id获取角色Id集合
	 * 
	 * @param accountId 账户Id
	 * @return
	 * @throws BusinessException
	 */
	public List<String> getRoleIdListByAccountId(String accountId) throws BusinessException;

	/**
	 * 根据帐号ID获取用户角色
	 * 
	 * @param accountId
	 * @return
	 */
	public AccountRole getUserRole(String accountId);
}