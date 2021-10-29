package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.LockParam;

/**
 * 锁控板参数
 * 
 * @author Weifeng.Li
 *
 */
public interface LockParamService extends BaseService<LockParam> {

	public List<LockParam> getLockParamByEquSettingId(String equSettingId, CabinetType cabinetType);

	/**
	 * @description 根据柜子Id获取板子栈号列表
	 * @param equSettingId 柜子Id
	 * @return
	 */
	public List<Integer> getBoardNoListByEquSettingId(String equSettingId);

	/**
	 * @description 根据主柜Id获取附属储物柜栈号列表
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	public List<Integer> getStoreBoardNoListByEquSettingId(String mainCabinetId);
}