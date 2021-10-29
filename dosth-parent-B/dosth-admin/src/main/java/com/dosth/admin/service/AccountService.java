package com.dosth.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Account;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;

/**
 * 账户Service
 * 
 * @author guozhidong
 *
 */
public interface AccountService extends BaseService<Account> {
	/**
	 * 注册新用户
	 * 
	 * @param loginName
	 *            登录名
	 * @param password
	 *            密码
	 * @throws BusinessException
	 */
	public void registAccount(String loginName, String password) throws BusinessException;

	/**
	 * 根据登录名获取账户列表
	 * 
	 * @param loginName
	 *            登录名
	 * @return
	 */
	public List<Account> getByLoginName(String loginName) throws BusinessException;

	/**
	 * 添加账户信息
	 * 
	 * @param account
	 * @param deptId
	 *            当前操作人所属组织
	 * @throws BusinessException
	 */
	public void save(Account account, String deptId) throws BusinessException;

	/**
	 * 分页数据
	 * 
	 * @param pageNo
	 *            当前页码
	 * @param pageSize
	 *            每页大小
	 * @param deptid
	 *            部门Id
	 * @param name
	 *            账户名称
	 * @param managerStatus
	 *            账户状态
	 * @return
	 * @throws BusinessException
	 */
	public Page<Account> getPage(int pageNo, int pageSize, String deptid, String name) throws BusinessException;

	/**
	 * 创建账户树
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public List<ZTreeNode> createAccountTree() throws BusinessException;

	/**
	 * @description 根据部门Id获取部门下所有员工
	 * @param deptId 部门Id
	 */
	public List<String> getAccountIdListByDeptId(String deptId);
}