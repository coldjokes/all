package com.dosth.toolcabinet.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnbaosi.dto.tool.ApplyVoucher;
import com.cnbaosi.dto.tool.FeignCodeName;
import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.motorboard.ModbusUtil;
import com.dosth.comm.node.TypeNode;
import com.dosth.comm.node.ZTreeNode;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.StockTip;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.MyPage;
import com.dosth.toolcabinet.commu.ConcreteMediator;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.dto.AccountUserInfo;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.ExtractionMethod;
import com.dosth.toolcabinet.dto.FeedDetailSta;
import com.dosth.toolcabinet.dto.MatDetail;
import com.dosth.toolcabinet.dto.NoticeMgrInfo;
import com.dosth.toolcabinet.dto.SubMatDetail;
import com.dosth.toolcabinet.service.AuthorService;
import com.dosth.toolcabinet.service.CabinetCheckService;
import com.dosth.toolcabinet.service.CnbaosiFeignService;
import com.dosth.toolcabinet.service.DoorService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.util.BaseController;
import com.dosth.toolcabinet.util.MiscUtil;
import com.dosth.util.DateUtil;
import com.dosth.util.OpTip;
import com.dosth.util.SystemUtil;

/**
 * @description 主页Controller
 * 
 * @author guozhidong
 *
 */
@Controller
public class IndexController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ToolService toolService;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private ConcreteMediator mediator;
	@Autowired
	private DoorService doorService;
	@Autowired
	private CabinetCheckService cabinetCheckService;
	@Autowired
	private CnbaosiFeignService cnbaosiFeignService;

	/**
	 * @description 请求重新向
	 * @return
	 */
	@RequestMapping("")
	public String redirect(Model model, HttpServletRequest request) {
		return "redirect:/login";
	}

	/**
	 * @description 登录
	 * @param model
	 * @return
	 */
	@RequestMapping("/login")
	public String login(Model model, HttpServletRequest request) {
		model.addAttribute("faceLogin", Boolean.valueOf(DosthToolcabinetRunnerInit
				.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.FACE_LOGIN)));
		model.addAttribute("mainCabinetId", DosthToolcabinetRunnerInit.mainCabinetId);
		MP3Player.play("AUDIO_A1.mp3");

		// 获取文件最后更新时间
		String lastUpdateTime = MiscUtil.getFileLastUpdate("D:/resource/Toolcabinet.jar");
		model.addAttribute("serialNo", cabinetConfig.getSerialNo());
		model.addAttribute("version", cabinetConfig.getVersion());
		model.addAttribute("projectLastUpdateTime", lastUpdateTime);
		model.addAttribute("logo", this.cabinetConfig.getLogo());
		List<StockTip> tipList = this.toolService.getStockTip(DosthToolcabinetRunnerInit
				.getCabinetParam(this.cabinetConfig.getSerialNo(), SetupKey.Cabinet.MAIN_CABINET_ID));
		model.addAttribute("stockTipList", tipList);
		return "login";
	}

	/**
	 * @description 根据卡片内容获取帐户用户信息
	 * @param cardStr 卡片内容
	 * @return
	 */
	@RequestMapping("/getAccountInfo/{cardStr}")
	@ResponseBody
	public AccountUserInfo getAccountInfo(@PathVariable String cardStr, HttpServletRequest request) {
		AccountUserInfo accountUserInfo = null;
		try {
			accountUserInfo = this.authorService.getAccountUserInfo(cardStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return accountUserInfo;
	}

	/**
	 * @description 人脸识别登录
	 * @param file 人像
	 * @return
	 */
	@RequestMapping(value = "/faceLogin", method = RequestMethod.POST)
	@ResponseBody
	public AccountUserInfo faceLogin(@RequestParam("file") String file) {
		AccountUserInfo accountUserInfo = this.authorService.faceLogin(file);
		return accountUserInfo;
	}

	/**
	 * @description 人脸识别 绑定
	 * @param request
	 * @return
	 */
	@RequestMapping("/faceBind")
	public String faceBind(Model model) {
		model.addAttribute("faceLogin", Boolean.valueOf(DosthToolcabinetRunnerInit
				.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.FACE_LOGIN)));
		return "facebind";
	}

	/**
	 * @description 校验用户
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkUser")
	@ResponseBody
	public OpTip checkUser(HttpServletRequest request) {
		OpTip opTip = new OpTip(200, "登录成功");
		try {
			AccountUserInfo accountUserInfo = this.authorService.checkUser(request.getParameter("loginName"),
					request.getParameter("pwd"));
			HttpSession session = request.getSession();
			session.setAttribute("accountInfo", accountUserInfo);
		} catch (Exception e) {
			opTip = new OpTip(201, e.getMessage());
		}
		if (opTip.getCode() == 200) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 执行马达板故障恢复
					resetMotorBoardProblem();
				}
			}).start();
			// 设置马达等待故障
			this.resetWaitForMotor();
			MP3Player.play("AUDIO_A2.mp3");
		} else {
			MP3Player.play("AUDIO_A3.mp3");
		}
		return opTip;
	}

	/**
	 * @description 按格子取料初始化
	 */
	@RequestMapping("/index")
	public String index(Model model, HttpServletRequest request) {
		try {
			AccountUserInfo accountInfo = super.getAccountInfo(request);
			String accountId = accountInfo.getAccountId();
			model.addAttribute("borrowTypeList",
					this.toolService.getBorrowTypeList(DosthToolcabinetRunnerInit.mainCabinetId, accountId));
			List<CartInfo> cartInfoList = this.toolService.selectCart(accountId,
					DosthToolcabinetRunnerInit.mainCabinetId);
			int nums = 0;
			for (CartInfo info : cartInfoList) {
				nums += info.getNum();
			}
			model.addAttribute("nums", nums);
			model.addAttribute("accountInfo", accountInfo);
			model.addAttribute("mainCabinetId", DosthToolcabinetRunnerInit.mainCabinetId);
			model.addAttribute("logo", this.cabinetConfig.getLogo());
			model.addAttribute("agenMap", this.cabinetConfig.getAgenMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	/**
	 * @description 按类型取料初始化
	 */
	@RequestMapping("/otherborrow/{type}")
	public String init(@PathVariable String type, Model model, HttpServletRequest request) {
		model.addAttribute("type", type);
		try {
			String accountId = super.getAccountInfo(request).getAccountId();
			List<TypeNode> list = this.toolService.getCategoryTree(DosthToolcabinetRunnerInit.mainCabinetId, accountId,
					type);
			model.addAttribute("list", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "otherborrow";
	}

	/**
	 * @description 按格子取料初始化
	 */
	@RequestMapping("/initGrid")
	public String grid(Model model) {
		List<ExtraCabinet> cabinetList = this.toolService.getCabinetList(this.cabinetConfig.getSerialNo());
		cabinetList = cabinetList.stream()
				.filter(cabinet -> !cabinet.getCabinetType().equals(CabinetType.TEM_CABINET.name())
						&& !cabinet.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name())
						&& !cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_C.name()))
				.collect(Collectors.toList());
		cabinetList.sort((o1, o2) -> o1.getCabinetType().compareTo(o2.getCabinetType()));
		model.addAttribute("cabinetList", cabinetList);
		return "grid";
	}

	/**
	 * @description 获取板子集合
	 * @param model
	 * @param cabinetId   柜子Id
	 * @param cabinetType 柜体类型
	 */
	@RequestMapping("/grid")
	public String initGrid(Model model, @RequestParam("cabinetId") String cabinetId,
			@RequestParam("cabinetType") String cabinetType) {
		if (cabinetId == null || "".equals(cabinetId) || "undefined".equals(cabinetId)) {
			cabinetId = DosthToolcabinetRunnerInit.mainCabinetId;
		}
		List<Card> cardList = this.toolService.getCardList(cabinetId);
		model.addAttribute("cabinetId", cabinetId);
		model.addAttribute("cardList", cardList);
		model.addAttribute("cabinetType", cabinetType);
		model.addAttribute("logo", this.cabinetConfig.getLogo());
		return "gridlist";
	}

	/**
	 * @description 根据种别获取物料关联
	 * @param equInfoId 种别Id
	 */
	@RequestMapping("/getMatListByEqu/{equId}")
	public String getMatListByEqu(HttpServletRequest request, @PathVariable String equId, Model model) {
		String accountId = super.getAccountInfo(request).getAccountId();
		List<MatDetail> matlist = this.toolService.getMatListByEqu(DosthToolcabinetRunnerInit.mainCabinetId, accountId,
				equId);
		// 过滤获取库存大于0的物料列表
		matlist = matlist.stream().filter(list -> list.getRemainNum() > 0).collect(Collectors.toList());
		model.addAttribute("matlist", matlist);
		return "matlist";
	}

	/**
	 * @description 待归还、我的暂存柜主页
	 * @return
	 */
	@RequestMapping("/borrandstor")
	public String borrandstor(Model model) {
		List<ZTreeNode> list = new ArrayList<>();
		list.add(new ZTreeNode("history", "", "待归还物料"));

		// 过滤暂存柜
		List<ExtraCabinet> cabinetTreeList = this.toolService
				.getCabinetTreeList(DosthToolcabinetRunnerInit.mainCabinetId);
		List<ExtraCabinet> temCabinet = cabinetTreeList.stream()
				.filter(cabinetInfo -> cabinetInfo.getCabinetType().equals(CabinetType.TEM_CABINET.name()))
				.collect(Collectors.toList());

		if (temCabinet != null && temCabinet.size() > 0) {
			list.add(new ZTreeNode("subcabinet", "", "我的暂存柜"));
		}
		model.addAttribute("hasTempCabinet", temCabinet != null && temCabinet.size() > 0);

		List<ExtraCabinet> recCabinet = cabinetTreeList.stream()
				.filter(cabinetInfo -> cabinetInfo.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name()))
				.collect(Collectors.toList());
		if (recCabinet != null && recCabinet.size() > 0) {
			for (ExtraCabinet rec : recCabinet) {
				String recCom = DosthToolcabinetRunnerInit.getCabinetParam(rec.getCabinetId(),
						SetupKey.RecCabinet.REC_SCAN_COM);
				if (recCom != null && !"".equals(recCom)) {
					model.addAttribute("recCom", recCom);
				}
			}
		}

		model.addAttribute("list", list);
		model.addAttribute("selectedType", "history");
		return "borrandstor";
	}

	/**
	 * @description 初始化归还
	 * @param model
	 * @param matUseBillId 物料领取历史Id
	 * @param packNum      物料包装内总数
	 * @param 物料Id
	 * @return
	 */
	@RequestMapping("/initReturnBack")
	public String initReturnBack(Model model, @RequestParam("matUseBillId") String matUseBillId,
			@RequestParam("packNum") String packNum, @RequestParam("matInfoId") String matInfoId,
			@RequestParam("useLife") Integer useLife, @RequestParam("backWay") String backWay) {
		model.addAttribute("matUseBillId", matUseBillId);
		model.addAttribute("packNum", packNum);
		model.addAttribute("matInfoId", matInfoId);
		model.addAttribute("useLife", useLife);
		model.addAttribute("reBackTypeMap", this.toolService.getReBackTypeMap());
		String printComm = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
				SetupKey.Public.PRINT_COM);
		model.addAttribute("printComm", printComm == null ? "" : printComm);
		// 判定回收口有没有扫描仪
		List<ExtraCabinet> cabinetTreeList = this.toolService
				.getCabinetTreeList(DosthToolcabinetRunnerInit.mainCabinetId);
		cabinetTreeList = cabinetTreeList.stream()
				.filter(cabinet -> cabinet.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name()))
				.collect(Collectors.toList());
		String recovery = "N";
		String recovertComm = null;
		for (ExtraCabinet cabinet : cabinetTreeList) {
			recovertComm = DosthToolcabinetRunnerInit.getCabinetParam(cabinet.getCabinetId(),
					SetupKey.RecCabinet.REC_SCAN_COM);
			if (recovertComm != null && !"".equals(recovertComm)) {
				recovery = "Y";
				break;
			}
		}
		model.addAttribute("recovery", recovery);
		model.addAttribute("backWay", backWay);
		return "returnback";
	}

	/**
	 * @description 获取我的暂存柜信息
	 */
	@RequestMapping("/getSubCabinetList")
	public String getSubCabinetList(HttpServletRequest request, Model model) {
		String accountId = super.getAccountInfo(request).getAccountId();
		// 暂存柜共享开关
		String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
				SetupKey.TemCabinet.TEM_SHARE_SWITCH);
		List<SubMatDetail> detailList = this.toolService.getSubCabinetRpcList(shareSwitch, accountId);
		model.addAttribute("detailList", detailList);
		return "subcabinetlist";
	}

	/**
	 * @description 获取归还分类
	 */
	@RequestMapping("/getBackTypeInfo")
	public String getBackTypeInfo(Model model, @RequestParam("returnBackType") String returnBackType) {
		if (StringUtils.isNotBlank(returnBackType)) {
			model.addAttribute("backType", returnBackType);
		} else {
			model.addAttribute("backType", "backNormal");
		}
		return "borrlist";
	}

	/**
	 * @description 获取常规归还列表
	 */
	@RequestMapping("/getBackNormalList")
	public String getUnReturnList(@RequestParam("pageNo") int pageNo, HttpServletRequest request, Model model) {
		// 获取待归还数据
		String accountId = super.getAccountInfo(request).getAccountId();
		String serialNo = this.cabinetConfig.getSerialNo();
		MyPage<SubMatDetail> page = this.toolService.getBackNormalList(accountId, serialNo, pageNo);

		// 组装时间轴
		List<SubMatDetail> unreturnlist = page.getContent();
		Map<String, List<SubMatDetail>> map = new LinkedHashMap<>();
		List<SubMatDetail> detailList;
		for (SubMatDetail detail : unreturnlist) {
			detailList = map.get(DateUtil.getDay(detail.getBorrowTime()));
			if (detailList == null) {
				detailList = new ArrayList<>();
			}
			detailList.add(detail);
			map.put(DateUtil.getDay(detail.getBorrowTime()), detailList);
		}
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("map", map);
		return "backnormal";
	}

	/**
	 * @description 获取分类归还类型
	 */
	@RequestMapping("/getBackSortList")
	public String getBackSortList(HttpServletRequest request, Model model) {
		try {
			String cabinetId = DosthToolcabinetRunnerInit.mainCabinetId;
			String accountId = super.getAccountInfo(request).getAccountId();
			List<FeignCodeName> typeList = this.toolService.getUnReturnTypeList(accountId, cabinetId);
			model.addAttribute("list", typeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backsort";
	}

	/**
	 * @description 获取分类归还列表
	 */
	@RequestMapping("/getMatListByType/{matType}")
	public String getMatListByType(HttpServletRequest request, @PathVariable String matType, Model model) {
		try {
			String serialNo = this.cabinetConfig.getSerialNo();
			String accountId = super.getAccountInfo(request).getAccountId();
			Map<String, List<SubMatDetail>> mapList = this.toolService.getUnReturnList(accountId, serialNo);
			List<SubMatDetail> list = mapList.get(matType);
			model.addAttribute("matType", matType);
			model.addAttribute("matList", list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "backsortinfo";
	}

	/**
	 * @description 获取批量归还列表
	 */
	@RequestMapping("/getBackBatchList")
	public String getBackBatchList(HttpServletRequest request, Model model) {
		String accountId = super.getAccountInfo(request).getAccountId();
		String serialNo = this.cabinetConfig.getSerialNo();
		List<SubMatDetail> backBatchList = this.toolService.getBackBatchList(accountId, serialNo);
		model.addAttribute("backBatchList", backBatchList);
		return "backbatch";
	}

	/**
	 * @description 跳转到管理员页面
	 * @return
	 */
	@RequestMapping("/admin")
	public String admin(Model model) {
		// 过滤虚拟仓和回收柜
		List<ExtraCabinet> cabinetTreeList = this.toolService
				.getCabinetTreeList(DosthToolcabinetRunnerInit.mainCabinetId);
		cabinetTreeList = cabinetTreeList.stream()
				.filter(cabinetInfo -> !cabinetInfo.getCabinetType().equals(CabinetType.VIRTUAL_WAREHOUSE.name())
						&& !cabinetInfo.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name()))
				.collect(Collectors.toList());
		cabinetTreeList.sort((c1, c2) -> c1.getCabinetType().compareTo(c2.getCabinetType()));

		model.addAttribute("cabinetTreeList", cabinetTreeList);
		model.addAttribute("faceBind", DosthToolcabinetRunnerInit
				.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.FACE_LOGIN));
		return "admin";
	}

	/**
	 * @description 管理员界面跳转
	 */
	@RequestMapping("/adminTo")
	public String adminTo(@RequestParam("cabinetId") String cabinetId, Model model) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID);
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		model.addAttribute("cabinetId", cabinetId);
		// PLC或DET标识,用于门故障恢复
		model.addAttribute("plcOrDet", cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name()) ? "PLC" : "DET");
		String page;
		switch (CabinetType.valueOf(cabinetType)) {
		case KNIFE_CABINET_PLC: // PLC
		case KNIFE_CABINET_DETA: // 行列式A型柜
		case KNIFE_CABINET_DETB: // 行列式B型柜
		case KNIFE_CABINET_C: // C型柜
		case SUB_CABINET: // 副柜
			if (mainCabinetId != null && mainCabinetId.equals(cabinetId)) { // 主柜
				// 打印数量设定
				String printComm = DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Public.PRINT_COM);
				if (printComm != null && !"".equals(printComm)) {
					model.addAttribute("printComm", printComm);
				}
				// 判定回收口有没有扫描仪
				List<ExtraCabinet> cabinetTreeList = this.toolService
						.getCabinetTreeList(DosthToolcabinetRunnerInit.mainCabinetId);
				cabinetTreeList = cabinetTreeList.stream()
						.filter(cabinet -> cabinet.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name()))
						.collect(Collectors.toList());
				String recovery = "N";
				String recovertComm = null;
				if (cabinetTreeList != null && cabinetTreeList.size() > 0) {
					for (ExtraCabinet cabinet : cabinetTreeList) {
						recovertComm = DosthToolcabinetRunnerInit.getCabinetParam(cabinet.getCabinetId(),
								SetupKey.RecCabinet.REC_SCAN_COM);
						if (recovertComm != null && !"".equals(recovertComm)) {
							recovery = "Y";
							break;
						}
					}
				}
				model.addAttribute("recovery", recovery);
				page = "adminmain";
			} else { // 副柜
				page = "adminsub";
			}
			break;
		case KNIFE_CABINET_C_A:
		case KNIFE_CABINET_C_B:
			page = "admincpart";
			break;
		case TEM_CABINET: // 暂存柜
			model.addAttribute("subBoxMap", this.toolService.getSubBoxMapGroupByBoardNo(cabinetId));
			page = "admintem";
			break;
		case STORE_CABINET: // 储物柜
			model.addAttribute("cardList", this.toolService.getCardList(cabinetId));
			page = "adminstore";
			break;
		case TROL_DRAWER: // 可控柜
			model.addAttribute("cardList", this.toolService.getCardList(cabinetId));
			page = "admintroldrawer";
			break;
		default:
			page = "404";
			break;
		}
		return page;
	}

	/**
	 * @description IC 绑定
	 * @return
	 */
	@RequestMapping("/icBind")
	public String icBind(Model model) {
		return "icbind";
	}

	/**
	 * @description 补料
	 */
	@RequestMapping("/supplymat")
	public String supplymat(HttpServletRequest request, Model model) throws Exception {
		String accountId = super.getAccountInfo(request).getAccountId();
		List<ExtractionMethod> feedingList = this.toolService.getFeedingList(DosthToolcabinetRunnerInit.mainCabinetId,
				accountId);
		if (feedingList == null || feedingList.size() == 0) {
			throw new Exception("没有可操作的补料清单！");
		} else {
			model.addAttribute("feedingList", feedingList);
			return "supplymat";
		}
	}

	/**
	 * @description 根据补料清单Id获取清单明细列表
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/selectFeed")
	public String selectFeed(@RequestParam("feedingListId") String feedingListId, Model model) {
		List<Card> cards = this.toolService.getFeedingDetailListByListId(feedingListId);
		cards.sort((c1, c2) -> c1.getRowNo() - c2.getRowNo());
		model.addAttribute("cards", cards);
		return "feedingdetail";
	}

	/**
	 * @description 暂存柜
	 * @param cabinetId 暂存柜Id
	 * @return
	 */
	@RequestMapping("/tmpCabinet")
	public String tmpCabinet(@RequestParam("cabinetId") String cabinetId, Model model) {
		model.addAttribute("subBoxMap", this.toolService.getSubBoxMapGroupByBoardNo(cabinetId));
		return "tmpcabinet";
	}

	/**
	 * @description 我要暂存
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("/tosubcabinet")
	public String tosubcabinet(Model model, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		List<SubMatDetail> unSubDetailList = this.toolService.getUnSubMatInfoList(accountId);
		model.addAttribute("unSubDetailList", unSubDetailList);
		return "tosubcabinet";
	}

	/**
	 * @description 我要取料
	 * @param subboxId  暂存柜盒子Id
	 * @param matInfoId 物料Id
	 * @param lockIndex 锁控板索引
	 * @param model
	 * @return
	 */
	@RequestMapping("/outsubcabinet")
	public String outsubcabinet(@RequestParam("subboxId") String subboxId, @RequestParam("matInfoId") String matInfoId,
			@RequestParam("lockIndex") String lockIndex, Model model, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		SubMatDetail detail = this.toolService.outsubcabinet(subboxId, matInfoId, accountId);
		model.addAttribute("detail", detail);
		model.addAttribute("matId", detail.getMatId());
		model.addAttribute("subBoxId", detail.getSubBoxId());
		model.addAttribute("subCabinetId", detail.getSubCabinetId());
		model.addAttribute("boxIndex", detail.getLockPara().getBoxIndex());
		model.addAttribute("boardNo", detail.getLockPara().getBoardNo());
		model.addAttribute("lockIndex", detail.getLockPara().getLockIndex());
		return "outsubcabinet";
	}

	@RequestMapping("/resetSystem")
	@ResponseBody
	public void resetSystem() {
		Integer sec = 5;
		SystemUtil.restart(sec);
	}

	@RequestMapping("/resetService")
	@ResponseBody
	public void resetService() throws Exception {

		// 关闭服务
		try {
			URL url = new URL("http://localhost:8888/shutdown");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.connect();
			InputStream is = conn.getInputStream();
			// 构造一个字符流缓存
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String str = "";
			while ((str = br.readLine()) != null) {
				str = new String(str.getBytes(), "UTF-8");// 解决中文乱码问题
			}
			// 关闭流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 启动服务
//		try {
//			String startPath = ResourceUtils.getFile("classpath:start.bat").getPath();
//			SystemUtil.executeBat(startPath);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		String startPath = "D:\\resource\\AHNO_toolcabinet.bat";
		SystemUtil.executeBat(startPath);
	}

	/**
	 * @description 前台补料
	 */
	@RequestMapping("/initFeed")
	public String initFeeding(Model model) {
		List<ExtraCabinet> cabinetList = this.toolService.getCabinetList(this.cabinetConfig.getSerialNo());
		cabinetList = cabinetList.stream()
				.filter(cabinet -> !cabinet.getCabinetType().equals(CabinetType.TEM_CABINET.name())
						&& !cabinet.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name())
						&& !cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_C.name()))
				.collect(Collectors.toList());
		if (cabinetList != null && cabinetList.size() > 0) {
			model.addAttribute("cabinetList", cabinetList);
		}
		return "sta";
	}

	/**
	 * @description 柜子详情(库存明细)
	 * @return
	 */
	@RequestMapping("/staList")
	public String initSta(Model model, HttpServletRequest request) {
		String query = request.getParameter("query");
		String search = request.getParameter("search");
		List<FeedDetailSta> staList = this.toolService.getFeedDetailStaList(DosthToolcabinetRunnerInit.mainCabinetId,
				query, search);
		model.addAttribute("staList", staList);
		return "stalist";
	}

	@RequestMapping("/checkDoorStatus")
	@ResponseBody
	public String checkDoorStatus() {
		try {
			this.cabinetCheckService.check(DosthToolcabinetRunnerInit.mainCabinetId, true, true);
		} catch (Exception e1) {
			return e1.getMessage();
		}
		try {
			this.doorService.checkDoorStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "检测门状态";
	}

	/**
	 * 恢复马达板故障
	 */
	private void resetMotorBoardProblem() {
		String cabinetType = "";
		if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) {
			List<Card> cardList = this.toolService.getCardList(DosthToolcabinetRunnerInit.mainCabinetId);
			for (Card card : cardList) {
				logger.info("恢复马达板" + card.getHost() + "故障");
				// 获取马达板Ip
				ModbusUtil.reset(card.getHost());
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置马达等待故障
	 */
	private void resetWaitForMotor() {
		this.mediator.setShoppCartTakenOut();
	}

	/**
	 * @description 跳转到重置页面-打印纸/回收仓
	 * @return
	 */
	@RequestMapping("/getNoticeMgr")
	public String getNoticeMgr(@RequestParam("noticeType") String noticeType, Model model) {
		List<NoticeMgrInfo> noticeInfoList = this.toolService.getNoticeMgr(DosthToolcabinetRunnerInit.mainCabinetId)
				.stream().filter(noticeInfo -> noticeInfo.getNoticeType().equals(noticeType))
				.collect(Collectors.toList());
		model.addAttribute("noticeType", noticeType);
		if (noticeInfoList != null && noticeInfoList.size() > 0) {
			model.addAttribute("noticeMgrInfo", noticeInfoList.get(0));
		}

		if (noticeType.equals("PRINT")) {
			return "resetPrint";
		} else {
			return "resetRecovery";
		}
	}

	/**
	 * @description 初始化申请单
	 */
	@RequestMapping("/initApplyVoucher")
	public String initApply(Model model) {
		model.addAttribute("cabinetId", DosthToolcabinetRunnerInit.mainCabinetId);
		return "applyvoucher";
	}

	/**
	 * @description 加载申请单详情
	 * @param request
	 * @param model
	 * @param search  查询条件
	 * @return
	 */
	@RequestMapping("/getApplyMatDetailList")
	public String getApplyMatDetailList(HttpServletRequest request, Model model,
			@RequestParam("search") String search) {
		String userName = super.getAccountInfo(request).getUserName();
		List<ApplyVoucher> voucherList = this.cnbaosiFeignService.getApplyVoucherList(userName, search);
		model.addAttribute("voucherList", voucherList);
		return "applymatdetaillist";
	}
}