package com.dosth.tool.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.app.dto.FeignUser;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.warpper.DailyLimitPageWarpper;
import com.dosth.tool.entity.DailyLimit;
import com.dosth.tool.service.DailyLimitService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.util.OpTip;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 每日限额
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/dailyLimit")
public class DailyLimitController extends BaseController {

	private static String PREFIX = "/tool/dailyLimit/";

	@Autowired
	private DailyLimitService dailyLimitService;
	@Autowired
	private UserService userService;

	/**
	 * 跳转到班次设定列表的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new DailyLimitPageWarpper(null).createColumns(TableSelectType.RADIO));
		model.addAttribute("deptUserGroup", this.userService.group());
		return PREFIX + "index.html";
	}

	/**
	 * 查询
	 */
	@RequestMapping("/search/{accountId}")
	public String search(@PathVariable String accountId, Model model) {
		ViewUser user = this.userService.getViewUser(accountId);
		List<DailyLimit> limitList = this.dailyLimitService.findAllByAccountId(accountId);
		model.addAttribute("limitList", limitList);
		model.addAttribute("netPath", ToolProperties.PREFIX);
		model.addAttribute("limitSumNum", user.getLimitSumNum());
		model.addAttribute("notReturnLimitNum", user.getNotReturnLimitNum());
		model.addAttribute("startTime", user.getStartTime());
		model.addAttribute("endTime", user.getEndTime());
		return PREFIX + "edit.html";
	}
	
	/**
	 * 同步
	 */
	@RequestMapping("/dataSync/{accountId}")
	@ResponseBody
	public OpTip dataSync(@PathVariable String accountId) {
		this.userService.getViewUser(accountId);
		OpTip tip = this.dailyLimitService.dataSyncByAccountId(accountId);
		return tip;
	}

	/**
	 * 获取限额时间
	 */
	@ResponseBody
	@RequestMapping("/searchUser/{accountId}")
	public ViewUser searchUser(@PathVariable String accountId) {
		ViewUser user = this.userService.getViewUser(accountId);
		return user;
	}

	/**
	 * 保存
	 */
	@RequestMapping("/saveDailyLimit")
	@ResponseBody
	public OpTip saveDailyLimit(HttpServletRequest request) {
		String data = request.getParameter("data");
		JSONObject array = JSONObject.fromObject(data);
		String startTime = array.getString("startTime");
		String endTime = array.getString("endTime");
		int limitSumNum = Integer.parseInt(array.getString("limitSumNum"));
		int notReturnLimitNum = Integer.parseInt(array.getString("notReturnLimitNum"));
		String accountId = array.getString("accountId");
		FeignUser feignUser = new FeignUser(accountId, limitSumNum, notReturnLimitNum, startTime, endTime);
		JSONArray arr = array.getJSONArray("limitListArr");
		Map<String, DailyLimit> limitMap = new HashMap<>();
		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = arr.getJSONObject(i);
			limitMap.put(obj.getString("limitId"),
					new DailyLimit(obj.getInt("limitNum"), obj.getInt("notReturnNum")));
		}
		OpTip tip = this.dailyLimitService.saveDailyLimit(limitMap, feignUser);
		return tip;
	}
}