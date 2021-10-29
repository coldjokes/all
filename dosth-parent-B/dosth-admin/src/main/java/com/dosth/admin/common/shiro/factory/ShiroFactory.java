package com.dosth.admin.common.shiro.factory;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.shiro.IShiro;
import com.dosth.admin.constant.factory.ConstantFactory;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.Menu;
import com.dosth.admin.repository.AccountRepository;
import com.dosth.admin.repository.AccountRoleRepository;
import com.dosth.admin.repository.RelationRepository;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.util.SpringContextHolder;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class ShiroFactory implements IShiro {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private RelationRepository relationRepository;

	@Autowired
	private AccountRoleRepository accountRoleRepository;

	public static IShiro me() {
		return SpringContextHolder.getBean(IShiro.class);
	}

	@Override
	public Account account(String loginName) {
		List<Account> accountList = this.accountRepository.getByLoginName(loginName);
		// 账户不存在
		if (accountList == null || accountList.size() < 1) {
			throw new CredentialsException();
		}
		for (Account account : accountList) {
			// 正常的账户
			if (ManagerStatus.OK.equals(account.getStatus())) {
				return account;
			}
		}
		// 不存在启用的账户
		throw new LockedAccountException();
	}

	@Override
	public ShiroAccount shiroAccount(Account account) {
		ShiroAccount shiroAccount = new ShiroAccount();
		// 账户Id
		shiroAccount.setId(account.getId());
		// 账户登录名
		shiroAccount.setLoginName(account.getLoginName());
		// 设置用户部门Id
		shiroAccount.setDeptId(account.getUser().getDeptId());
		// 设置用户部门名称
		shiroAccount.setDeptName(ConstantFactory.me().getDeptName(account.getUser().getDeptId()));
		// 用户姓名
		shiroAccount.setName(account.getUser().getName());

		List<String> roleArray = this.accountRoleRepository.getRoleIdListByAccountId(account.getId());
		List<String> roleList = new ArrayList<>();
		List<String> roleNameList = new ArrayList<>();
		for (String roleId : roleArray) {
			roleList.add(roleId);
			roleNameList.add(ConstantFactory.me().getSingleRoleName(roleId));
		}
		shiroAccount.setRoleList(roleList);
		shiroAccount.setRoleNames(roleNameList);
		return shiroAccount;
	}

	@Override
	public List<String> findPermissionsByRoleId(String roleId) {
		List<String> resUrls = new ArrayList<>();
		List<Menu> list = this.relationRepository.getMenuListByRoleId(roleId);
		List<Menu> menuList = new ArrayList<>();
		for (Menu menu : list) {
			this.addMenuChildren(menuList, menu);
		}
		for (Menu menu : menuList) {
			resUrls.add(menu.getUrl());
		}
		return resUrls;
	}

	/**
	 * 递归菜单
	 * 
	 * @param menuList
	 * @param menu
	 */
	private void addMenuChildren(List<Menu> menuList, Menu menu) {
		menuList.add(menu);
		for (Menu child : menu.getChildList()) {
			addMenuChildren(menuList, child);
		}
	}

	@Override
	public String findRoleNameByRoleId(String roleId) {
		return ConstantFactory.me().getSingleRoleTip(roleId);
	}

	@Override
	public SimpleAuthenticationInfo info(ShiroAccount shiroAccount, Account account, String realmName) {
		String credentials = account.getPassword();
		// 密码加盐处理
		String source = account.getSalt();
		ByteSource credentialsSalt = new Md5Hash(source);
		return new SimpleAuthenticationInfo(shiroAccount, credentials, credentialsSalt, realmName);
	}
}