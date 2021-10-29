package com.dosth.admin.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.AccountRole;
import com.dosth.admin.repository.AccountRoleRepository;
import com.dosth.admin.service.AccountRoleService;

/**
 * 账户角色关系Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class AccountRoleServiceImpl implements AccountRoleService {

	@Autowired
	private AccountRoleRepository accountRoleRepository;

	@Override
	public void updateRoles(String accountId, String roleIds) throws BusinessException {
		List<AccountRole> accountRoleList = this.accountRoleRepository.getAccountRoleListByAccountId(accountId);
		for (AccountRole accountRole : accountRoleList) {
			this.accountRoleRepository.delete(accountRole);
		}
		String[] rolesId = roleIds.split(",");
		for (String roleId : rolesId) {
			this.accountRoleRepository.save(new AccountRole(accountId, roleId));
		}
	}

	@Override
	public List<String> getRoleIdListByAccountId(String accountId) throws BusinessException {
		return this.accountRoleRepository.getRoleIdListByAccountId(accountId);
	}

	@Override
	public AccountRole getUserRole(String accountId) {
		return this.accountRoleRepository.getUserRole(accountId);
	}
}