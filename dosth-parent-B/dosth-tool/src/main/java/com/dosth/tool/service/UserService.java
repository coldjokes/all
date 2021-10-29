package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import com.cnbaosi.dto.admin.FeignDeptUserGroup;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.vo.ViewUser;

/**
 * @description 用户Service
 * 
 * @author guozhidong
 *
 */
public interface UserService {
	/**
	 * @description 创建人员树
	 * 
	 * @param paramMap 条件集合
	 * 
	 * @return
	 * @throws DoSthException
	 */
	public List<ZTreeNode> tree(Map<String, String> paramMap) throws DoSthException;

	/**
	 * @description 根据帐户Id获取用户信息-tool
	 * @param accountId
	 * @return
	 * @throws DoSthException
	 */
	public ViewUser getViewUser(String accountId) throws DoSthException;

	/**
	 * @description 创建部门人员分组
	 * @return
	 * @throws DoSthException
	 */
	public List<FeignDeptUserGroup> group() throws DoSthException;
}