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
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.enums.CabinetType;
import com.dosth.tool.common.dto.CabinetName;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.LowerFrameQueryService;
import com.dosth.tool.service.MainCabinetService;
import com.dosth.tool.service.TempCabinetService;

/**
 * @description 库存Controller
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/stock")
public class StockController extends BaseController {
	private static String PREFIX = "/tool/stock/";
	@Autowired
	private MainCabinetService mainCabinetService;
	@Autowired
	private TempCabinetService tempCabinetService;
	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private LowerFrameQueryService lowerFrameQueryService;

	/**
	 * @description 暂存柜库存显示初始化
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<CabinetName> cabinetList = this.lowerFrameQueryService.getCabinetNameList();
		model.addAttribute("cabinetList", cabinetList);
		return PREFIX + "index.html";
	}

	/**
	 * @description 初始化主柜列表
	 */
	@RequestMapping("/init/{cabinetId}")
	public String init(@PathVariable String cabinetId, Model model) {
		EquSettingCriteria criteria = new EquSettingCriteria();
		criteria.setId(cabinetId);
		List<EquSetting> equSettingList = this.equSettingService.findAllCabinet(criteria);
		EquSetting equSetting = equSettingList.get(0);
		if (equSetting.getCabinetType().equals(CabinetType.SUB_CABINET)
				|| equSetting.getCabinetType().equals(CabinetType.VIRTUAL_WAREHOUSE)
				|| equSetting.getCabinetType().equals(CabinetType.STORE_CABINET)
				|| equSetting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_A)
				|| equSetting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_B)
				|| equSetting.getEquSettingParentId() == null) {
			return PREFIX + "initMain.html";
		} else {
			return PREFIX + "initTemp.html";
		}
	}

	/**
	 * @description 查询列表
	 */
	@RequestMapping("/listMain")
	@ResponseBody
	public Object listMain() {
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
		String cabinetId = super.getPara("cabinetId");
		String matInfo = super.getPara("matInfo");
		String rowNo = super.getPara("rowNo");
		String colNo = super.getPara("colNo");
		Page<EquDetailSta> page = this.mainCabinetService.getEquDetailStaPage(pageNo, pageSize, cabinetId, matInfo,
				rowNo, colNo);
		return page;
	}

	/**
	 * @description 查询列表
	 */
	@RequestMapping("/listTemp")
	@ResponseBody
	public Object listTemp() {
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
		String cabinetId = super.getPara("cabinetId");
		Page<SubBox> page = this.tempCabinetService.getSubBoxPage(pageNo, pageSize, cabinetId);
		return page;
	}

	/**
	 * @description 导出
	 * @param response  servletResponse
	 * @param area      柜体类型
	 * @param cabinetId 柜体Id
	 */
	@RequestMapping("/export/{cabinetId}")
	@ResponseBody
	public Tip export(HttpServletRequest request, HttpServletResponse response, @PathVariable String cabinetId) {
		EquSettingCriteria criteria = new EquSettingCriteria();
		criteria.setId(cabinetId);
		List<EquSetting> equSettingList = this.equSettingService.findAllCabinet(criteria);
		EquSetting equSetting = equSettingList.get(0);
		if (equSetting.getCabinetType().equals(CabinetType.SUB_CABINET)
				|| equSetting.getCabinetType().equals(CabinetType.VIRTUAL_WAREHOUSE)
				|| equSetting.getCabinetType().equals(CabinetType.STORE_CABINET)
				|| equSetting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_A)
				|| equSetting.getCabinetType().equals(CabinetType.KNIFE_CABINET_C_B)
				|| equSetting.getEquSettingParentId() == null) {
			this.mainCabinetService.export(request, response, cabinetId);
		} else {
			this.tempCabinetService.export(request, response, cabinetId);
		}
		return SUCCESS_TIP;
	}
}