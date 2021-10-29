package com.dosth.admin.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.AccountRole;
import com.dosth.common.db.repository.BaseRepository;

public interface AccountRoleRepository extends BaseRepository<AccountRole, String> {

	/**
	 * 根据账户Id获取账户角色关系列表
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("from AccountRole ar where ar.accountId = ?1")
	public List<AccountRole> getAccountRoleListByAccountId(String accountId);

	/**
	 * 根据账户Id获取账户角色Id列表
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("select ar.roleId from AccountRole ar where ar.accountId = ?1")
	public List<String> getRoleIdListByAccountId(String accountId);

	/**
	 * 根据账户ID获取用户角色
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("from AccountRole role where role.accountId = ?1")
	public AccountRole getUserRole(String accountId);

	/**
	 * @description 清除账户角色关联(除管理员与超级管理员)
	 */
	@Modifying
	@Transactional
	@Query("delete from AccountRole where accountId <> '1' and accountId <> '101'")
	public void clearByUser();
}