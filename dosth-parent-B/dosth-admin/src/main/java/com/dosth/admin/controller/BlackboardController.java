package com.dosth.admin.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.entity.Dept;
import com.dosth.admin.rpc.ToolService;
import com.dosth.admin.service.DeptService;
import com.dosth.admin.service.NoticeService;
import com.dosth.common.util.NumUtil;
import com.dosth.statistics.dto.MonthCost;
import com.dosth.statistics.enums.MonthEnum;

/**
 * 总览信息
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/blackboard")
public class BlackboardController {

	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private ToolService toolService;
	
	@Autowired
	private DeptService deptService;

	/**
	 * 跳转到黑板
	 */
	@RequestMapping("")
	public String blackboard(Model model) {
		List<Map<String, Object>> notices = this.noticeService.list(null);
		model.addAttribute("noticeList", notices);
		return "/blackboard.html";
	}
	
	/**
	 * @description 三年同期趋势图
	 * @return
	 */
	@RequestMapping("/getCompThrYear")
	@ResponseBody
	public Map<String, List<MonthData>> getCompThrYear() {
		Map<String, List<MonthData>> map = new LinkedHashMap<>();
		Map<String, List<MonthCost>> rpcMap = this.toolService.getThrYearPriceSumGroupByMonth();
		List<MonthData> list;
		for (Entry<String, List<MonthCost>> entry : rpcMap.entrySet()) {
			list = map.get(entry.getKey());
			if (list == null) {
				list = new ArrayList<>();
			}
			for (MonthEnum month : MonthEnum.values()) {
				for (MonthCost cost : entry.getValue()) {
					if (month.equals(cost.getMonthEnum())) {
						list.add(new MonthData(month.cn(), NumUtil.keep2PointZero(cost.getCost())));
						continue;
					}
				}
			}
			map.put(entry.getKey(), list);
		}
		return map;
	}
	
	/**
	 * @description 当前月饼型图
	 * @return
	 */
	@RequestMapping("/getCurMonthPie")
	@ResponseBody
	public Map<String, Integer> getCurMonthPie() {
		Map<String, Integer> map = this.toolService.getCurMonthGroupByMat();
		return map;
	}
	
	/**
	 * @description 获取三年统计物料类型分组统计
	 * @return
	 */
	@RequestMapping("/getThrYCntGroupByMatType")
	@ResponseBody
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType() {
		return this.toolService.getThrYCntGroupByMatType();
	}

	/**
	 * @description 按部门统计领取数量分组
	 * @return
	 */
	@RequestMapping("/getBorrowNumGroupByDept")
	@ResponseBody
	public Map<String, Integer> getBorrowNumGroupByDept() {
		Map<String, Integer> map = this.toolService.getBorrowNumGroupByDept();
		Map<String, Integer> result = new HashMap<>();
		Dept dept;
		for (Entry<String, Integer> entry : map.entrySet()) {
			dept = this.deptService.get(entry.getKey());
			result.put(dept.getDeptName(), entry.getValue());
		}		
		return result;
	}
}

/**
 * 
 * @description 月份数据
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
class MonthData implements Serializable {
	private String month;
	private String cost;

	public MonthData() {
	}

	public MonthData(String month, String cost) {
		this.month = month;
		this.cost = cost;
	}

	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getCost() {
		return this.cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}