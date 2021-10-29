package com.dosth.admin.rpc.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.repository.AccountRepository;
import com.dosth.admin.repository.DeptRepository;
import com.dosth.admin.repository.UserRepository;
import com.dosth.admin.rpc.AdminRpcService;
import com.dosth.admin.service.UserService;
import com.dosth.admin.util.face.FaceVerificationUtil;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.node.ZTreeNode;
import com.dosth.toolcabinet.dto.AccountUserInfo;

/**
 * @description 用户管理远程接口方法实现
 * @Author guozhidong
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class AdminRpcServiceImpl implements AdminRpcService {

	@Autowired
	private UserService userService;;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private DeptRepository deptRepository;

	@Override
	public AccountUserInfo getAccountUserInfoByCardStr(String cardStr) {
		Account account = this.userRepository.findAccountByIcCard(cardStr);
		if (account != null) {
			return new AccountUserInfo(account.getId(), account.getLoginName(),
					FaceVerificationUtil.faceDecode(account.getFacePwd(), account.getSalt()),
					account.getUser().getDept().getId(), account.getUser().getDept().getDeptName(),
					account.getUser().getId(), account.getUser().getName());
		}
		return null;
	}

	@Override
	public AccountUserInfo checkUserPwd(String loginName, String pwd) {
		List<Account> accountList = this.accountRepository.getAccountsByLoginName(loginName);
		// 添加有效账户过滤
		accountList = accountList.stream().filter(account -> ManagerStatus.OK.equals(account.getStatus())).collect(Collectors.toList());
		if (accountList != null && accountList.size() > 0) {
			Account account = accountList.get(0);
			if (pwd.equals(FaceVerificationUtil.faceDecode(account.getFacePwd(), account.getSalt()))) {
				return new AccountUserInfo(account.getId(), account.getLoginName(),
						FaceVerificationUtil.faceEncode(pwd, account.getSalt()), account.getUser().getDept().getId(),
						account.getUser().getDept().getDeptName(), account.getUser().getId(),
						account.getUser().getName());
			}
		}
		return null;
	}

	@Override
	public List<ZTreeNode> initAccountTree() {
		List<ZTreeNode> tree = new ArrayList<>();
		Criteria<Dept> c1 = new Criteria<>();
		c1.add(Restrictions.eq("status", ManagerStatus.OK, true));
		List<Dept> deptList = this.deptRepository.findAll(c1);
		ZTreeNode node;
		for (Dept dept : deptList) {
			node=new ZTreeNode("d_" + dept.getId(), "d_" + (dept.getpId() != null ? dept.getpId() : dept.getId()),
					dept.getDeptName());
			node.setLevel(0);
			node.setIcon("/static/img/part.png");
			if (dept.getpId() == null || "".equals(dept.getpId())) {
				node.setIcon("/static/img/comp.png");
			}
			tree.add(node);
		}
		List<Account> accountList = this.accountRepository.getAccountList(ManagerStatus.OK);
		for (Account account : accountList) {
			if (account.getLoginName().equals("administrator") || account.getUserName().equals("administrator")) {
				continue;
			}
			node=new ZTreeNode(String.valueOf(account.getId()), "d_" + account.getDeptId(),
					account.getUserName() + "[" + account.getLoginName() + "]");
			node.setLevel(1);
			node.setIcon("/static/img/person.png");
			tree.add(node);
		}
		tree.sort((tree1, tree2) -> tree2.getLevel() - tree1.getLevel());
		return tree;
	}

	@Override
	public boolean icBind(String accountId, String cardNo) {
		return this.userService.icBind(accountId, cardNo);
	}

	@Override
	public boolean faceBind(String accountId, String file) {
		return this.userService.faceBind(accountId, file);
	}

	@Override
	public AccountUserInfo faceLogin(String file) {
		return this.userService.faceLogin(file);
	}
}