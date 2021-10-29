package com.dosth.tool.rpc.Impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.statistics.dto.MonthCost;
import com.dosth.tool.rpc.StatisticsService;
import com.dosth.tool.service.MatUseRecordService;

/**
 * 
 * @description 统计接口实现
 * @author guozhidong
 *
 */
@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	private MatUseRecordService matUseRecordService;
	
	@Override
	public Map<String, List<MonthCost>> getThrYearPriceSumGroupByMonth() {
		return this.matUseRecordService.getThrYearPriceSumGroupByMonth();
	}

	@Override
	public Map<String, Integer> getCurMonthGroupByMat() {
		return this.matUseRecordService.getCurMonthGroupByMat();
	}

	@Override
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType() {
		return this.matUseRecordService.getThrYCntGroupByMatType();
	}
	
	@Override
	public Map<String, Integer> getBorrowNumGroupByDept() {
		return this.matUseRecordService.getBorrowNumGroupByDept();
	}
}