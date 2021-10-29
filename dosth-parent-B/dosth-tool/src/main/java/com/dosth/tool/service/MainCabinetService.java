package com.dosth.tool.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.vo.EquDetailStaVo;

/**
 * @description 主柜Service
 * @author guozhidong
 *
 */
public interface MainCabinetService {

	/**
	 * @description 查询主柜分页数据
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public Page<EquDetailSta> getEquDetailStaPage(int pageNo, int pageSize, String cabinetId,String matInfo, String rowNo, String colNo);

	/**
	 * @description 主柜详情导出
	 * @param response
	 * @param cabinetId 柜子Id
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, String cabinetId);

	/**
	 * @description 查询主柜分页数据
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public Page<EquDetailStaVo> getEquDetailStaVoPage(int pageNo, int pageSize, String cabinetId);

	/**
	 * 库存明细邮件发送
	 * 
	 * @param cabinetId
	 */
	public void sendStockRecord(String cabinetId);
}