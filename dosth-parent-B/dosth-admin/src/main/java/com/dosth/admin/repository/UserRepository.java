package com.dosth.admin.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.User;
import com.dosth.common.db.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User, String> {
	/**
	 * 通过账户Id获取用户信息
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("select u from User u where u.accountId = ?1 and u.account.status = 'OK'")
	public User findUserByAccountId(String accountId);

	/**
	 * 通过icCard获取用户信息
	 * 
	 * @param icCard
	 * @return
	 */
	@Query("select u from User u where u.icCard = ?1 and u.account.status = 'OK'")
	public User findUserByIcCard(String icCard);

	/**
	 * 通过icCard获取帐号信息
	 * 
	 * @param icCard
	 * @return
	 */
	@Query("select account from User u where u.icCard = ?1 and u.account.status = 'OK'")
	public Account findAccountByIcCard(String icCard);

	/**
	 * 通过部门Id获取用户信息
	 * 
	 * @param deptId
	 * @return
	 */
	@Query("select u from User u where u.deptId = ?1 and u.account.status = 'OK'")
	public List<User> findUserByDeptId(String deptId);

	/**
	 * 根据人脸特征获取用户信息
	 * 
	 * @param faceFeature
	 * @return
	 */
	@Query("select u from User u where u.faceFeature = ?1 and u.account.status = 'OK'")
	public User findUserByFaceFeature(byte[] faceFeature);

	/**
	 * 根据员工帐号获取帐号信息
	 * 
	 * @param loginName
	 * @return
	 */
	@Query("select a from Account a where a.loginName = ?1 and a.status = 'OK'")
	public List<Account> findAccountByLoginName(String loginName);

	/**
	 * @description 删除用户信息(除管理员与超级管理员)
	 */
	@Modifying
	@Transactional
	@Query("delete from User where id <> '1' and id <> '101'")
	public void clearUser();
}