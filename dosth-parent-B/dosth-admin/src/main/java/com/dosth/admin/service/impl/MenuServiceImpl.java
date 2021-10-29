package com.dosth.admin.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Menu;
import com.dosth.admin.repository.MenuRepository;
import com.dosth.admin.repository.RelationRepository;
import com.dosth.admin.service.MenuService;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;

/**
 * 菜单Service
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private RelationRepository relationRepository;
	
	@Override
	public void save(Menu menu) throws DoSthException {
		this.menuRepository.save(menu);
	}

	@Override
	public Menu get(Serializable id) throws DoSthException {
		Menu menu = this.menuRepository.getOne(id);
		return menu;
	}

	@Override
	public Menu update(Menu menu) throws DoSthException {
		return this.menuRepository.saveAndFlush(menu);
	}

	@Override
	public void delete(Menu menu) throws DoSthException {
		this.menuRepository.delete(menu);
	}

	@Override
	public List<String> getMenuIdsByRoleId(String roleId) throws BusinessException {
		return this.relationRepository.getMenuIdListByRoleId(roleId);
	}

	@Override
	public List<ZTreeNode> menuTreeList() throws BusinessException {
		List<ZTreeNode> nodeList = new ArrayList<>();
		List<Menu> menuList = this.menuRepository.createMenuTree();
		for (Menu menu : menuList) {
			this.createNodeList(menu, menu.getId(), nodeList, null);
		}
		return nodeList;
	}

	@Override
	public List<ZTreeNode> menuTreeListByMenuIds(List<String> menuIds) throws BusinessException {
		List<ZTreeNode> nodeList = new ArrayList<>();
		List<Menu> menuList = this.menuRepository.createMenuTree();
		for (Menu menu : menuList) {
			this.createNodeList(menu, menu.getId(), nodeList, menuIds);
		}
		return nodeList;
	}
	
	/**
	 * 创建
	 * @param menu
	 * @param nodeList
	 */
	private void createNodeList(Menu menu, String pId, List<ZTreeNode> nodeList, List<String> menuIds) {
		ZTreeNode treeNode = new ZTreeNode(String.valueOf(menu.getId()), String.valueOf(pId), menu.getName(), 
				menuIds != null && menuIds.size() > 0, menuIds!= null && menuIds.contains(menu.getId()));
		nodeList.add(treeNode);
		for (Menu child : menu.getChildList()) {
			createNodeList(child, menu.getId(), nodeList, menuIds);
		}
	}
}