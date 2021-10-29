package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.DeptBorrowPopedom;

/**
 * @description 部门权限持久化层
 * @author guozhidong
 *
 */
public interface DeptBorrowPopedomRepository extends BaseRepository<DeptBorrowPopedom, String> {
	
	/**
	 * @description 根据部门Id获取权限集合
	 * @param deptId 部门Id
	 * @return
	 */
	@Query("from DeptBorrowPopedom p where p.deptId = :deptId")
	public List<DeptBorrowPopedom> getPopedomListByDeptId(String deptId);
}