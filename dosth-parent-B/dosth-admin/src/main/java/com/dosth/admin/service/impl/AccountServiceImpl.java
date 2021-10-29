package com.dosth.admin.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.admin.constant.Const;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.entity.User;
import com.dosth.admin.repository.AccountRepository;
import com.dosth.admin.repository.DeptRepository;
import com.dosth.admin.repository.UserRepository;
import com.dosth.admin.service.AccountService;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.ToolUtil;

/**
 * 账户Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DeptRepository deptRepository;

	@Override
	public void registAccount(String loginName, String password) throws BusinessException {
		Account account = new Account();
		account.setLoginName(loginName);
		account.setSalt(ShiroKit.getRandomSalt(5));
		account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
		account.setFacePwd(FaceVerificationUtil.faceEncode(Const.DEFAULT_PWD, account.getSalt()));
		User user = new User();
		account.setUser(user);
		save(account);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<Account> getByLoginName(String loginName) throws BusinessException {
		Criteria<Account> criteria = new Criteria<Account>();
		if (loginName != null && !"".equals(loginName)) {
			criteria.add(Restrictions.eq("loginName", loginName, true));
		}
		return  this.accountRepository.findAll(criteria);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Account get(Serializable accountId) throws BusinessException {
		return this.accountRepository.findOne(accountId);
	}

	@Override
	public Account update(Account account) throws BusinessException {
		return this.accountRepository.saveAndFlush(account);
	}

	@Override
	public void save(Account account, String deptId) throws BusinessException {
		this.accountRepository.save(account);
		User user = new User();
		user.setAccountId(account.getId());
		user.setName(account.getLoginName());
		user.setDeptId(deptId);
		this.userRepository.save(user);
	}

	@Override
	public Page<Account> getPage(int pageNo, int pageSize, String deptid, String name) throws BusinessException {
		Criteria<Account> criteria = new Criteria<Account>();
		criteria.add(Restrictions.eq("status", ManagerStatus.OK, true));
		if (ToolUtil.isAllEmpty(deptid)) {
			criteria.add(Restrictions.eq("user.deptId", deptid, false));
		}
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("loginName", name, true));
		}
		return this.accountRepository.findAll(criteria, new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "status")));
	}

	@Override
	public void save(Account account) throws DoSthException {
		this.accountRepository.save(account);
	}

	@Override
	public void delete(Account account) throws DoSthException {
		update(account);
	}

	@Override
	public List<ZTreeNode> createAccountTree() throws BusinessException {
		List<ZTreeNode> tree = new ArrayList<>();
		Criteria<Dept> c1 = new Criteria<>();
		c1.add(Restrictions.eq("status", ManagerStatus.OK, true));
		List<Dept> deptList = this.deptRepository.findAll(c1);
		for (Dept dept : deptList) {
			tree.add(new ZTreeNode("d_" + dept.getId(), "d_" + (dept.getpId() != null ? dept.getpId() : dept.getId()), dept.getDeptName()));
		}
		List<Account> accountList = this.accountRepository.getAccountList(ManagerStatus.OK);
		for (Account account : accountList) {
			tree.add(new ZTreeNode(String.valueOf(account.getId()), "d_" + account.getDeptId(), account.getUserName() + "[" + account.getLoginName() + "]"));
		}
		return tree;
	}

	@Override
	public List<String> getAccountIdListByDeptId(String deptId) {
		List<String> accountIdList = new ArrayList<>();
		Dept curDept = this.deptRepository.getOne(deptId);
		getAccountIdListByDeptId(curDept, accountIdList);
		return accountIdList;
	}
	
	/**
	 * @description 递归部门下所有账户Id
	 * @param deptId 部门Id
	 * @param accountIdList 账户Id集合
	 */
	private void getAccountIdListByDeptId(Dept curDept, List<String> accountIdList) {
		if (ManagerStatus.OK.equals(curDept.getStatus())) {
			accountIdList.addAll(this.accountRepository.getAccountIdByDeptId(curDept.getId()));
			List<Dept> deptList = this.deptRepository.getChildDeptList(curDept.getId());
			for (Dept dept : deptList) {
				getAccountIdListByDeptId(dept, accountIdList);
			}
		}
	}
}