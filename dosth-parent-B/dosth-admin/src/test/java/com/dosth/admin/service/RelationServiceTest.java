package com.dosth.admin.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;
import com.dosth.common.node.MenuNode;

public class RelationServiceTest extends DosthApplicationTests {

	@Autowired
	private RelationService relationService;

	@Test
	public void test() {
		List<String> roleIdList = new ArrayList<>();
		roleIdList.add("1");
		List<MenuNode> list = this.relationService.getMenusByRoleIds(roleIdList);
		for (MenuNode node : list) {
			System.out.println("#####" + node.getName() + "[" + node.getId() + "]");
			List<MenuNode> children = node.getChildren();
			if (children != null && children.size() > 0) {
				for (MenuNode child : children) {
					System.out.println("*********" + child.getName() + "[" + node.getId() + "-" + child.getId() + "]");
					List<MenuNode> thirdList = child.getChildren();
					for (MenuNode third : thirdList) {
						System.out.println(">>>>>>>>>>>" + third.getName() + "[" + child.getId() + "-" + third.getId() + "]");
					}
				}
			}
		}
	}
}