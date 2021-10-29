package com.dosth.tool.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.entity.Statement;

/**
 * @description 供应商核对Service
 * @author guozhidong
 *
 */
public interface StatementService extends BaseService<Statement> {
	/**
	 * @description 获取列表
	 * @param manufacturerId 供应商Id
	 * @param startDate 起始日期
	 * @param endDate 截止日期
	 * @return
	 * @throws DoSthException
	 */
	public List<Statement> getStatementList(String manufacturerId, String startDate, String endDate) throws DoSthException;
	
	/**
	 * @description 供应商核对信息列表
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param startDate 起始日期
	 * @param endDate 截止日期
	 * @return
	 * @throws DoSthException
	 */
	public Page<Statement> getPager(int pageNo, int pageSize, String startDate, String endDate) throws DoSthException;

	/**
	 * @description 核对
	 * @param manufacturerId 供应商Id
	 * @param startDate 起始日期
	 * @param endDate 截止日期
	 */
	public void statement(String manufacturerId, String startDate, String endDate);

	/**
	 * @description 获取核对明细
	 * @param statementId 核对Id
	 * @return
	 */
	public List<MatUseRecord> getMatUseRecordView(String statementId);

	/**
	 * @description 导出核对明细
	 * @param statementId 核对Id
	 * @param request
	 * @param response
	 */
	public void exportDetail(String statementId, HttpServletRequest request, HttpServletResponse response);
}