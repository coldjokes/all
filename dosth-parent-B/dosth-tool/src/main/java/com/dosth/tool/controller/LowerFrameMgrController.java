package com.dosth.tool.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.ShiroController;
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.enums.CabinetType;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.vo.EquDetailStaVo;
import com.dosth.tool.entity.vo.SubBoxVo;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.LowerFrameQueryService;
import com.dosth.tool.service.MainCabinetService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.tool.service.TempCabinetService;
import com.dosth.util.OpTip;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description 下架管理Controller
 * 
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/lowerFrameMgr")
public class LowerFrameMgrController extends ShiroController {

	private final static String PREFIX = "/tool/lowerFrameMgr/";

	@Autowired
	private LowerFrameQueryService lowerFrameQueryService;
	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private MainCabinetService mainCabinetService;
	@Autowired
	private TempCabinetService tempCabinetService;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private EquDetailStaService equDetailStaService;

	/**
	 * @description 初始化
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * @description 初始化主柜列表
	 */
	@RequestMapping("/initlist/{cabinetId}")
	public String initlist(@PathVariable String cabinetId, Model model) {
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
	 * @description 跳转到上架页面
	 */
	@RequestMapping("/upFrameView/{equDetailStaId}/{cabinetId}")
	public String upFrameView(@PathVariable String equDetailStaId, @PathVariable String cabinetId, Model model) {
		model.addAttribute("cabinetId", cabinetId);
		model.addAttribute("equDetailStaId", equDetailStaId);
		return PREFIX + "upFrame.html";
	}

	/**
	 * @description 查询上架物料
	 */
	@RequestMapping("/getMatInfoList")
	@ResponseBody
	public Object getMatInfoList(Model model) {
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
		String params = super.getPara("params");
		Page<MatEquInfo> page = this.matEquInfoService.getAllMatInfo(pageNo, pageSize, params);
		return page;
	}

	/**
	 * @description 跳转到批量上架页面
	 */
	@RequestMapping("/upFrameViewAll/{cabinetId}")
	public String upFrameViewAll(@PathVariable String cabinetId, Model model) {
		model.addAttribute("cabinetId", cabinetId);
		return PREFIX + "upFrameAll.html";
	}

	/**
	 * @description 查询批量上架物料
	 */
	@RequestMapping("/getMatInfoListAll")
	@ResponseBody
	public Object getMatInfoListAll(Model model) {
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
		String params = super.getPara("params");
		Page<MatEquInfo> page = this.matEquInfoService.getAllMatInfo(pageNo, pageSize, params);
		return page;
	}

	/**
	 * @description 上架操作
	 */
	@RequestMapping("/upFrame/{equDetailStaId}/{matInfoId}")
	@ResponseBody
	public OpTip upFrame(@PathVariable String equDetailStaId, @PathVariable String matInfoId, Model model) {
		String accountId = super.getShiroAccount().getId();
		OpTip tip = this.equDetailStaService.upFrame(equDetailStaId, matInfoId, accountId);
		return tip;
	}

	/**
	 * @description 批量上架操作
	 */
	@RequestMapping("/upFrameAll/{cabinetId}")
	@ResponseBody
	public OpTip upFrameAll(@PathVariable String cabinetId, HttpServletRequest request, Model model) {
		String accountId = super.getShiroAccount().getId();
		String data = request.getParameter("data");
		JSONArray array = JSONArray.fromObject(data);
		JSONObject obj = null;
		List<String> list = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			list.add(obj.getString("id"));
		}
		OpTip tip = this.equDetailStaService.upFrameAll(accountId, cabinetId, list);
		return tip;
	}

	/**
	 * @description 查询主柜列表
	 */
	@RequestMapping("/mainCabinet")
	@ResponseBody
	public Object mainCabinet() {
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
		Page<EquDetailStaVo> page = this.mainCabinetService.getEquDetailStaVoPage(pageNo, pageSize, cabinetId);
		return page;
	}

	/**
	 * @description 查询暂存柜列表
	 */
	@RequestMapping("/tempCabinet")
	@ResponseBody
	public Object subCabinet() {
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
		Page<SubBoxVo> page = this.tempCabinetService.getSubBoxVoPage(pageNo, pageSize, cabinetId);
		return page;
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
		Page<EquDetailSta> page = this.mainCabinetService.getEquDetailStaPage(pageNo, pageSize, cabinetId, matInfo,rowNo,colNo);
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
	 *
	 * @description 根据设备Id获取设置信息
	 * 
	 * @param equSettingId 设备Id
	 */
	@Deprecated
	@RequestMapping("/getEquInfos/{equSettingId}")
	public String getEquInfos(@PathVariable String equSettingId, Model model) {
		EquSetting equSetting = this.equSettingService.getEquInfos(equSettingId);
		model.addAttribute(equSetting);
		return PREFIX + "equinfos.html";
	}

	/**
	 * @description 下架操作
	 * @param frameId 下架对象Id 主柜弹簧或暂存柜盒子
	 */
	@RequestMapping("/unFrame")
	@ResponseBody
	public OpTip borrowBySetting(HttpServletRequest request, @RequestParam String cabinetId) {
		OpTip tip = new OpTip();
		String accountId = super.getShiroAccount().getId();
		String data = request.getParameter("data");
		JSONObject obj = null;
		JSONArray array = JSONArray.fromObject(data);
		List<String> frameIds = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			frameIds.add(obj.get("id").toString());
		}

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
			tip = this.lowerFrameQueryService.lowMainFrame(frameIds, accountId, cabinetId);
		} else {
			tip = this.lowerFrameQueryService.lowTempFrame(frameIds, accountId, cabinetId);
		}

		return tip;
	}
}