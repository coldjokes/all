package com.dosth.admin.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.DosthApplicationTests;
import com.dosth.admin.entity.Menu;

public class RelationRepositoryTest extends DosthApplicationTests {

	@Autowired
	private RelationRepository relationRepository;
	
	@Test
	public void createPopedomList() {
		List<Menu> list = this.relationRepository.getMenuListByRoleId("1");
		List<Menu> menuList = new ArrayList<>();
		for (Menu menu : list) {
			addMenuChildren(menuList, menu);
		}
		for (Menu menu : menuList) {
			System.out.println(menu.getName() + ":" + menu.getUrl());
		}
	}
	
	private void addMenuChildren(List<Menu> menuList, Menu menu) {
		menuList.add(menu);
		for (Menu child : menu.getChildList()) {
			addMenuChildren(menuList, child);
		}
	}
}