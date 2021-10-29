package com.dosth.admin.service;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.admin.constant.Const;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Account;
import com.dosth.admin.entity.User;
import com.dosth.admin.repository.UserRepository;

/**
 * 账户测试
 * 
 * @author guozhidong
 *
 */
public class AccoutServiceTest extends DosthApplicationTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void testAccount() {

		for (int i = 0; i < 97; i ++) {
			Account account = new Account();
			/**
			 * md5密码盐
			 */
			account.setSalt(ShiroKit.getRandomSalt(5));
			/**
			 * 账号
			 */
			account.setLoginName("admin" + account.getSalt());
			/**
			 * 密码
			 */
			account.setPassword(ShiroKit.md5(Const.DEFAULT_PWD, account.getSalt()));
			/**
			 * 状态(1：启用 2：冻结 3：删除）
			 */
			account.setStatus(ManagerStatus.OK);
	
			User user = new User();
			user.setName(account.getLoginName());
			user.setDeptId("1");
			user.setAccount(account);
			/**
			 * 创建时间
			 */
			user.setCreatetime(new Date());
			this.userRepository.save(user);
		}
	}
}