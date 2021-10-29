package com.dosth.tool.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.admin.FeignDept;
import com.cnbaosi.dto.admin.FeignDeptUserGroup;
import com.cnbaosi.dto.admin.FeignUser;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.common.state.ManagerStatus;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.repository.DeptRepository;
import com.dosth.tool.repository.UserRepository;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewDept;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 用户Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DeptRepository deptRepository;

	@Override
	public List<ZTreeNode> tree(Map<String, String> paramMap) throws DoSthException {
		List<ZTreeNode> treeNode = new ArrayList<>();
		List<ViewDept> deptList = this.deptRepository.findAll();

		ZTreeNode node;

		for (ViewDept dept : deptList) {
			if (dept.getDeptStatus().equals(ManagerStatus.FREEZED.toString())
					|| dept.getDeptStatus().equals(ManagerStatus.DELETED.toString())) {
				continue;
			}
			node = new ZTreeNode("d_" + dept.getDeptId(),
					"d_" + (dept.getDeptPId() != null ? dept.getDeptPId() : dept.getDeptPId()), dept.getDeptName(),
					dept.getDeptId() != dept.getDeptPId());
			node.setLevel(0);
			node.setIcon("/static/img/part.png");
			if (dept.getDeptPId() == null || "".equals(dept.getDeptPId())) {
				node.setIcon("/static/img/comp.png");
			}
			treeNode.add(node);
		}

		List<ViewUser> userList = this.userRepository.findAll();
		if (treeNode != null && treeNode.size() > 0) {
			for (ViewUser user : userList) {
				if (user.getLoginName().equals("administrator") || user.getUserName().equals("administrator")) {
					continue;
				}
				node = new ZTreeNode(String.valueOf(user.getAccountId()), "d_" + user.getDeptId(),
						ViewUserUtil.createViewUserName(user));
				node.setLevel(1);
				node.setIcon("/static/img/person.png");
				treeNode.add(node);
			}
		}
		treeNode.sort((tree1, tree2) -> tree2.getLevel() - tree1.getLevel());
		return treeNode;
	}

	@Override
	public ViewUser getViewUser(String accountId) throws DoSthException {
		return this.userRepository.findUserByAccountId(accountId);
	}

	@Override
	public List<FeignDeptUserGroup> group() throws DoSthException {

		List<FeignDeptUserGroup> groupList = new ArrayList<>();
		List<ViewDept> deptList = this.deptRepository.findAll();

		for (ViewDept dept : deptList) {
			if (dept.getDeptStatus().equals(ManagerStatus.FREEZED.toString())
					|| dept.getDeptStatus().equals(ManagerStatus.DELETED.toString())) {
				continue;
			}
			groupList.add(new FeignDeptUserGroup(new FeignDept(dept.getDeptId(), dept.getDeptName())));
		}

		List<ViewUser> userList = this.userRepository.findAll();
		List<ViewUser> tmpUserList;
		for (FeignDeptUserGroup group : groupList) {
			tmpUserList = userList.stream()
					.filter(user -> user.getDeptId().equals(group.getDept().getDeptId())
							&& !user.getLoginName().equals("administrator")
							&& !user.getUserName().equals("administrator"))
					.collect(Collectors.toList());
			for (ViewUser tmp : tmpUserList) {
				group.getUserList().add(new FeignUser(tmp.getAccountId(), tmp.getUserName(), tmp.getLoginName()));
			}
		}
		return groupList;
	}
}