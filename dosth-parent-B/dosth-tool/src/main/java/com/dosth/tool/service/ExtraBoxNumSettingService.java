package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.dto.ExtraBoxNum;
import com.dosth.tool.entity.ExtraBoxNumSetting;

public interface ExtraBoxNumSettingService extends BaseService<ExtraBoxNumSetting> {

	/**
	 * 分页方法
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param equInfoId Id
	 * @param status    是否启用状态
	 * @return
	 * @throws DoSthException
	 */
	public Page<ExtraBoxNumSetting> getPage(int pageNo, int pageSize, String name) throws DoSthException;

	public String getAccount(String id) throws DoSthException;

	/**
	 * @description 获取暂存柜允许最大数量
	 * @return
	 */
	public Integer getExtraBoxNumByAccountId(String accountId);

	/**
	 * @description 获取暂存柜信息
	 * @return
	 */
	public List<ExtraBoxNumSetting> findAll();

	/**
	 * @description 根据账户Id获取暂存柜数量设定集合
	 * @param accountId 账户Id
	 * @return
	 */
	public List<ExtraBoxNumSetting> getNumListByAccountId(String accountId);

	/**
	 * @description 修改暂存柜数量-admin调用
	 * @param accountId 账户Id
	 * @param extraBoxNum 数量
	 * @return
	 */
	public void updateExtraBoxNum(String accountId, String extraBoxNum);

	/**
	 * @description 查询暂存柜数量-admin调用
	 * @param accountId 账户Id
	 * @return
	 */
	public ExtraBoxNum getExtraBoxNum(String accountId);

	/**
	 * @description 删除暂存柜数量-admin调用
	 * @param accountId 账户Id
	 * @return
	 */
	public void delExtraBoxNum(String accountId);
}