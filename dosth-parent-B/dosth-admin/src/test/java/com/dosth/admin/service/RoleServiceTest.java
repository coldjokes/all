package com.dosth.admin.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;
import com.dosth.common.node.ZTreeNode;

public class RoleServiceTest extends DosthApplicationTests {

	@Autowired
	private RoleService roleService;
	
	@Test
	public void test1() {
		List<ZTreeNode> list = this.roleService.tree(null);
		for (ZTreeNode node : list) {
			System.out.println(node.getName());
		}
	}
	
	@Test
	public void test2() {
		boolean flag = this.roleService.isHaveChildren(1L);
		System.out.println(flag);
	}
}