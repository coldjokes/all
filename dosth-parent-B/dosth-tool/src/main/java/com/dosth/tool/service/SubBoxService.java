package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.SubBox;

/**
 * 副柜盒子Service
 * 
 * @author guozhidong
 *
 */
public interface SubBoxService extends BaseService<SubBox> {

	/**
	 * 分页方法
	 * 
	 * @param pageNo   当前页码
	 * @param pageSize 每页大小
	 * @param name     刀具柜名称
	 * @return
	 */
	public Page<SubBox> getPage(int pageNo, int pageSize, String equSettingId);

	/**
	 * 根据副柜电路板Id获取副柜盒子列表
	 * 
	 * @param subCardId
	 * @return
	 * @throws DoSthException
	 */
	public List<SubBox> getSubBoxList(String subCardId) throws DoSthException;

	/**
	 * @description 获取未使用的副柜盒子
	 * @return
	 * @throws DoSthException
	 */
	public List<SubBox> getUnUsedSubBoxList() throws DoSthException;

	/**
	 * @description 获取暂存柜总数量
	 * @return
	 * @throws DoSthException
	 */
	public Integer getSubBoxNum() throws DoSthException;
}