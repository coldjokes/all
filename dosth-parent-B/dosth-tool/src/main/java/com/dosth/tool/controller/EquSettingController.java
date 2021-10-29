package com.dosth.tool.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.ErrorTip;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.ShiroController;
import com.dosth.common.node.ZTreeNode;
import com.dosth.criteria.EquSettingCriteria;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.TrueOrFalse;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.CabinetPlcSetting;
import com.dosth.tool.entity.CabinetSetup;
import com.dosth.tool.entity.EquDetail;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.FeedingDetail;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.LockParam;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.vo.EquDetailStaVo;
import com.dosth.tool.service.CabinetPlcSettingService;
import com.dosth.tool.service.CabinetSetupService;
import com.dosth.tool.service.EquDetailService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.FeedingListService;
import com.dosth.tool.service.LockParamService;
import com.dosth.tool.service.SmartCabinetService;
import com.dosth.tool.service.SubBoxService;
import com.dosth.tool.service.TrolDrawerService;
import com.dosth.tool.service.UserService;
import com.dosth.util.OpTip;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 柜体管理Controller
 * 
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/equsetting")
public class EquSettingController extends ShiroController {

	private static String PREFIX = "/tool/equsetting/";

	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private FeedingListService feedingListService;
	@Autowired
	private FeedingDetailService feedingDetailService;
	@Autowired
	private CabinetSetupService cabinetSetupService;
	@Autowired
	private LockParamService lockParamService;
	@Autowired
	private SubBoxService subBoxService;
	@Autowired
	private EquDetailService equDetailService;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private CabinetPlcSettingService cabinetPlcSettingService;
	@Autowired
	private SmartCabinetService smartCabinetService;
	@Autowired
	private TrolDrawerService trolDrawerService;
	@Autowired
	private UserService userService;

	/**
	 * 跳转到主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * 查询-主表
	 */
	@GetMapping("/list")
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
		String cabinetType = super.getPara("cabinetType");
		Page<EquSetting> page = this.equSettingService.getPage(pageNo, pageSize, name, cabinetType);
		return page;
	}

	/**
	 * 查询-子表
	 */
	@GetMapping("/sublist")
	@ResponseBody
	public Object sublist() {
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
		String equSettingParentId = super.getPara("equSettingParentId");
		Page<EquSetting> page = this.equSettingService.getsubPage(pageNo, pageSize, equSettingParentId);
		return page;
	}

	/**
	 * @Desc 跳转到-添加主柜
	 */
	@RequestMapping("/addView")
	public String addView(Model model) {
		model.addAttribute(new EquSetting());
		model.addAttribute("deptUserGroup", this.userService.group());
		return PREFIX + "edit.html";
	}

	/**
	 * @Desc 跳转到-添加子柜
	 */
	@RequestMapping("/addInfoView/{equSettingParentId}")
	public String addInfoView(@PathVariable String equSettingParentId, Model model) {
		EquSetting equSetting = new EquSetting();
		equSetting.setEquSettingParentId(equSettingParentId);
		model.addAttribute(equSetting);
		model.addAttribute("deptUserGroup", this.userService.group());
		return PREFIX + "editInfo.html";
	}

	/**
	 * 添加-主柜/子柜
	 */
	@RequestMapping("/add")
	@ResponseBody
	public OpTip add(@Valid EquSetting equSetting) {
		OpTip tip = new OpTip(200, "添加成功");
		EquSetting setting = this.equSettingService.getEquSettingBySerialNo(equSetting.getSerialNo());
		if (setting != null) {
			if (equSetting.getSerialNo().equals(setting.getSerialNo())) {
				return new OpTip(201, "序列号重复");
			}
		}
		this.equSettingService.save(equSetting);
		return tip;
	}

	/**
	 * 跳转到-修改主柜页面
	 */
	@RequestMapping("/editView/{equSettingId}")
	public String editView(@PathVariable String equSettingId, Model model) {
		EquSetting equSetting = this.equSettingService.get(equSettingId);
		model.addAttribute(equSetting);
		model.addAttribute("userName", ViewUserUtil.createViewUserName(equSetting.getUser()));
		model.addAttribute("deptUserGroup", this.userService.group());
		LogObjectHolder.me().set(equSetting);
		return PREFIX + "edit.html";
	}

	/**
	 * 跳转到-修改子柜页面
	 */
	@RequestMapping("/editInfoView/{equSettingId}")
	public String editInfoView(@PathVariable String equSettingId, Model model) {
		EquSetting equSetting = this.equSettingService.get(equSettingId);
		model.addAttribute(equSetting);
		model.addAttribute("userName", ViewUserUtil.createViewUserName(equSetting.getUser()));
		model.addAttribute("deptUserGroup", this.userService.group());
		//回显switch
		if (CabinetType.TEM_CABINET.equals(equSetting.getCabinetType())) {
			CabinetSetup cabinetSetup = this.cabinetSetupService.getShareSwitch(equSettingId);
			model.addAttribute("shareSwitch", cabinetSetup.getSetupValue() != null ? cabinetSetup.getSetupValue() : "");
		}

		LogObjectHolder.me().set(equSetting);
		return PREFIX + "editInfo.html";
	}

	/**
	 * 修改-主柜/子柜
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public OpTip edit(@Valid EquSetting equSetting, BindingResult result) {
		OpTip tip = new OpTip(200, "修改成功");
		EquSetting tmpEquSetting = this.equSettingService.get(equSetting.getId());
		if (equSetting.getSerialNo().equals(tmpEquSetting.getSerialNo())
				&& !equSetting.getId().equals(tmpEquSetting.getId())) {
			return new OpTip(201, "序列号重复");
		}
		//查询并修改switch
		CabinetSetup cabinetSetup = this.cabinetSetupService.getShareSwitch(equSetting.getId());
		if (cabinetSetup != null && cabinetSetup.getSetupValue().equals(TrueOrFalse.TRUE.toString())) {
			cabinetSetup.setSetupValue(TrueOrFalse.FALSE.toString());
		} else {
			cabinetSetup.setSetupValue(TrueOrFalse.TRUE.toString());
		}
		this.cabinetSetupService.update(cabinetSetup);
		try {
			BeanUtils.copyProperties(tmpEquSetting, equSetting);
			this.equSettingService.update(tmpEquSetting);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * 启用/禁用-主柜
	 */
	@RequestMapping("/update")
	@BussinessLog(businessName = "启用/禁用")
	@ResponseBody
	public Tip update(@RequestParam("equSettingId") String equSettingId, @RequestParam("status") String status) {
		EquSetting equSetting = this.equSettingService.get(equSettingId);
		if (status != null && "0".equals(status)) {
			equSetting.setStatus(UsingStatus.ENABLE);
		} else {
			equSetting.setStatus(UsingStatus.DISABLE);
		}
		this.equSettingService.update(equSetting);
		return SUCCESS_TIP;
	}

	/**
	 * 跳转到-参数页面-主柜/子柜
	 * 
	 * @return
	 */
	@RequestMapping("/setupView/{equSettingId}")
	public String setupView(@PathVariable String equSettingId, Model model) {
		model.addAttribute(new CabinetSetup());
		model.addAttribute("equSettingId", equSettingId);

		// 查询配置
		Map<String, CabinetSetup> map = this.cabinetSetupService.getCabinetSetupByEquSettingId(equSettingId);
		model.addAttribute("map", map);

		// 查询刀具柜类型
		EquSettingCriteria equSettingCriteria = new EquSettingCriteria();
		equSettingCriteria.setId(equSettingId);
		List<EquSetting> equSettingList = this.equSettingService.findAllCabinet(equSettingCriteria);

		CabinetType cabinetType = null;
		CabinetType parentCabinetType = null;
		if (equSettingList != null && equSettingList.size() > 0) {
			cabinetType = equSettingList.get(0).getCabinetType();
			if (equSettingList.get(0).getEquSettingParent() != null) {
				parentCabinetType = equSettingList.get(0).getEquSettingParent().getCabinetType();
			}
		}

		// 根据刀具柜类型跳转不同页面
		switch (cabinetType) {
		case KNIFE_CABINET_PLC:
			model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_PLC);
			return PREFIX + "plcSetup.html";
		case KNIFE_CABINET_DETA:
			model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_DETA);
			return PREFIX + "detSetup.html";
		case KNIFE_CABINET_DETB:
			model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_DETB);
			return PREFIX + "detSetup.html";
		case KNIFE_CABINET_C:
			model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_C);
			return PREFIX + "cSetup.html";
		case KNIFE_CABINET_C_A:
			model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_C_A);
			return PREFIX + "aSetup.html";
		case KNIFE_CABINET_C_B:
			model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_C_B);
			return PREFIX + "bSetup.html";
		case TROL_DRAWER:
			List<LockParam> trolList = this.lockParamService.getLockParamByEquSettingId(equSettingId,
					CabinetType.TROL_DRAWER);
			model.addAttribute("trolList", trolList);
			model.addAttribute("cabinetType", CabinetType.TROL_DRAWER);
			return PREFIX + "trolSetup.html";
		case TEM_CABINET:
			List<LockParam> temList = this.lockParamService.getLockParamByEquSettingId(equSettingId,
					CabinetType.TEM_CABINET);
			model.addAttribute("temList", temList);
			model.addAttribute("cabinetType", CabinetType.TEM_CABINET);
			return PREFIX + "temSetup.html";
		case STORE_CABINET:
			List<LockParam> stoList = this.lockParamService.getLockParamByEquSettingId(equSettingId,
					CabinetType.STORE_CABINET);
			model.addAttribute("stoList", stoList);
			model.addAttribute("cabinetType", CabinetType.STORE_CABINET);
			return PREFIX + "stoSetup.html";
		case VIRTUAL_WAREHOUSE:
			model.addAttribute("cabinetType", CabinetType.VIRTUAL_WAREHOUSE);
			return PREFIX + "virSetup.html";
		case RECOVERY_CABINET:
			model.addAttribute("cabinetType", CabinetType.RECOVERY_CABINET);
			return PREFIX + "recSetup.html";
		case SUB_CABINET:
			switch (parentCabinetType) {
			case KNIFE_CABINET_PLC:
				model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_PLC);
				return PREFIX + "plcSetup.html";
			case KNIFE_CABINET_DETA:
				model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_DETA);
				return PREFIX + "detSetup.html";
			case KNIFE_CABINET_DETB:
				model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_DETB);
				return PREFIX + "detSetup.html";
			case KNIFE_CABINET_C:
				model.addAttribute("cabinetType", CabinetType.KNIFE_CABINET_C);
				return PREFIX + "cSetup.html";
			default:
				break;
			}
		default:
			break;
		}
		return "";
	}

	/**
	 * 参数
	 */
	@RequestMapping("/editSetup")
	@ResponseBody
	public OpTip editSetup(HttpServletRequest request) {
		OpTip tip = new OpTip(200, "设置成功");
		String data = request.getParameter("data");
		JSONArray array = JSONArray.fromObject(data);

		JSONObject obj = null;
		String equSettingId = null;
		String cabinetType = null;

		// 公用参数
		String FACE_LOGIN = null;
		String PRINT_COM = null;
		String PRINT_CUT = null;
		String PRINT_TYPE_CODE = null;
		String SCAN_COM = null;
		String SCAN_TYPE = null;
		String SCAN_BAUD = null;

		// PLC参数
		String PLC_IP = null;
		String PLC_PORT = null;

		// 行列式参数
		String DET_COM = null;
		String DET_BAUD = null;
		String DET_BOARD_NO = null;
		String DET_DOOR_HEIGHT = null;
		String DET_LEVEL_HEIGHT = null;
		String DET_LEVEL_SPACING = null;

		// C型柜参数
		String DET_BOARD_NO_A = null;
		String DET_BOARD_NO_B = null;

		// 暂存柜参数
		String TEM_COM = null;
		String TEM_BAUD = null;
		String TEM_BOARD_NO = null;
		String TEM_ROW_NO = null;
		String TEM_COL_NO = null;

		// 暂存柜锁控板参数
		Map<Integer, String[]> temSetupMap = new HashMap<>();

		// 回收柜参数
		String REC_SCAN_COM = null;
		String REC_SCAN_TYPE = null;
		String REC_SCAN_BAUD = null;

		// 储物柜参数
		String STORE_COM = null;
		String STORE_BAUD = null;
		String STORE_BOARD_NO = null;
		String STORE_ROW_NO = null;
		String STORE_COL_NO = null;

		// 储物柜锁控板参数
		Map<Integer, String[]> stoSetupMap = new HashMap<>();

		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			equSettingId = obj.getString("equSettingId");
			cabinetType = obj.getString("cabinetType");

			switch (cabinetType) {
			case "KNIFE_CABINET_PLC":
			case "KNIFE_CABINET_DETA":
			case "KNIFE_CABINET_DETB":
			case "KNIFE_CABINET_C":
				// 公用参数
				FACE_LOGIN = obj.getString(SetupKey.Public.FACE_LOGIN);
				PRINT_COM = obj.getString(SetupKey.Public.PRINT_COM);
				PRINT_CUT = obj.getString(SetupKey.Public.PRINT_CUT);
				PRINT_TYPE_CODE = obj.getString(SetupKey.Public.PRINT_TYPE_CODE);
				SCAN_COM = obj.getString(SetupKey.Public.SCAN_COM);
				SCAN_TYPE = obj.getString(SetupKey.Public.SCAN_TYPE);
				SCAN_BAUD = obj.getString(SetupKey.Public.SCAN_BAUD);

				switch (cabinetType) {
				case "KNIFE_CABINET_PLC":
					// PLC参数
					PLC_IP = obj.getString(SetupKey.Plc.PLC_IP);
					PLC_PORT = obj.getString(SetupKey.Plc.PLC_PORT);
					break;
				case "KNIFE_CABINET_DETA":
				case "KNIFE_CABINET_DETB":
					// 行列式参数
					DET_COM = obj.getString(SetupKey.Det.DET_COM);
					DET_BAUD = obj.getString(SetupKey.Det.DET_BAUD);
					DET_BOARD_NO = obj.getString(SetupKey.Det.DET_BOARD_NO);
					DET_DOOR_HEIGHT = obj.getString(SetupKey.Det.DET_DOOR_HEIGHT);
					DET_LEVEL_HEIGHT = obj.getString(SetupKey.Det.DET_LEVEL_HEIGHT);
					DET_LEVEL_SPACING = obj.getString(SetupKey.Det.DET_LEVEL_SPACING);
					break;
				case "KNIFE_CABINET_C":
					DET_COM = obj.getString(SetupKey.Det.DET_COM);
					DET_BAUD = obj.getString(SetupKey.Det.DET_BAUD);
					DET_DOOR_HEIGHT = obj.getString(SetupKey.Det.DET_DOOR_HEIGHT);
					DET_LEVEL_HEIGHT = obj.getString(SetupKey.Det.DET_LEVEL_HEIGHT);
					DET_LEVEL_SPACING = obj.getString(SetupKey.Det.DET_LEVEL_SPACING);
					break;
				default:
					break;
				}
				break;
			case "KNIFE_CABINET_C_A":
				DET_BOARD_NO_A = obj.getString(SetupKey.CCabinet.DET_BOARD_NO_A);
				break;
			case "KNIFE_CABINET_C_B":
				DET_BOARD_NO_B = obj.getString(SetupKey.CCabinet.DET_BOARD_NO_B);
				break;
			case "TEM_CABINET":
				// 暂存柜参数
				TEM_COM = obj.getString(SetupKey.TemCabinet.TEM_COM);
				TEM_BAUD = obj.getString(SetupKey.TemCabinet.TEM_BAUD);
				TEM_BOARD_NO = obj.getString(SetupKey.TemCabinet.TEM_BOARD_NO);
				TEM_ROW_NO = obj.getString(SetupKey.TemCabinet.TEM_ROW_NO);
				TEM_COL_NO = obj.getString(SetupKey.TemCabinet.TEM_COL_NO);

				// 暂存柜锁控板参数
				String[] temSetupArray = new String[3];
				temSetupArray[0] = TEM_BOARD_NO;
				temSetupArray[1] = TEM_ROW_NO;
				temSetupArray[2] = TEM_COL_NO;
				temSetupMap.put(i, temSetupArray);
				break;
			case "STORE_CABINET":
				// 储物柜参数
				STORE_COM = obj.getString(SetupKey.StoreCabinet.STORE_COM);
				STORE_BAUD = obj.getString(SetupKey.StoreCabinet.STORE_BAUD);
				STORE_BOARD_NO = obj.getString(SetupKey.StoreCabinet.STORE_BOARD_NO);
				STORE_ROW_NO = obj.getString(SetupKey.StoreCabinet.STORE_ROW_NO);
				STORE_COL_NO = obj.getString(SetupKey.StoreCabinet.STORE_COL_NO);

				// 储物柜锁控板参数
				String[] stoSetupArray = new String[3];
				stoSetupArray[0] = STORE_BOARD_NO;
				stoSetupArray[1] = STORE_ROW_NO;
				stoSetupArray[2] = STORE_COL_NO;
				stoSetupMap.put(i, stoSetupArray);
				break;
			case "RECOVERY_CABINET":
				REC_SCAN_COM = obj.getString(SetupKey.RecCabinet.REC_SCAN_COM);
				REC_SCAN_TYPE = obj.getString(SetupKey.RecCabinet.REC_SCAN_TYPE);
				REC_SCAN_BAUD = obj.getString(SetupKey.RecCabinet.REC_SCAN_BAUD);
				break;
			default:
				break;
			}
		}

		// 查询配置
		Map<String, CabinetSetup> map = this.cabinetSetupService.getCabinetSetupByEquSettingId(equSettingId);

		switch (cabinetType) {
		case "KNIFE_CABINET_PLC":
		case "KNIFE_CABINET_DETA":
		case "KNIFE_CABINET_DETB":
		case "KNIFE_CABINET_C":
			// 公用参数
			// 人脸识别登录
			if (map.get(SetupKey.Public.FACE_LOGIN) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.FACE_LOGIN);
				cabinetSetup.setSetupValue(FACE_LOGIN);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Public.FACE_LOGIN, FACE_LOGIN));
			}

			// 打印机COM口
			if (map.get(SetupKey.Public.PRINT_COM) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.PRINT_COM);
				cabinetSetup.setSetupValue(PRINT_COM);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Public.PRINT_COM, PRINT_COM));
			}

			// 打印机是否自动切刀
			if (map.get(SetupKey.Public.PRINT_CUT) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.PRINT_CUT);
				cabinetSetup.setSetupValue(PRINT_CUT);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Public.PRINT_CUT, PRINT_CUT));
			}

			// 打印条码类型
			if (map.get(SetupKey.Public.PRINT_TYPE_CODE) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.PRINT_TYPE_CODE);
				cabinetSetup.setSetupValue(PRINT_TYPE_CODE);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.Public.PRINT_TYPE_CODE, PRINT_TYPE_CODE));
			}

			// 扫描仪COM口
			if (map.get(SetupKey.Public.SCAN_COM) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.SCAN_COM);
				cabinetSetup.setSetupValue(SCAN_COM);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Public.SCAN_COM, SCAN_COM));
			}

			// 扫描仪类型
			if (map.get(SetupKey.Public.SCAN_TYPE) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.SCAN_TYPE);
				cabinetSetup.setSetupValue(SCAN_TYPE);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Public.SCAN_TYPE, SCAN_TYPE));
			}

			// 描仪波特率
			if (map.get(SetupKey.Public.SCAN_BAUD) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.Public.SCAN_BAUD);
				cabinetSetup.setSetupValue(SCAN_BAUD);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Public.SCAN_BAUD, SCAN_BAUD));
			}

			switch (cabinetType) {
			case "KNIFE_CABINET_PLC":
				// PLC参数
				// ip地址
				if (map.get(SetupKey.Plc.PLC_IP) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Plc.PLC_IP);
					cabinetSetup.setSetupValue(PLC_IP);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Plc.PLC_IP, PLC_IP));
				}

				// 端口
				if (map.get(SetupKey.Plc.PLC_PORT) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Plc.PLC_PORT);
					cabinetSetup.setSetupValue(PLC_PORT);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Plc.PLC_PORT, PLC_PORT));
				}
				break;
			case "KNIFE_CABINET_DETA":
			case "KNIFE_CABINET_DETB":
			case "KNIFE_CABINET_C":
				// 行列式参数
				// 层高
				List<EquDetail> detDetailList = this.equDetailService.getEquDetailListBySettingId(equSettingId);
				for (EquDetail equDetail : detDetailList) {
					equDetail.setLevelHt((equDetail.getRowNo() - 1) * Integer.valueOf(DET_LEVEL_SPACING)
							+ Integer.valueOf(DET_LEVEL_HEIGHT));
					this.equDetailService.update(equDetail);
				}

				if (!cabinetType.equals("KNIFE_CABINET_C")) {
					// 栈号
					List<EquDetailSta> detDetailStaList = this.equDetailStaService
							.getEquDetailStaListByCabinetId(equSettingId);
					for (EquDetailSta equDetailSta : detDetailStaList) {
						equDetailSta.setBoardNo(Integer.valueOf(DET_BOARD_NO));
						equDetailSta.setLockIndex(
								(equDetailSta.getEquDetail().getRowNo() - 1) * 12 + equDetailSta.getColNo());
						this.equDetailStaService.update(equDetailSta);
					}

					// 栈号
					if (map.get(SetupKey.Det.DET_BOARD_NO) != null) {
						CabinetSetup cabinetSetup = map.get(SetupKey.Det.DET_BOARD_NO);
						cabinetSetup.setSetupValue(DET_BOARD_NO);
						this.cabinetSetupService.update(cabinetSetup);
					} else {
						this.cabinetSetupService
								.save(new CabinetSetup(equSettingId, SetupKey.Det.DET_BOARD_NO, DET_BOARD_NO));
					}
				}

				// COM口
				if (map.get(SetupKey.Det.DET_COM) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Det.DET_COM);
					cabinetSetup.setSetupValue(DET_COM);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Det.DET_COM, DET_COM));
				}

				// 波特率
				if (map.get(SetupKey.Det.DET_BAUD) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Det.DET_BAUD);
					cabinetSetup.setSetupValue(DET_BAUD);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.Det.DET_BAUD, DET_COM));
				}

				// 门高
				if (map.get(SetupKey.Det.DET_DOOR_HEIGHT) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Det.DET_DOOR_HEIGHT);
					cabinetSetup.setSetupValue(DET_DOOR_HEIGHT);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService
							.save(new CabinetSetup(equSettingId, SetupKey.Det.DET_DOOR_HEIGHT, DET_DOOR_HEIGHT));
				}

				// 层高
				if (map.get(SetupKey.Det.DET_LEVEL_HEIGHT) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Det.DET_LEVEL_HEIGHT);
					cabinetSetup.setSetupValue(DET_LEVEL_HEIGHT);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService
							.save(new CabinetSetup(equSettingId, SetupKey.Det.DET_LEVEL_HEIGHT, DET_LEVEL_HEIGHT));
				}

				// 层间距
				if (map.get(SetupKey.Det.DET_LEVEL_SPACING) != null) {
					CabinetSetup cabinetSetup = map.get(SetupKey.Det.DET_LEVEL_SPACING);
					cabinetSetup.setSetupValue(DET_LEVEL_SPACING);
					this.cabinetSetupService.update(cabinetSetup);
				} else {
					this.cabinetSetupService
							.save(new CabinetSetup(equSettingId, SetupKey.Det.DET_LEVEL_SPACING, DET_LEVEL_SPACING));
				}
				break;
			default:
				break;
			}
			break;
		case "KNIFE_CABINET_C_A":
			// 栈号
			List<EquDetailSta> detDetailAStaList = this.equDetailStaService
					.getEquDetailStaListByCabinetId(equSettingId);
			for (EquDetailSta equDetailSta : detDetailAStaList) {
				equDetailSta.setBoardNo(Integer.valueOf(DET_BOARD_NO_A));
				equDetailSta.setLockIndex((equDetailSta.getEquDetail().getRowNo() - 1) * 12 + equDetailSta.getColNo());
				this.equDetailStaService.update(equDetailSta);
			}

			// 栈号
			if (map.get(SetupKey.CCabinet.DET_BOARD_NO_A) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.CCabinet.DET_BOARD_NO_A);
				cabinetSetup.setSetupValue(DET_BOARD_NO_A);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.CCabinet.DET_BOARD_NO_A, DET_BOARD_NO_A));
			}
			break;
		case "KNIFE_CABINET_C_B":
			// 栈号
			List<EquDetailSta> detDetailBStaList = this.equDetailStaService
					.getEquDetailStaListByCabinetId(equSettingId);
			for (EquDetailSta equDetailSta : detDetailBStaList) {
				equDetailSta.setBoardNo(Integer.valueOf(DET_BOARD_NO_B));
				equDetailSta.setLockIndex((equDetailSta.getEquDetail().getRowNo() - 1) * 12 + equDetailSta.getColNo());
				this.equDetailStaService.update(equDetailSta);
			}

			// 栈号
			if (map.get(SetupKey.CCabinet.DET_BOARD_NO_B) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.CCabinet.DET_BOARD_NO_B);
				cabinetSetup.setSetupValue(DET_BOARD_NO_B);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.CCabinet.DET_BOARD_NO_B, DET_BOARD_NO_B));
			}
			break;
		case "TEM_CABINET":
			// 暂存柜参数
			// COM口
			if (map.get(SetupKey.TemCabinet.TEM_COM) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.TemCabinet.TEM_COM);
				cabinetSetup.setSetupValue(TEM_COM);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.TemCabinet.TEM_COM, TEM_COM));
			}

			// 波特率
			if (map.get(SetupKey.TemCabinet.TEM_BAUD) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.TemCabinet.TEM_BAUD);
				cabinetSetup.setSetupValue(TEM_BAUD);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService.save(new CabinetSetup(equSettingId, SetupKey.TemCabinet.TEM_BAUD, TEM_BAUD));
			}

			// 删除暂存柜锁控板参数
			List<LockParam> temCabinetSetupList = this.lockParamService.getLockParamByEquSettingId(equSettingId,
					CabinetType.TEM_CABINET);
			if (temCabinetSetupList != null && temCabinetSetupList.size() > 0) {
				for (LockParam temCabinetSetup : temCabinetSetupList) {
					this.lockParamService.delete(temCabinetSetup);
				}
			}

			// 删除暂存柜格子
			List<SubBox> subBoxList = this.subBoxService.getSubBoxList(equSettingId);
			if (subBoxList != null && subBoxList.size() > 0) {
				for (SubBox subBox : subBoxList) {
					this.subBoxService.delete(subBox);
				}
			}

			// 新增暂存柜锁控板参数
			Integer temBoxIndex = 1;// 索引号
			if (temSetupMap != null && temSetupMap.size() > 0) {
				for (Entry<Integer, String[]> entry : temSetupMap.entrySet()) {
					String[] setupArray = entry.getValue();
					Integer boardNo = Integer.valueOf(setupArray[0]);
					Integer rowNo = Integer.valueOf(setupArray[1]);
					Integer colNo = Integer.valueOf(setupArray[2]);
					this.lockParamService
							.save(new LockParam(equSettingId, CabinetType.TEM_CABINET, boardNo, rowNo, colNo));

					// 新增暂存柜格子
					Integer lockIndex = 1;// 针脚号
					for (int i = 1; i <= rowNo; i++) {
						for (int j = 1; j <= colNo; j++) {
							this.subBoxService.save(new SubBox(equSettingId, boardNo, lockIndex, temBoxIndex, i, j));
							temBoxIndex++;
							lockIndex++;
						}
					}
				}
			}
			break;
		case "STORE_CABINET":
			// 储物柜参数
			// COM口
			if (map.get(SetupKey.StoreCabinet.STORE_COM) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.StoreCabinet.STORE_COM);
				cabinetSetup.setSetupValue(STORE_COM);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.StoreCabinet.STORE_COM, STORE_COM));
			}

			// 波特率
			if (map.get(SetupKey.StoreCabinet.STORE_BAUD) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.StoreCabinet.STORE_BAUD);
				cabinetSetup.setSetupValue(STORE_BAUD);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.StoreCabinet.STORE_BAUD, STORE_BAUD));
			}

			// 删除储物柜锁控板参数
			List<LockParam> stoCabinetSetupList = this.lockParamService.getLockParamByEquSettingId(equSettingId,
					CabinetType.STORE_CABINET);
			if (stoCabinetSetupList != null && stoCabinetSetupList.size() > 0) {
				for (LockParam stoCabinetSetup : stoCabinetSetupList) {
					this.lockParamService.delete(stoCabinetSetup);
				}
			}

			// 删除储物柜格子
			List<EquDetailSta> stoDetailStaList = this.equDetailStaService.getEquDetailStaListByCabinetId(equSettingId);
			if (stoDetailStaList != null && stoDetailStaList.size() > 0) {
				for (EquDetailSta equDetailSta : stoDetailStaList) {
					this.equDetailStaService.delete(equDetailSta);
				}
			}

			List<EquDetail> stoDetailList = this.equDetailService.getEquDetailListBySettingId(equSettingId);
			if (stoDetailList != null && stoDetailList.size() > 0) {
				for (EquDetail equDetail : stoDetailList) {
					this.equDetailService.delete(equDetail);
				}
			}

			// 新增储物柜锁控板参数
			Integer boxIndex = 1;// 索引号
			Integer lockIndex = 1;// 针脚号
			// 新增储物柜格子
			EquDetail equDetail;
			int row; // 行标
			Integer col; // 列标
			String[] setupArray;
			Integer boardNo;
			Integer rowNo = 0;
			Integer colNo = 0;
			Boolean isNewLine = false; // 是否新起一行
			Boolean isContinue = false; // 是否追加
			if (stoSetupMap != null && stoSetupMap.size() > 0) {
				for (Entry<Integer, String[]> entry : stoSetupMap.entrySet()) {
					setupArray = entry.getValue();
					boardNo = Integer.valueOf(setupArray[0]);
					row = Integer.valueOf(setupArray[1]);
					col = Integer.valueOf(setupArray[2]);
					this.lockParamService
							.save(new LockParam(equSettingId, CabinetType.STORE_CABINET, boardNo, row, col));
					for (int i = 1; i <= row; i++) {
						if (row == 1) { // 中断或追加
							if (isContinue) {
								isNewLine = false;
							} else {
								isNewLine = true;
								isContinue = true;
							}
						} else {
							isNewLine = true;
							isContinue = false;
						}
						if (isNewLine) {
							rowNo++;
							colNo = 1;
						}
						equDetail = this.equDetailService.add(new EquDetail(equSettingId, rowNo));
						for (int j = 1; j <= col; j++) {
							if (lockIndex % 51 == 0) {
								lockIndex = 1;
							}
							this.equDetailStaService.save(new EquDetailSta(equDetail.getId(), rowNo, colNo, 16, 3,
									new Date(), boardNo, lockIndex, boxIndex));
							boxIndex++;
							lockIndex++;
							colNo++;
						}
					}
				}
			}
			break;
		case "RECOVERY_CABINET":
			// 回收柜参数
			// 扫描仪COM口
			if (map.get(SetupKey.RecCabinet.REC_SCAN_COM) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.RecCabinet.REC_SCAN_COM);
				cabinetSetup.setSetupValue(REC_SCAN_COM);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.RecCabinet.REC_SCAN_COM, REC_SCAN_COM));
			}

			// 扫描仪类型
			if (map.get(SetupKey.RecCabinet.REC_SCAN_TYPE) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.RecCabinet.REC_SCAN_TYPE);
				cabinetSetup.setSetupValue(REC_SCAN_TYPE);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.RecCabinet.REC_SCAN_TYPE, REC_SCAN_TYPE));
			}

			// 描仪波特率
			if (map.get(SetupKey.RecCabinet.REC_SCAN_BAUD) != null) {
				CabinetSetup cabinetSetup = map.get(SetupKey.RecCabinet.REC_SCAN_BAUD);
				cabinetSetup.setSetupValue(REC_SCAN_BAUD);
				this.cabinetSetupService.update(cabinetSetup);
			} else {
				this.cabinetSetupService
						.save(new CabinetSetup(equSettingId, SetupKey.RecCabinet.REC_SCAN_BAUD, REC_SCAN_BAUD));
			}
			break;
		default:
			break;
		}
		return tip;
	}

	/**
	 * @description 编辑可控抽屉柜参数
	 * @param equSettingId 柜体Id
	 * @param cabinetType  柜体类型
	 * @param trolCom      可控柜串口
	 * @param trolBaud     可控柜波特率
	 * @param data         编辑参数
	 * @return
	 */
	@RequestMapping("/editTroSetup")
	@ResponseBody
	public OpTip editTroSetup(@RequestParam("equSettingId") String equSettingId,
			@RequestParam("cabinetType") String cabinetType, @RequestParam("trolCom") String trolCom,
			@RequestParam("trolBaud") String trolBaud, @RequestParam("data") String data) {
		return this.trolDrawerService.editTroSetup(equSettingId, cabinetType, trolCom, trolBaud, data);
	}

	/**
	 * 跳转到-设置页面-主柜/子柜
	 */
	@RequestMapping("/setupInfoView/{equSettingId}/{cabinetType}")
	public String setupInfoView(@PathVariable String equSettingId, @PathVariable String cabinetType, Model model) {
		model.addAttribute("equSettingId", equSettingId);
		model.addAttribute("cabinetType", cabinetType);

		if (cabinetType.equals(CabinetType.TEM_CABINET.toString())) {
			return PREFIX + "temSetupInfo.html";
		}
		return PREFIX + "setupInfo.html";
	}

	/**
	 * @description 获取板子信息
	 */
	@RequestMapping("/card/{equSettingId}")
	public String getCard(@PathVariable String equSettingId, Model model) {
		List<CabinetPlcSetting> settingList = this.cabinetPlcSettingService
				.getCabinetPlcSettingListByCabinetId(equSettingId);
		EquSetting equSetting = this.equSettingService.get(equSettingId);
		model.addAttribute("equSetting", equSetting);
		model.addAttribute("cabinetId", equSettingId);
		model.addAttribute("settingList", settingList);
		return PREFIX + "card.html";
	}

	/**
	 * @description 获取通道信息
	 */
	@RequestMapping("/channel/{equDetailId}")
	public String getChannel(@PathVariable String equDetailId, Model model) {
		EquDetail detail = this.smartCabinetService.getEquDetail(equDetailId);
		model.addAttribute("detail", detail);
		model.addAttribute("cabinetType", detail.getEquSetting().getCabinetType().name());
		return PREFIX + "channel.html";
	}

	/**
	 * @description 修改刀具柜IP,端口
	 */
	@RequestMapping("/editCabinet/{equSettingId}")
	@ResponseBody
	public Tip editCabinet(@Valid EquSetting equSetting, @PathVariable String equSettingId) {
		this.equSettingService.editCabinet(equSettingId);
		return SUCCESS_TIP;
	}

	/**
	 * @description 修改设备详情
	 */
	@RequestMapping("/editEquDetail")
	@ResponseBody
	public Tip editEquDetail(@Valid EquDetail detail) {
		this.equDetailService.update(detail);
		return SUCCESS_TIP;
	}

	/**
	 * @description 更新管道状态
	 */
	@RequestMapping("/updateChannelStatus/{equDetailStaId}")
	@ResponseBody
	public Tip updateChannelStatus(@PathVariable String equDetailStaId) {
		EquDetailSta sta = this.equDetailStaService.get(equDetailStaId);
		if (sta.getStatus().equals(UsingStatus.ENABLE)) {
			sta.setStatus(UsingStatus.DISABLE);
		} else {
			sta.setStatus(UsingStatus.ENABLE);
		}
		this.equDetailStaService.update(sta);
		return SUCCESS_TIP;
	}

	/**
	 * @description 获取设备详情状态列表
	 */
	@RequestMapping("/getStaList/{equDetailId}")
	public String getStaList(@PathVariable String equDetailId, Model model) {
		List<EquDetailSta> staList = this.smartCabinetService.getStaList(equDetailId);
		model.addAttribute("staList", staList);
		return PREFIX + "stalist.html";
	}

	/**
	 * @description 设置刀具柜参数配置
	 */
	@RequestMapping("/saveCabinetPlcSetting")
	@ResponseBody
	public Tip saveCabinetPlcSetting(@Valid CabinetPlcSetting setting) {
		if (setting.getId() == null || "".equals(setting.getId())) {
			this.cabinetPlcSettingService.save(setting);
		} else {
			CabinetPlcSetting tmp = this.cabinetPlcSettingService.get(setting.getId());
			try {
				BeanUtils.copyProperties(tmp, setting);
				this.cabinetPlcSettingService.update(tmp);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
				return new ErrorTip(405, "设置失败");
			}
		}
		return SUCCESS_TIP;
	}

	/**
	 * @Desc 创建已设置设备tree[储存设备]
	 */
	@RequestMapping("/initStoreEquTree/{equSettingId}")
	@ResponseBody
	public List<ZTreeNode> initEquSettingTree(@PathVariable String equSettingId) {
		List<ZTreeNode> tree = this.equSettingService.initEquSettingTree(equSettingId);
		return tree;
	}

	/**
	 * 创建已设置设备tree[储存设备]
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<ZTreeNode> treeEqu() {
		List<ZTreeNode> tree = this.equSettingService.treeEqu();
		return tree;
	}

	/**
	 * @description 创建设备+层级
	 * @return
	 */
	@RequestMapping("/treeCabDetail")
	@ResponseBody
	public List<ZTreeNode> treeCabDetail() {
		return this.equSettingService.treeCabDetail();
	}

	/**
	 * 跳转到业务主页面
	 */
	@RequestMapping("/getEquInfosView")
	public String getEquInfosView(Model model) {
		return PREFIX + "equinfos.html";
	}

	/**
	 * /** 根据设备设置Id获取设置信息(设备详情)
	 * 
	 * @param equSettingId 设备设置Id
	 */
	@RequestMapping("/getEquInfos")
	@ResponseBody
	public Object getEquInfos(Model model) {
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

		String matInfo = super.getPara("matInfo");
		String equSettingId = super.getPara("equSettingId");

		EquSetting equSetting = this.equSettingService.getEquInfos(equSettingId);
		Map<String, Integer> rowMap = new HashMap<>();
		List<FeedingDetail> detailList = new ArrayList<>();
		List<FeedingList> feedingList = this.feedingListService.getWaitFeedingNum(equSettingId);
		for (FeedingList feeding : feedingList) {
			detailList = this.feedingDetailService.getFeedingDetailListByFeedingListId(feeding.getId());
			for (FeedingDetail detail : detailList) {
				String matInfoId = detail.getMatInfoId();
				String equDetailStaId = detail.getEquDetailStaId();
				Integer waitFeedingNum = rowMap.get(equDetailStaId + "_" + matInfoId);
				Integer feedingNum = detail.getFeedingNum();
				Integer finalNum = 0;
				if (waitFeedingNum == null) {
					finalNum = feedingNum;
				} else {
					finalNum = waitFeedingNum + feedingNum;
				}
				rowMap.put(equDetailStaId + "_" + matInfoId, finalNum);
			}
		}

		List<EquDetailStaVo> list = new ArrayList<>();
		Map<Integer, Map<Integer, EquDetailSta>> map = equSetting.getEquInfoMap();
		for (Entry<Integer, Map<Integer, EquDetailSta>> detail : map.entrySet()) {
			for (Map.Entry<Integer, EquDetailSta> sta : map.get(detail.getKey()).entrySet()) {

				// 通过物料名称、编号、规格做筛选
				MatEquInfo matEquInfo = sta.getValue().getMatInfo();
				if (matEquInfo != null && StringUtils.isNotBlank(matInfo)) {

					String matEquName = matEquInfo.getMatEquName();
					String barCode = matEquInfo.getBarCode();
					String spec = matEquInfo.getSpec();

					// 模糊匹配

					int nameIndex = -1;
					if (StringUtils.isNotBlank(matEquName)) {
						nameIndex = matEquName.indexOf(matInfo);
					}
					int barCodeIndex = -1;
					if (StringUtils.isNotBlank(barCode)) {
						barCodeIndex = barCode.indexOf(matInfo);
					}
					int specIndex = -1;
					if (StringUtils.isNotBlank(spec)) {
						specIndex = spec.indexOf(matInfo);
					}

					if (nameIndex == -1 && barCodeIndex == -1 && specIndex == -1) {
						continue;
					}
				}

				String matInfoId = sta.getValue().getMatInfo() != null ? sta.getValue().getMatInfo().getId() : "";
				String equDetailStaId = sta.getValue() != null ? sta.getValue().getId() : "";
				list.add(new EquDetailStaVo(sta.getValue() != null ? sta.getValue().getId() : "",
						sta.getValue().getMatInfo() != null ? sta.getValue().getMatInfo().getId() : "",
						sta.getValue().getMatInfo() != null ? sta.getValue().getMatInfo().getMatEquName() : "",
						sta.getValue().getMatInfo() != null ? sta.getValue().getMatInfo().getBarCode() : "",
						sta.getValue().getMatInfo() != null ? sta.getValue().getMatInfo().getSpec() : "",
						sta.getValue().getMaxReserve() != null ? sta.getValue().getMaxReserve() : 0,
						sta.getValue().getWarnVal() != null ? sta.getValue().getWarnVal() : 0,
						sta.getValue().getMatInfo() != null ? sta.getValue().getCurNum() : 0,
						"F" + sta.getValue().getEquDetail().getRowNo() + "-" + sta.getValue().getColNo(),
						rowMap.get(equDetailStaId + "_" + matInfoId) != null
								? rowMap.get(equDetailStaId + "_" + matInfoId)
								: 0,
						sta.getValue().getMatInfo() != null
								? '/' + ToolProperties.PREFIX + '/' + sta.getValue().getMatInfo().getIcon()
								: ""));
			}
		}
		pageSize = list != null ? list.size() : pageSize;
		Pageable pageable = new PageRequest(pageNo, pageSize);
		Page<EquDetailStaVo> page = ListUtil.listConvertToPage(list, pageable);
		return page;
	}
}