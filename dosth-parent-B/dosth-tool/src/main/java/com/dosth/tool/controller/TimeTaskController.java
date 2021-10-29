package com.dosth.tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.entity.TimeTask;
import com.dosth.tool.service.TimeTaskService;
import com.dosth.util.OpTip;

/**
 * 定时任务
 * 
 * @author Weifeng.Li
 *
 */
@Controller
@RequestMapping("/timeTask")
public class TimeTaskController extends BaseController {

	private static String PREFIX = "/tool/timeTask/";

	@Autowired
	private TimeTaskService timeTaskSvc;

	/**
	 * 跳转到主页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * 列表
	 * 
	 * @return
	 */
	@GetMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {

		}
		Page<TimeTask> page = this.timeTaskSvc.getPage(pageNo, pageSize);
		return page;
	}

	/**
	 * 任务状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping("/statusSet/{id}/{status}")
	@ResponseBody
	public OpTip statusSet(@PathVariable("id") String id, @PathVariable("status") String status) {
		OpTip tip = new OpTip(200, "设置成功");
		TimeTask timeTask = this.timeTaskSvc.get(id);
		timeTask.setStatus(status.equals("0") ? OnOrOff.ON : OnOrOff.OFF);
		this.timeTaskSvc.update(timeTask);
		return tip;
	}
}
