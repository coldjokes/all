package com.dosth.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.Dept;
import com.dosth.common.db.repository.BaseRepository;

public interface DeptRepository extends BaseRepository<Dept, Long> {

	/**
	 * 根据部门Id获取子级部门列表
	 * 
	 * @param deptId
	 * @return
	 */
    @Query("select d from Dept d where d.fullIds like '%?1%' and d.status = 'OK'")
    public List<Dept> findDeptListByDeptId(String deptId);
    
    /**
	 * 根据部门名称获取部门信息
	 * 
	 * @param deptName
	 * @return
	 */
    @Query("select d from Dept d where d.deptName = ?1 and d.status = 'OK'")
    public Dept findDeptByDeptName(String deptName);

    /**
     * @description 获取部门Id子级部门
     * @param deptId 部门Id
     * @return
     */
    @Query("from Dept d where d.pId = :deptId")
	public List<Dept> getChildDeptList(String deptId);
    
    /**
	 * 根据部门名称获取部门列表信息
	 * 
	 * @param deptName 部门名称
	 * @return
	 */
    @Query("from Dept d where d.deptName = :deptName and d.status = 'OK'")
	public List<Dept> getDeptListByDeptName(String deptName);
    
    /**
	 * 根据部门编码获取部门列表信息
	 * 
	 * @param deptNo 部门编码
	 * @return
	 */
    @Query("from Dept d where d.deptNo = :deptNo and d.status = 'OK'")
	public List<Dept> getDeptListByDeptNo(String deptNo);

    @Query("from Dept d where d.pId = :pId and d.status = 'OK'")
	public List<Dept> getDeptByPid(String pId);
}