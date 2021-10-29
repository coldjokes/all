package com.dosth.tool.controller;

import java.io.IOException;
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

import com.dosth.common.controller.BaseController;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.SubCabinetBill;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.SubCabinetBillService;

/**
 * 副柜领用查询Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/viceCabinetQuery")
public class ViceCabinetQueryController extends BaseController {

	private static String PREFIX = "/tool/viceCabinetQuery/";

	@Autowired
	private SubCabinetBillService subCabinetBillService;
	@Autowired
	private EquSettingRepository equSettingRepository;

	/**
	 * 跳转到补料查询的页面
	 */
	@RequestMapping("")

	public String index(Model model) {
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("cabinetType", CabinetType.TEM_CABINET, true));
		List<EquSetting> subBoxNameList = this.equSettingRepository.findAll(criteria);
		model.addAttribute("subBoxNameList", subBoxNameList);
		return PREFIX + "index.html";
	}

	/**
	 * 副柜领用查询列表
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
		String info = super.getPara("info");
		String subBoxName = super.getPara("subBoxName");
		String inOrOut=super.getPara("inOrOut");
		Page<SubCabinetBill> page = this.subCabinetBillService.getPage(pageNo, pageSize, beginTime, endTime, info,subBoxName,inOrOut);
		return page;
	}

	/**
	 * @description 暂存柜领用记录查询
	 * @param
	 */
	@RequestMapping("/infoExport/{params}")
	public String infoExport(HttpServletRequest request, HttpServletResponse response, @PathVariable String[] params)
			throws IOException {

		String beginTime = params[0];
		String endTime = params[1];
		String info = params[2];
		String subBoxName=params[3];
		String status=params[4];
		return this.subCabinetBillService.infoExport(request, response, beginTime, endTime, info,subBoxName,status);
	}
}
