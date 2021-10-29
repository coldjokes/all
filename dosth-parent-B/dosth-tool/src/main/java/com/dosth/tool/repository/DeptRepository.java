package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.vo.ViewDept;

public interface DeptRepository extends BaseRepository<ViewDept, String> {

	/**
	 * @description 获取子级部门
	 * @param deptId 部门Id
	 */
	@Query("from ViewDept d where d.deptPId = :deptId")
	public List<ViewDept> getChildDeptList(String deptId);
}