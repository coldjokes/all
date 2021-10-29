package com.dosth.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.service.UserService;

/**
 * 人员树Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 创建人员tree
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<ZTreeNode> tree() {
		List<ZTreeNode> tree = this.userService.tree(null);
		return tree;
	}
}