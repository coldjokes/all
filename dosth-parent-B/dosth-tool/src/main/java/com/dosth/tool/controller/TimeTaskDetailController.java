package com.dosth.tool.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.BaseController;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.service.TimeTaskDetailService;
import com.dosth.util.OpTip;

import net.sf.json.JSONObject;

/**
 * 定时任务
 * 
 * @author Weifeng.Li
 *
 */
@Controller
@RequestMapping("/timeTaskDetail")
public class TimeTaskDetailController extends BaseController {

	@Autowired
	private TimeTaskDetailService timeTaskDetailSvc;

	@RequestMapping("/getDetail")
	@ResponseBody
	public TimeTaskDetail getDetail() {
		return this.timeTaskDetailSvc.getDetail();
	}

	@RequestMapping("/saveAndFlush")
	@ResponseBody
	public OpTip saveAndFlush(HttpServletRequest request) {
		String data = request.getParameter("data");
		JSONObject array = JSONObject.fromObject(data);
		String id = array.getString("id");
		String accountId = array.getString("accountId");
		String executionTime = array.getString("executionTime");
		TimeTaskDetail timeTaskDetail = new TimeTaskDetail();
		timeTaskDetail.setId(id);
		timeTaskDetail.setAccountId(accountId);
		timeTaskDetail.setExecutionTime(executionTime);
		timeTaskDetail.setCronExpression("0 0 " + executionTime + " * * ?");
		timeTaskDetail.setJobId("1");
		timeTaskDetail.setJobGroup("REPORT");
		return this.timeTaskDetailSvc.saveAndFlush(timeTaskDetail);
	}
}