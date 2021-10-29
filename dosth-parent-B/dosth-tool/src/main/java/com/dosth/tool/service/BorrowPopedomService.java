package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.node.TypeNode;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.BorrowPopedom;
import com.dosth.toolcabinet.dto.RpcBorrowType;

/**
 * 
 * @description 借出权限Service
 * @author guozhidong
 *
 */
public interface BorrowPopedomService extends BaseService<BorrowPopedom> {

	/**
	 * @description 根据帐户Id获取借出权限列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<BorrowPopedom> getPopedomList(String accountId);

	/**
	 * @description 解绑指定帐户所有取料权限
	 * @param accountId  帐户Id
	 */
	public void deletePopedomListByAccountId(String accountId);

	/**
	 * @description 绑定借出权限
	 * @param opObjId 操作对象Id
	 * @param popedoms  借出权限
	 * @return
	 */
	public Boolean bindBorrowPopedoms(String opObjId, String popedoms);

	/**
	 * @description 初始化借出权限树
	 * @param opObjId 操作对象Id
	 * @return
	 */
	public List<ZTreeNode> initBorrowPopedomTree(String opObjId);

	/**
	 * @description 获取刀具柜借出类型列表
	 * 
	 * @param cabinetId 刀具柜Id
	 * @param accountId 账户Id
	 * 
	 * @return
	 */
	public List<RpcBorrowType> getBorrowTypeList(String cabinetId, String accountId);
	
	/**
	 * @description 获取物料类型树
	 * 
	 * @param cabinetId 刀具柜Id
	 * @param accountId 帐户Id
	 * @param type 领取类型
	 * @return
	 */
	public List<TypeNode> getCategoryTree(String cabinetId, String accountId, String type);
	
}