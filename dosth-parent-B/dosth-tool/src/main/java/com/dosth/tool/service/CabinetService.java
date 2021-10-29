package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.node.ZTreeNode;

/**
 * @description 柜体Service
 * @author guozhidong
 *
 */
public interface CabinetService {
	/**
	 * @description 创建柜体树
	 * @return
	 */
	public List<ZTreeNode> createCabinetTree();

	/**
	 * @description 创建主柜树形数据
	 * @return
	 */
	public List<ZTreeNode> createMainCabinetTree();
}