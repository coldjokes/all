package com.dosth.tool.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.tool.FeignReturnBack;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;

/**
 * @description 物料归还Service
 * @author guozhidong
 *
 */
public interface MatReturnBackService extends BaseService<MatReturnBack> {

	/**
	 * 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param userName  归还人员
	 * @param status    审核状态
	 * @param backType  归还种别
	 * @return
	 * @throws BusinessException
	 */
	public Page<MatReturnBack> getPage(int pageNo, int pageSize, String beginTime, String endTime, String userName,
			String status, String backType, String equSettingName) throws DoSthException;

//	/**
//	 * app回收确认
//	 * 
//	 * @param accountId
//	 * @param matReturnBackId
//	 * @return
//	 */
//	public MatReturnBackVerify appConfirm(String accountId, String matReturnBackId, int num, AuditStatus auditStatus,
//			String remark);

	/**
	 * 回收管理信息导出
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param userName
	 * @param status
	 * @return
	 * @throws IOException
	 */
	public String infoExport(HttpServletRequest request, HttpServletResponse response, String beginTime, String endTime,
			String userName, String status, String backType, String equSettingName) throws IOException;

	/**
	 * 回收管理信息导出
	 * 
	 * @param billId
	 * @return
	 */
	public Boolean isBackCheck(String returnBackNo);

	/**
	 * 扫码获取归还信息
	 * 
	 * @param billId
	 * @return
	 */
	public List<ReturnBackPrintInfo> getReturnInfo(String billId);

	/**
	 * 归还记录
	 * 
	 * @param cabinetName 刀具柜别名
	 * @param endTime     查询截止时间
	 * @return
	 */
	public ApiFeignResponse<FeignReturnBack> getSyncReturnBack(String cabinetName, Long endTime);

	/**
	 * 归还记录邮件发送
	 * 
	 * @param beginTime
	 * @param endTime
	 * @param userName
	 * @param status
	 * @param backType
	 * @param equSettingName
	 * @return
	 * @throws IOException
	 */
	public void sendReturnRecord(String beginTime, String endTime, String userName, String status, String backType,
			String equSettingName);

}