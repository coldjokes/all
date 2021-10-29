package com.dosth.toolcabinet.service;

import java.util.List;

import com.cnbaosi.dto.tool.ApplyVoucher;

/**
 * @description 鲍斯对外接口
 * @author guozhidong
 *
 */
public interface CnbaosiFeignService {
	/**
	 * @description 获取申请单列表
	 * @param userName 申请人名称
	 * @param search   搜索条件
	 * @return
	 */
	public List<ApplyVoucher> getApplyVoucherList(String userName, String search);

	/**
	 * @description 推送申请单结果
	 * @param applyVoucherResult 申请单结果
	 */
	public void sendApplyVoucherResult(String applyVoucherResult);

	/**
	 * @description 同步补料单
	 * @param feedingListId 补料单Id
	 */
	public void syncFeedingList(String feedingListId);
}