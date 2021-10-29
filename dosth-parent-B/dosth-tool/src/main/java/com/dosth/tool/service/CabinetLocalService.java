package com.dosth.tool.service;

import java.util.Map;

import com.dosth.tool.entity.MatEquInfo;

/**
 * @description 柜子本地服务接口
 * @author guozhidong
 *
 */
public interface CabinetLocalService {
	/**
	 * @description 按物料分组获取柜子统计总数
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public Map<MatEquInfo, Integer> getTotalQuantityGroupByMatEquInfo(String cabinetId);

	/**
	 * @description 获取借出统计数按物料分类
	 * @param accountId 账户Id
	 * @return
	 */
	public Map<String, Integer> getBorrCountGroupByMatId(String accountId);
}