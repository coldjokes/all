package com.dosth.admin.util;

import java.util.ArrayList;
import java.util.List;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.admin.constant.Const;
import com.dosth.common.node.MenuNode;
import com.dosth.common.util.SpringContextHolder;

/**
 * api接口文档显示过滤
 * 
 * @author guozhidong
 *
 */
public class ApiMenuFilter extends MenuNode {

	public static List<MenuNode> build(List<MenuNode> nodes) {
		// 如果关闭了接口文档,则不显示接口文档菜单
		DosthProperties dosthProperties = SpringContextHolder.getBean(DosthProperties.class);
	//	if (!dosthProperties.getSwaggerOpen()) {
		// 暂未处理
		if (dosthProperties == null) {
			List<MenuNode> menuNodesCopy = new ArrayList<>();
			for (MenuNode menuNode : nodes) {
				if (Const.API_MENU_NAME.equals(menuNode.getName())) {
					continue;
				} else {
					menuNodesCopy.add(menuNode);
				}
			}
			nodes = menuNodesCopy;
		}
		return nodes;
	}
}