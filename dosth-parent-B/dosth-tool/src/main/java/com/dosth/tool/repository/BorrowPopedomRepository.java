package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.BorrowPopedom;
import com.dosth.toolcabinet.enums.EnumBorrowType;

/**
 * 
 * @description 借出权限持久化层
 * @author guozhidong
 *
 */
public interface BorrowPopedomRepository extends BaseRepository<BorrowPopedom, String> {

	/**
	 * @description 根据帐户Id获取借出权限列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@Query("from BorrowPopedom p where p.accountId = :accountId")
	public List<BorrowPopedom> getPopedomList(String accountId);

	/**
	 * @description 根据帐户Id获取指定权限类型借出权限列表
	 * @param accountId  帐户Id
	 * @param borrowType 权限类型
	 * @return
	 */
	@Query("from BorrowPopedom p where p.accountId = :accountId and p.borrowPopedom = :borrowType")
	public List<BorrowPopedom> getPopedomListByBorrowType(String accountId, EnumBorrowType borrowType);

	/**
	 * 根据帐户Id获取借出权限Id集合
	 * 
	 * @param acconutId 帐户Id
	 * @return
	 */
	@Query("from BorrowPopedom b where b.accountId = ?1")
	public List<BorrowPopedom> getPopedomIds(String accountId);
}