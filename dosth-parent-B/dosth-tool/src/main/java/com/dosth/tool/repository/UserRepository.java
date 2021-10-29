package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.vo.ViewUser;

public interface UserRepository extends BaseRepository<ViewUser, String> {

	/**
	 * 通过账户Id获取用户信息
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("select u from ViewUser u where u.accountId = ?1")
	public ViewUser findUserByAccountId(String accountId);
	
	/**
	 * 通过用户名获取用户信息
	 * 
	 * @param accountId
	 * @return
	 */
	@Query("select u from ViewUser u where u.userName =:userName")
	public List<ViewUser> findUserByUserName(String userName);
}