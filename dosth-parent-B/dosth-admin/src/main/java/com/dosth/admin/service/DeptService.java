package com.dosth.admin.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.admin.FeignDept;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Dept;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.toolcabinet.dto.DeptInfo;
import com.dosth.util.OpTip;

/**
 * 部门Service
 * 
 * @author liweifeng
 *
 */
public interface DeptService extends BaseService<Dept> {
	/**
	 * 创建部门树
	 * 
	 * @param paramMap 条件集合
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public List<ZTreeNode> tree(Map<String, String> paramMap) throws BusinessException;

	/**
	 * 分页数据
	 * 
	 * @param pageNo        当前页码
	 * @param pageSize      每页大小
	 * @param name          部门名称
	 * @param managerStatus 部门状态
	 * @return
	 * @throws BusinessException
	 */
	public Page<Dept> getPage(int pageNo, int pageSize, String name) throws BusinessException;

	/**
	 * 根据部门名称获取部门列表
	 * 
	 * @param deptName
	 * @return
	 */
	public Dept findDeptByDeptName(String deptName);

	/**
	 * 获取部门信息
	 * 
	 * @return
	 */
	public List<DeptInfo> getDeptInfo();

	/**
	 * @description 同步部门信息
	 * @param deptList 部门信息列表
	 * @return 同步状态
	 */
	public OpTip syncFeignDept(List<FeignDept> deptList);

	/**
	 * @description 保存外部部门信息
	 * @param feignDept
	 * @return
	 */
	public Dept saveFeignDept(FeignDept feignDept);

	/**
	 * 根据部门ID查询子部门
	 * 
	 * @param pId
	 * @return
	 */
	List<Dept> getDeptByPid(String pId);

}