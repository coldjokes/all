package com.dosth.tool.service;

import com.cnbaosi.dto.ElecLock;

/**
 * @description 副柜管理Service
 * 
 * @author guozhidong
 *
 */
public interface SubCabinetService {

	/**
	 * @description 我要暂存
	 * @param matId     物料Id
	 * @param matNum    物料数量
	 * @param storType  暂存种别
	 * @param accountId 帐户Id
	 * @return
	 */
	public ElecLock tmpNewStor(String matId, int matNum, String storType, String accountId);
}