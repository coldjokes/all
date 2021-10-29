package com.dosth.tool.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.vo.SubBoxVo;

/**
 * @description 暂存柜Service
 * @author guozhidong
 *
 */
public interface TempCabinetService {

	/**
	 * @description 根据暂存柜Id获取暂存柜详情
	 * @param pageNo       当前页码
	 * @param pageSize     每页大小
	 * @param subCabinetId 暂存柜Id
	 * @return
	 */
	public Page<SubBox> getSubBoxPage(int pageNo, int pageSize, String cabinetId);

	/**
	 * @description 暂存柜库存导出
	 * @param response
	 * @param subCabinetId 暂存柜Id
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, String subCabinetId);

	/**
	 * @description 根据暂存柜Id获取暂存柜详情
	 * @param pageNo       当前页码
	 * @param pageSize     每页大小
	 * @param subCabinetId 暂存柜Id
	 * @return
	 */
	public Page<SubBoxVo> getSubBoxVoPage(int pageNo, int pageSize, String cabinetId);

	/**
	 * 库存明细邮件发送
	 * 
	 * @param subCabinetId
	 */
	public void sendStockRecord(String subCabinetId);
}