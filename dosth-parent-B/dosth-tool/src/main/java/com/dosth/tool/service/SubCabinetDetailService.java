package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubCabinetDetail;

/**
 * 
 * @description 副柜明细Service
 * @author guozhidong
 *
 */
public interface SubCabinetDetailService extends BaseService<SubCabinetDetail> {
	/**
	 * @description 由副柜格子获取指定物料的存储列表
	 * @param subBoxId  副柜格子Id
	 * @param matInfoId 物料Id
	 * @return
	 */
	public List<SubCabinetDetail> getSubDetailList(String subBoxId, String matInfoId);

	/**
	 * @description 根据副柜盒子Id获取副柜详情
	 * @param subBoxId 副柜盒子Id
	 * @return
	 * @throws DoSthException
	 */
	public List<SubCabinetDetail> getSubDetailListBySubBoxId(String subBoxId) throws DoSthException;

	/**
	 * @description 统计副柜
	 * @param accountId
	 * @return
	 */
	public Map<String, Integer> getTotalQuantityGroupByMatId(String shareSwitch, String accountId)
			throws DoSthException;

	/**
	 * @description 根据物料获取暂存柜集合
	 * @param matId 物料Id
	 * @return
	 */
	public List<SubCabinetDetail> getSubDetailListByMatId(String matId);

	/**
	 * @description 根据帐户和物料获取暂存柜集合
	 * @param accountId 帐户Id
	 * @param matId     物料Id
	 * @return
	 */
	public List<SubCabinetDetail> getSubDetailListByAccountIdAndMatId(String accountId, String matId);

	/**
	 * @description 暂存柜获取物料信息(我要取料)
	 * @param subboxId  暂存柜盒子Id
	 * @param matInfoId 物料Id
	 * @return
	 */
	public SubCabinetDetail outsubcabinet(String subboxId, String matInfoId);

	/**
	 * @description 指定帐户暂存柜物料信息
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<MatEquInfo> getSubCabinetMatList(String accountId);

	/**
	 * @description 根据帐户ID获取暂存柜信息
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<SubCabinetDetail> findByAccountId(String accountId);

	/**
	 * @description 获取全部暂存柜信息
	 * @param
	 * @return
	 */
	public List<SubCabinetDetail> getSubCabinetDetailList();
}