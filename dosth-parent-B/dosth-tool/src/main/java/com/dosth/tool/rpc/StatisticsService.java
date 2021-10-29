package com.dosth.tool.rpc;

import java.util.List;
import java.util.Map;

import com.dosth.statistics.dto.MonthCost;

/**
 * 
 * @description 统计接口
 * @author guozhidong
 *
 */
public interface StatisticsService {
	/**
	 * @description 近三年成本趋势
	 * @return
	 */
	public Map<String, List<MonthCost>> getThrYearPriceSumGroupByMonth();
	
	/**
	 * @description 当前月物料数量分布
	 * @return
	 */
	public Map<String, Integer> getCurMonthGroupByMat();
	
	/**
	 * @description 三年物料类型分布
	 * @return
	 */
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType();
	
	/**
	 * @description 按部门统计领取数量分组
	 * @return
	 */
	public Map<String, Integer> getBorrowNumGroupByDept();
}