package com.dosth.tool.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.NoticeMgr;
import com.dosth.toolcabinet.dto.NoticeMgrInfo;

/**
 * @description 通知管理
 * @author Weifeng Li
 *
 */
public interface NoticeMgrService extends BaseService<NoticeMgr> {

	/**
	 * 分页方法
	 * 
	 * @param pageNo   当前页码
	 * @param pageSize 每页大小
	 * @param name     刀具柜名称
	 * @return
	 */
	public Page<NoticeMgr> getPage(int pageNo, int pageSize, String name, String noticeType);

	/**
	 * 修改计数
	 * 
	 * @param cabinetId
	 * @return
	 */
	public List<NoticeMgrInfo> editCount(String cabinetId);

	/**
	 * 查询计数
	 * 
	 * @param cabinetId
	 * @return
	 */
	public List<NoticeMgrInfo> getNoticeMgr(String cabinetId);

	/**
	 * 重置计数
	 * 
	 * @param cabinetId
	 * @param printNum
	 * @return
	 */
	public NoticeMgrInfo resetNoticeMgr(String cabinetId, int num, String noticeType);

	/**
	 * 查询
	 * 
	 * @param cabinetId
	 * @return
	 */
	public List<NoticeMgr> getNoticeMgrByCabinetId(String cabinetId);

	/**
	 * 删除收件人
	 * 
	 * @param cabinetId
	 * @return
	 */
	public void delNoticeMgr(String accountId);

}
