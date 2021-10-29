package com.dosth.admin.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;
import com.dosth.admin.entity.Menu;
import com.dosth.common.node.ZTreeNode;

public class MenuServiceTest extends DosthApplicationTests {

	@Autowired
	private MenuService menuService;
	
	@Test
	public void test() {
		Menu menu = this.menuService.get(2L);
		System.out.println("***************" + menu.getpMenu().getName());
		printMenu(menu);
	}
	
	private void printMenu(Menu menu) {
		System.out.println(menu.getName());
		List<Menu> childList = menu.getChildList();
		for (Menu child : childList) {
			printMenu(child);
		}
	}
	
	@Test
	public void testService() {
		List<String> menuIds = this.menuService.getMenuIdsByRoleId("1");
		List<ZTreeNode> nodeList = this.menuService.menuTreeListByMenuIds(menuIds);
		for (ZTreeNode node : nodeList) {
			System.out.println(node.getName());
		}
		System.out.println("*******************");
		nodeList = this.menuService.menuTreeList();
		for (ZTreeNode node : nodeList) {
			System.out.println(node.getName());
		}
	}
}