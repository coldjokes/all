package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.util.OpTip;

/**
 * 
 * @description 副柜盒子与帐户关联Service
 * @author guozhidong
 *
 */
public interface SubBoxAccountRefService extends BaseService<SubBoxAccountRef> {

	/**
	 * 
	 * @description 根据帐户获取正在使用的副柜盒子
	 * @param accountId
	 * @return
	 */
	public List<SubBox> getUsingSubBoxList(String accountId);
	
	/**
	 * @description 根据副柜Id副柜账户关系
	 * @param subBoxId
	 * @return
	 */
	public SubBoxAccountRef getAccountBySubBoxId(String subBoxId);

	/**
	 * @description 校验新暂存柜权限
	 * @param matId 物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	public OpTip checkNewSubCabinet(String matId, String accountId);
}