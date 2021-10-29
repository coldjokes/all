package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import com.dosth.app.dto.FeignUser;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.DailyLimit;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;

/**
 * 
 * @description 每日限额Service
 * @author liweifeng
 *
 */
public interface DailyLimitService extends BaseService<DailyLimit> {

	/**
	 * @description 根绝账户Id删除权限物料数据
	 * @param accountId
	 */
	public void deleteByAccountId(String accountId) throws DoSthException;

	/**
	 * @description 根绝账户Id查询每日限额信息
	 * @param accountId
	 */
	public List<DailyLimit> findAllByAccountId(String accountId) throws DoSthException;

	/**
	 * @description 根绝账户Id同步限额信息
	 * @param accountId
	 */
	public OpTip dataSyncByAccountId(String accountId) throws DoSthException;

	/**
	 * @description 立即领取每日限额判断
	 * @param accountId
	 */
	public OpTip getDailyLimit(String accountId, String matId, Integer borrowNum, UserInfo userInfo);

	/**
	 * @description 购物车领取每日限额判断
	 * @param accountId
	 */
	public OpTip getDailyLimitByCart(String shareSwitch, String accountId, Map<String, CartInfo> limitMap,
			String startTime, String endTime, Integer limitSumNum, Integer notReturnLimitNum);

	/**
	 * @description 保存每日限额
	 */
	public OpTip saveDailyLimit(Map<String, DailyLimit> limitMa, FeignUser feignUser);

	/**
	 * 根据帐号id删除每日限额
	 * 
	 * @param accountId
	 * @return
	 */
	public void delDailyLimit(String accountId);

}
