package com.dosth.admin.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.admin.entity.LoginLog;
import com.dosth.admin.service.LoginLogService;
import com.dosth.admin.warpper.LoginLogPageWarpper;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.controller.BaseController;

/**
 * 登录日志
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

	private static String PREFIX = "/admin/log/";
	
	@Autowired
	private LoginLogService loginLogService;

	/**
	 * 跳转到登录日志的页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new LoginLogPageWarpper(null).createColumns(TableSelectType.RADIO));
		return PREFIX + "loginlog.html";
	}
	
	/**
	 * 查询日志列表
	 */
	@RequestMapping("/list")
//	@Permission
	@ResponseBody
	public Object list() {
		int pageNo = 1; 
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		try {
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		String name = super.getPara("name"); 
		String logName = super.getPara("logName"); 
		String beginTime = super.getPara("beginTime");
		String endTime = super.getPara("endTime");
		Page<LoginLog> page = this.loginLogService.getPage(pageNo, pageSize, name, logName, beginTime, endTime);
		Map<String, Object> map = new LoginLogPageWarpper(page).invokeObjToMap();
		
//		this.loginLogRepository.findAll(SortTools.basicSort("desc", "id"));
		
		return map;
	}
	
}

