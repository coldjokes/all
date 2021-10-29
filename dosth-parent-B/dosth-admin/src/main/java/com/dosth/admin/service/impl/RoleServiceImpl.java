package com.dosth.admin.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.entity.Relation;
import com.dosth.admin.entity.Roles;
import com.dosth.admin.repository.RelationRepository;
import com.dosth.admin.repository.RoleRepository;
import com.dosth.admin.service.RoleService;
import com.dosth.common.node.ZTreeNode;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RelationRepository relationRepository;

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> tree(Map<String, String> paramMap) throws BusinessException {
		List<Roles> roleList = this.roleRepository.getRoleList();
		List<ZTreeNode> treeNodeList = new ArrayList<>();
		for (Roles role : roleList) {
			this.createTreeNode(role, treeNodeList);
		}
		return treeNodeList;
	}

	@Override
	public void save(Roles role) throws BusinessException {
		this.roleRepository.save(role);
	}

	@Override
	public Roles get(Serializable id) throws BusinessException {
		Roles role = this.roleRepository.getOne(id);
		return role;
	}

	@Override
	public Roles update(Roles role) throws BusinessException {
		return this.roleRepository.saveAndFlush(role);
	}

	@Override
	public void delete(Roles role) throws BusinessException {
		this.procMenuIds(role.getId(), null, true);
		this.roleRepository.delete(role);
	}

	@Override
	public boolean isHaveChildren(Long pId) throws BusinessException {
		List<Roles> list = this.roleRepository.findAll(new Specification<Roles>() {
			@Override
			public Predicate toPredicate(Root<Roles> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate p1 = cb.equal(root.get("pId"), pId);
				Predicate p2 = cb.notEqual(root.get("pId"), root.get("id"));
				query.where(p1, p2);
				return null;
			}
		});
		if (list != null && list.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 创建角色树
	 * 
	 * @param role
	 * @param treeNodeList
	 * @return
	 */
	private List<ZTreeNode> createTreeNode(Roles role, List<ZTreeNode> treeNodeList) {
		treeNodeList.add(new ZTreeNode(String.valueOf(role.getId()),
				String.valueOf((role.getpId() == null || role.getId().equals(role.getpId())) ? 0L : role.getpId()), role.getName(),
						role.getId().equals(role.getpId()), true));
		for (Roles r : role.getRoleList()) {
			if (treeNodeList.contains(new ZTreeNode(String.valueOf(r.getId()), null, null))) {
				continue;
			}
			createTreeNode(r, treeNodeList);
		}
		return treeNodeList;
	}

	@Override
	public void save(Roles role, String menuIds) throws BusinessException {
		Roles theRoles = new Roles();
		theRoles.setDeptId(role.getDeptId().substring(2, role.getDeptId().length()));
		theRoles.setName(role.getName());
		theRoles.setNum(role.getNum());
		theRoles.setpId(role.getpId());
		theRoles.setTips(role.getTips());
		theRoles.setVersion(role.getVersion());
		this.roleRepository.save(theRoles);
		this.procMenuIds(role.getId(), menuIds, false);
	}

	@Override
	public Roles update(Roles role, String menuIds) throws BusinessException {
		update(role);
		this.procMenuIds(role.getId(), menuIds, true);
		return role;
	}
	
	/**
	 * 处理权限菜单
	 * @param roleId 角色Id
	 * @param menuIds 权限菜单Id集合
	 * @param isClearHistory 是否清理历史
	 */
	private void procMenuIds(String roleId, String menuIds, Boolean isClearHistory) {
		List<Relation> relationList = null;
		if (isClearHistory != null && isClearHistory) {
			relationList = this.relationRepository.getListByRoleId(roleId);
			for (Relation relation : relationList) {
				this.relationRepository.delete(relation);
			}
		}
		if (menuIds != null && menuIds.length() > 0) {
			relationList = new ArrayList<>();
			for (String menuId : menuIds.split(",")) {
				relationList.add(new Relation(roleId, menuId));
			}
			this.relationRepository.save(relationList);
		}
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> createRoleTree() throws BusinessException {
		List<ZTreeNode> nodeList = new ArrayList<>();
		List<Roles> roleList = this.roleRepository.findAll();
		for (Roles role : roleList) {
			nodeList.add(new ZTreeNode(String.valueOf(role.getId()), String.valueOf(role.getpId() != null ? role.getpId() : role.getId()), 
					role.getName(), true, false));
		}
		return nodeList;
	}
}