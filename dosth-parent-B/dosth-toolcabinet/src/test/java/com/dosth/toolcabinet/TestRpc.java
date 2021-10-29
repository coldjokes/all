package com.dosth.toolcabinet;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.toolcabinet.dto.AccountUserInfo;
import com.dosth.toolcabinet.service.AdminService;

public class TestRpc extends AppTest {

	@Autowired
	private AdminService adminService;
	
	@Test
	public void testA() {
		System.out.println(this.adminService);
		AccountUserInfo info = this.adminService.getAccountUserInfoByCardStr("123");
		System.out.println(info);
	}
}