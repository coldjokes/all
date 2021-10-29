package com.dosth.admin.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.common.db.repository.BaseRepository;

/**
 * @description 帐户持久化层
 * @author guozhidong
 *
 */
public interface AccountRepository extends BaseRepository<Account, String> {

	/**
	 * @description 根据登录名获取账户列表
	 * 
	 * @param loginName 登录名
	 * @return
	 */
	@Query("from Account a where a.loginName = ?1")
	public List<Account> getByLoginName(String loginName);

	/**
	 * @description 获取账户列表
	 * 
	 * @return
	 */
	@Query("select new Account(a.id, a.user.deptId, a.loginName, a.user.name) from Account a where a.status =:status")
	public List<Account> getAccountList(ManagerStatus status);

	/**
	 * @description 根据登录名和密码获取帐户列表
	 * @param loginName 登录名
	 * @return
	 */
	@Query("from Account where loginName = :loginName")
	public List<Account> getAccountsByLoginName(String loginName);

	/**
	 * @description 获取部门下有效的账户Id
	 * @param deptId 部门Id
	 * @return
	 */
	@Query("select a.id from Account a where a.status = 'OK' and a.user.deptId = :deptId")
	public List<String> getAccountIdByDeptId(String deptId);

	/**
	 * @description 清除账户信息(除管理员与超级管理员)
	 */
	@Modifying
	@Transactional
	@Query("delete from Account where id <> '1' and id <> '101'")
	public void clearByUser();
}