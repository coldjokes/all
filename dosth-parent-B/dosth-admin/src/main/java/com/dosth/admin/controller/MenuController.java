package com.dosth.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.service.MenuService;
import com.dosth.common.controller.BaseController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.ToolUtil;

/**
 * 菜单Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;
	
	@RequestMapping("/menuTreeListByRoleId/{roleId}")
    @ResponseBody
    public List<ZTreeNode> menuTreeListByRoleId(@PathVariable String roleId) {
		List<String> menuIds = null;
		if (!"-1".equals(roleId)) {
			menuIds = this.menuService.getMenuIdsByRoleId(roleId);
		}
        if (ToolUtil.isEmpty(menuIds)) {
            List<ZTreeNode> roleTreeList = this.menuService.menuTreeList();
            return roleTreeList;
        } else {
            List<ZTreeNode> roleTreeListByUserId = this.menuService.menuTreeListByMenuIds(menuIds);
            return roleTreeListByUserId;
        }
    }
}