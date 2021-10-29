package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.vo.ViewUser;

/**
 *
 * @description 副柜帐户关系持久化层
 * @author guozhidong
 *
 */
public interface SubBoxAccountRefRepository extends BaseRepository<SubBoxAccountRef, String> {

	/**
	 * @description 根据帐户Id获取使用中的副柜列表
	 * @param accountId
	 * @return
	 */
	@Query("select r.subBox from SubBoxAccountRef r where r.accountId = :accountId")
	public List<SubBox> getUsingSubBoxList(String accountId);
	
	/**
	 * @description 根据副柜Id获取账户
	 * @param subBoxId
	 * @return
	 */
	@Query("select user from SubBoxAccountRef r where r.subBoxId = :subBoxId")
	public List<ViewUser> getUserList(String subBoxId);
	
	/**
	 * @description 根据Id获取用户
	 * @param subCabinetId
	 * @return
	 */
	@Query("select new SubBoxAccountRef(b, r.user) from SubBoxAccountRef r right join r.subBox b  where b.id = :equSettingId")
	public List<SubBoxAccountRef> getSubBoxBySubCabinetId(String equSettingId);
	
	/**
	 * @description 根据副柜Id副柜账户关系
	 * @param subBoxId
	 * @return
	 */
	@Query("from SubBoxAccountRef r where r.subBoxId = :subBoxId")
	public SubBoxAccountRef getAccountBySubBoxId(String subBoxId);
}