package com.dosth.admin.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;

public class AccountRoleRepositoryTest extends DosthApplicationTests {

	@Autowired
	AccountRoleService accountRoleService;
	
	@Test
	public void test() {
		System.out.println(this.accountRoleService);
		this.accountRoleService.updateRoles("1", "1,2,3");
	}
}
