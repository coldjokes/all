package com.dosth.tool.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.tool.FeignCodeName;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.MatUseBill;
import com.dosth.toolcabinet.dto.BillInfo;

/**
 * @description 物料使用流水Service
 * 
 * @author guozhidong
 *
 */
public interface MatUseBillService extends BaseService<MatUseBill> {

	/**
	 * @description 根据帐户获取待归还列表
	 * @param accountId 帐户Id
	 * @return
	 * @throws DoSthException
	 */
	public List<MatUseBill> getUnReturnList(String accountId) throws DoSthException;
	
	/**
	 * @description 查询未归还类型
	 * @param accountId 帐户Id
	 * @return
	 * @throws DoSthException
	 */
	public List<FeignCodeName> getUnReturnTypeList(String accountId, String cabinetId) throws DoSthException;
	
	/**
	 * @description 获取流水信息
	 * @param billId 流水Id
	 * @return
	 * @throws DoSthException
	 */
	public BillInfo getBillInfo(String billId) throws DoSthException;

	/**
	 * @description 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @return
	 * @throws DoSthException
	 */
	public Page<MatUseBill> getPage(int pageNo, int pageSize, String beginTime, String endTime, String receiveType) throws DoSthException;
	
	/**
	 * @description 领用导出
	 * @param response ServletResponse
	 * @param receiveType 领取类型
	 * @param beginTime 起始日期
	 * @param endTime 截止日期
	 */
	public void export(HttpServletResponse response, String receiveType, String beginTime, String endTime);
}