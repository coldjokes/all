package com.dosth.tool.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;

import com.cnbaosi.dto.ApiFeignResponse;
import com.cnbaosi.dto.tool.ApplyVoucher;
import com.cnbaosi.dto.tool.FeignBorrow;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.servcie.BaseService;
import com.dosth.statistics.dto.MonthCost;
import com.dosth.tool.entity.MatUseRecord;

/**
 * @description 物料领用记录Service
 * @author guozhidong
 *
 */
public interface MatUseRecordService extends BaseService<MatUseRecord>{

	/**
	 * @description 添加
	 * @param record
	 * @throws DoSthException
	 */
	public void save(MatUseRecord record) throws DoSthException;

	/**
	 * @description 根据Id获取领用记录明细
	 * @param id
	 * @return
	 * @throws DoSthException
	 */
	public MatUseRecord get(Serializable id) throws DoSthException;

	/**
	 * @description 获取分页数据
	 * 
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param beginTime 起始日期
	 * @param endTime   截止日期
	 * @param receiveType 领料类型
	 * @param isReturnBack 是否归还
	 * @param equSettingName 设备名称
	 * @param searchCondition 模糊匹配条件
	 * @return
	 * @throws DoSthException
	 */
	public Page<MatUseRecord> getPage(int pageNo, int pageSize, String beginTime, String endTime, String receiveType,
			String isReturnBack, String equSettingName, String searchCondition) throws DoSthException;

	/**
	 * @description 领用导出
	 * @param response    ServletResponse
	 * @param receiveType 领取类型
	 * @param beginTime   起始日期
	 * @param endTime     截止日期
	 * @param receiveType 领料类型
	 * @param isReturnBack 是否归还
	 * @param equSettingName 设备名称
	 * @param searchCondition 模糊匹配条件
	 */
	public void export(HttpServletRequest request, HttpServletResponse response, String receiveType, String beginTime,
			String endTime, String isReturnBack, String equSettingName, String searchCondition);

	/**
	 * @description 获取三年成本按月分组
	 * @return
	 * @throws DoSthException
	 */
	public Map<String,List<MonthCost>> getThrYearPriceSumGroupByMonth() throws DoSthException;
	
	/**
	 * @description 获取当前月物料分组数统计
	 * @return
	 * @throws DoSthException
	 */
	public Map<String, Integer> getCurMonthGroupByMat() throws DoSthException;
	
	/**
	 * @description 近三年按物料类型统计
	 * @return
	 * @throws DoSthException
	 */
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType() throws DoSthException;

	/**
     * @description 获取申请单结果
     * @param applyVoucherResult 申请单序号|领料记录Id1,领料记录Id2
     * @return
     */
	public List<ApplyVoucher> getApplyVoucherResult(String applyVoucherResult);
	
	/**
	 * 同步借出记录
	 * @param cabinetName 刀具柜别名
	 * @param endTime 查询截止时间
	 * @return
	 */
	public ApiFeignResponse<FeignBorrow> getSyncBorrows(String cabinetName, Long endTime);

	/**
	 * @description 按部门统计领取数量分组
	 * @return
	 */
	public Map<String, Integer> getBorrowNumGroupByDept();

	/**
	 * 领用记录邮件发送
	 * 
	 * @param receiveType
	 * @param beginTime
	 * @param endTime
	 * @param isReturnBack
	 * @param equSettingName
	 * @param searchCondition
	 */
	public void sendUseRecord(String receiveType, String beginTime, String endTime, String isReturnBack,
			String equSettingName, String searchCondition);
}