package com.dosth.admin.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Menu;
import com.dosth.admin.repository.RelationRepository;
import com.dosth.admin.service.RelationService;
import com.dosth.common.node.MenuNode;

/**
 * 角色菜单关系Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class RelationServiceImpl implements RelationService {

	@Autowired
	private RelationRepository relationRepository;

	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	@Override
	public List<MenuNode> getMenusByRoleIds(List<String> roleIdList) throws BusinessException {
		List<MenuNode> menuNodeList = new ArrayList<>();
		List<Menu> menuList;
		for (String roleId : roleIdList) {
			menuList = this.relationRepository.getMenuListByRoleId(roleId);
			this.createMenuNodeTree(menuNodeList, menuList);
		}
		return menuNodeList;
	}

	/**
	 * 根据菜单列表封装菜单树
	 * 
	 * @param menuNodeList 封装菜单列表树
	 * @param menuList     菜单列表
	 * @return
	 */
	private List<MenuNode> createMenuNodeTree(List<MenuNode> menuNodeList, List<Menu> menuList) {
		List<Menu> tmpMenuList = new ArrayList<Menu>();
		if (menuList != null && menuList.size() > 0) {
			tmpMenuList = menuList.stream()
					.filter(list -> !list.getId().equals("101") && !list.getId().equals("201")
							&& !list.getId().equals("301") && !list.getId().equals("401")
							&& !list.getId().equals("501") && !list.getId().equals("601"))
					.collect(Collectors.toList());
			MenuNode menuNode;
			List<MenuNode> children;
			for (Menu menu : menuList) {
				menuNode = new MenuNode(menu.getId(), "0", menu.getName(), menu.getLevels(), menu.getIsMenu(),
						menu.getNum(), menu.getUrl(), menu.getIcon());
				children = new ArrayList<>();
				for (Menu child : menu.getChildList()) {
					if (tmpMenuList != null && tmpMenuList.size() > 0) {
						for (Menu tmpList : tmpMenuList) {
							if (child.getId().equals(tmpList.getId())) {
								children.add(this.createMenuNode(menu.getId(), child));
							}
						}
					} else {
						children.add(this.createMenuNode(menu.getId(), child));
					}
				}
				menuNode.setChildren(children);
				menuNodeList.add(menuNode);
			}
		}
		return menuNodeList;
	}

	/**
	 * 创建三级菜单
	 * 
	 * @param pId  上级菜单Id
	 * @param menu 菜单
	 * @return
	 */
	private MenuNode createMenuNode(String pId, Menu menu) {
		List<Menu> childList = menu.getChildList();
		List<MenuNode> children = new ArrayList<>();
		MenuNode menuNode = new MenuNode(menu.getId(), pId, menu.getName(), menu.getLevels(), menu.getIsMenu(),
				menu.getNum(), menu.getSystemUrl(), menu.getIcon());
		for (Menu child : childList) {
			children.add(new MenuNode(child.getId(), menu.getId(), child.getName(), child.getLevels(),
					child.getIsMenu(), child.getNum(), child.getSystemUrl(), child.getIcon()));
		}
		menuNode.setChildren(children);
		return menuNode;
	}
}