package com.dosth.tool.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.BaseController;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatUseRecord;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.MatUseRecordService;

/**
 * 主柜领用查询Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/mainCabinetQuery")
public class MainCabinetQueryController extends BaseController {

	private static String PREFIX = "/tool/mainCabinetQuery/";

	@Autowired
	private MatUseRecordService matUseRecordService;
	@Autowired
	private EquSettingRepository equSettingRepository;

	/**
	 * 页面初始化
	 */
	@RequestMapping("")
	public String index(Model model) {
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.ne("cabinetType", CabinetType.RECOVERY_CABINET, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.TEM_CABINET, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.KNIFE_CABINET_C, true));
		List<EquSetting> equSettingList = this.equSettingRepository.findAll(criteria);
		model.addAttribute("equSettingList", equSettingList);
		return PREFIX + "index.html";
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/list")
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
		String beginTime = super.getPara("beginTime");
		String endTime = super.getPara("endTime");
		String receiveType = super.getPara("receiveType");
		String isReturnBack = super.getPara("isReturnBack");
		String equSettingName = super.getPara("equSettingName");
		String searchCondition = super.getPara("searchCondition");
		Page<MatUseRecord> page = this.matUseRecordService.getPage(pageNo, pageSize, beginTime, endTime, receiveType,
				isReturnBack, equSettingName, searchCondition);
		return page;
	}

	/**
	 * @description 导出
	 * @param response    servletResponse
	 * @param receiveType 领取类型
	 * @param beginTime   起始时间
	 * @param endTime     截止时间
	 */
	@RequestMapping("/export/{receiveType}/{beginTime}/{endTime}/{isReturnBack}/{equSettingName}/{searchCondition}")
	@ResponseBody
	public Tip export(HttpServletRequest request, HttpServletResponse response, @PathVariable String receiveType,
			@PathVariable String beginTime, @PathVariable String endTime, @PathVariable String isReturnBack,
			@PathVariable String equSettingName, @PathVariable String searchCondition) {
		this.matUseRecordService.export(request, response, receiveType, beginTime, endTime, isReturnBack,
				equSettingName, searchCondition);
		return SUCCESS_TIP;
	}
}