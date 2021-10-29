package com.dosth.toolcabinet.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.common.CabinetConstant;
import com.cnbaosi.dto.ElecLock;
import com.cnbaosi.modbus.dto.MonitorData;
import com.cnbaosi.modbus.enums.AddrType;
import com.cnbaosi.modbus.monitor.ProblemMonitor;
import com.dosth.comm.UICompoment;
import com.dosth.comm.audio.MP3Player;
import com.dosth.comm.node.ZTreeNode;
import com.dosth.comm.plc.PLCUtil;
import com.dosth.comm.softhand.SoftHandComm;
import com.dosth.comm.softhand.SoftHandCommMsg;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.Lattice;
import com.dosth.dto.MatInfo;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.commu.ConcreteMediator;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.config.CabinetConfiguration;
import com.dosth.toolcabinet.dto.BorrowGrid;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.MatDetail;
import com.dosth.toolcabinet.dto.NoticeMgrInfo;
import com.dosth.toolcabinet.dto.ReBackType;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.dto.SubMatDetail;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.toolcabinet.dto.enums.TrueOrFalse;
import com.dosth.toolcabinet.enums.PlcOpType;
import com.dosth.toolcabinet.enums.PrintInfoType;
import com.dosth.toolcabinet.enums.TestWorkType;
import com.dosth.toolcabinet.service.AuthorService;
import com.dosth.toolcabinet.service.BorrowMatService;
import com.dosth.toolcabinet.service.CabinetCheckService;
import com.dosth.toolcabinet.service.CnbaosiFeignService;
import com.dosth.toolcabinet.service.DoorService;
import com.dosth.toolcabinet.service.HopperService;
import com.dosth.toolcabinet.service.ReturnBackDoorService;
import com.dosth.toolcabinet.service.ReturnBackService;
import com.dosth.toolcabinet.service.StorageMediumService;
import com.dosth.toolcabinet.service.TestWorkService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.service.TrolDrawerService;
import com.dosth.toolcabinet.util.BaseController;
import com.dosth.toolcabinet.util.DetABorrow;
import com.dosth.toolcabinet.util.LockBoardCabinetUtil;
import com.dosth.toolcabinet.util.ReturnBackUtil;
import com.dosth.toolcabinet.util.ScanUtil;
import com.dosth.util.OpTip;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description 柜子控制器
 * 
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/cabinet")
public class CabinetController extends BaseController {
	public static final Logger logger = LoggerFactory.getLogger(CabinetController.class);

	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ToolService toolService;
	@Autowired
	private TestWorkService testWorkService;
	@Autowired
	private UICompoment uiCom;
	@Autowired
	private ConcreteMediator mediator;
	@Autowired
	private ScanUtil scanUtil;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private DetABorrow detABorrow;
	@Autowired
	private LockBoardCabinetUtil lockBoardCabinetUtil;
	@Autowired
	private BorrowMatService borrowMatService;
	@Autowired
	private CabinetCheckService cabinetCheckService;
	@Autowired
	private ReturnBackService returnBackService;
	@Autowired
	private HopperService hopperService;
	@Autowired
	private DoorService doorService;
	@Autowired
	private ReturnBackDoorService returnBackDoorService;
	@Autowired
	private StorageMediumService storageMediumService;
	@Autowired
	private CnbaosiFeignService cnbaosiFeignService;
	@Autowired
	private TrolDrawerService trolDrawerService;

	/**
	 * @description 获取格子数量集合
	 */
	@RequestMapping("/getLatticeValueMap")
	public Map<String, Integer> getLatticeValueMap(@RequestParam("cabinetId") String cabinetId) {
		String curCabinetId;
		if (cabinetId != null && !"".equals(cabinetId)) {
			curCabinetId = cabinetId;
		} else {
			curCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
					SetupKey.Cabinet.MAIN_CABINET_ID);
		}
		Map<String, Integer> map = toolService.getLatticeValueMap(curCabinetId);
		return map;
	}

	/**
	 * 根据帐号ID获取IC卡号
	 * 
	 * @param accountId 帐号ID
	 * @return
	 */
	@RequestMapping("/getIcCard")
	public String getIcCard(@RequestParam("accountId") String accountId) {
		UserInfo user = toolService.getUserInfoByAccountId(accountId);
		return user.getIcCard();
	}

	/**
	 * @description 人员设定
	 * @param accountId 帐户Id
	 * @param cardNo    IC卡号
	 * @param file      人像
	 * @return
	 */
	@RequestMapping("/bind")
	public OpTip facebind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo,
			@RequestParam(value = "file") String file, HttpSession session) {
		OpTip tip = new OpTip(200, "");
		boolean faceLogin = Boolean.valueOf(DosthToolcabinetRunnerInit
				.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.FACE_LOGIN));

		if (faceLogin) {
			if (file != null && !"".equals(file)) {
				boolean faceFlag = this.authorService.faceBind(accountId, file);
				if (faceFlag) {
					tip.setMessage(tip.getMessage() + "人脸识别，绑定成功<br/>");
				} else {
					tip.setMessage(tip.getMessage() + "人脸识别，绑定失败<br/>");
				}
			} else {
				tip.setMessage(tip.getMessage() + "未获取到人脸特征<br/>");
			}
		}

		if (cardNo != null && !"".equals(cardNo)) {
			boolean icFlag = this.authorService.icBind(accountId, cardNo, null);
			if (icFlag) {
				tip.setMessage(tip.getMessage() + "IC卡，绑定成功<br/>");
			} else {
				tip.setMessage(tip.getMessage() + "IC卡，绑定失败<br/>");
			}
		} else {
			tip.setMessage(tip.getMessage() + "IC卡号为空<br/>");
		}
		return tip;
	}

	/**
	 * @description 人员设定
	 * @param accountId 帐户Id
	 * @param cardNo    IC卡号
	 * @param file      人像
	 * @return
	 */
	@RequestMapping("/icbind")
	public OpTip icbind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo) {
		OpTip tip = new OpTip();
		if (cardNo != null && !"".equals(cardNo)) {
			boolean icFlag = this.authorService.icBind(accountId, cardNo, null);
			if (icFlag) {
				tip = new OpTip(200, "IC卡，绑定成功");
			} else {
				tip = new OpTip(200, "IC卡，绑定失败");
			}
		} else {
			tip = new OpTip(201, "IC卡号为空");
		}
		return tip;
	}

	/**
	 * @description Ic绑定
	 * @param accountId 帐户Id
	 * @param cardNo    Ic号码
	 * @param popedoms  权限信息
	 * @return
	 */
//	@RequestMapping("/icBind")
//	public OpTip icBind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo,
//			@RequestParam("popedoms") String popedoms) {
//		Boolean flag = this.authorService.icBind(accountId, cardNo, popedoms);
//		if (flag != null && flag) {
//			return new OpTip("绑定成功");
//		}
//		return new OpTip(201, "绑定失败");
//	}

//	/**
//	 * @description 人脸识别绑定
//	 * @param accountId 帐户Id
//	 * @param file      人像
//	 * @return
//	 */
//	@RequestMapping("/faceBind")
//	@ResponseBody
//	public OpTip faceBind(@RequestParam(value = "accountId") String accountId,
//			@RequestParam(value = "file") String file) {
//		OpTip tip = new OpTip(200, "");
//		if (file == null) {
//			tip = new OpTip(201, "获取人脸信息失败！");
//			return tip;
//		}
//		if (accountId == null) {
//			tip = new OpTip(201, "获取人员信息失败！");
//			return tip;
//		}
//
//		boolean faceFlag = this.authorService.faceBind(accountId, file);
//		if (faceFlag) {
//			tip = new OpTip(200, "绑定成功");
//			return tip;
//		} else {
//			tip = new OpTip(200, "绑定失败");
//			return tip;
//		}
//	}

	/**
	 * @description 立即领取每日限额判断
	 */
	@RequestMapping("/getDailyLimit")
	public OpTip getDailyLimit(@RequestParam("matId") String matId, @RequestParam("borrowNum") Integer borrowNum,
			HttpServletRequest request) {
		OpTip opTip = new OpTip();
		String accountId = super.getAccountInfo(request).getAccountId();
		// 获取用户限额信息
		UserInfo userInfo = this.toolService.getUserInfoByAccountId(accountId);
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		MatDetail matDetail = this.toolService.getMatRemainNumByMatId(mainCabinetId, matId);
		if (matDetail != null) {
			if (matDetail.getRemainNum() < 1) {
				return opTip = new OpTip(201, "库存不足");
			}
		}
		opTip = this.toolService.getDailyLimit(accountId, matId, borrowNum, userInfo);
		return opTip;
	}

	/**
	 * @description 启动借出
	 */
	@RequestMapping("/startBorr")
	public OpTip startBorr(@RequestParam("data") String data, HttpServletRequest request) {
		OpTip tip = new OpTip(200, "机柜启动服务...");
		String accountId = super.getAccountInfo(request).getAccountId();
		JSONObject obj = JSONObject.fromObject(data);
		String matId = obj.getString("matId");
		String latticeId = obj.getString("latticeId");
		String borrowType = obj.getString("borrowType");
		String host = obj.getString("host");
		String port = obj.getString("port");
		String receiveType = obj.getString("receiveType");
		String receiveInfo = obj.getString("receiveInfo");
		int rowNo = obj.getInt("rowNo");
		int levelHeight = obj.getInt("levelHeight");
		int colNo = obj.getInt("colNo");
		int num = obj.getInt("num");
		String curDoor = obj.getString("curDoor");

		Integer doorHeight = 406;

		// 储物柜
		String boardNo = obj.getString("boardNo");
		String lockIndex = obj.getString("lockIndex");
		String boxIndex = obj.getString("boxIndex");
		// 可控抽屉柜间隔
		int interval = obj.getInt("interval");
		// 当前柜体Id
		String cabinetId = obj.getString("cabinetId");
		String type = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		if (type.equals(CabinetType.SUB_CABINET.name())) {
			type = DosthToolcabinetRunnerInit.getCabinetParam(
					DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.MAIN_CABINET_ID),
					SetupKey.Cabinet.CABINET_TYPE);
		}
		String cabinetType = type;
		if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETB.name())) {
			try {
				this.cabinetCheckService.check(cabinetId, true, true);
			} catch (Exception e1) {
				return new OpTip(201, e1.getMessage());
			}
		}
		// 请求服务端数据发送
		FutureTask<OpTip> task = new FutureTask<>(new Callable<OpTip>() {
			@Override
			public OpTip call() throws Exception {
				return toolService.borrowByGrid(new BorrowGrid(latticeId, matId, num, borrowType), accountId,
						receiveType, receiveInfo);
			}
		});
		new Thread(task).start();
		try {
			OpTip taskTip = task.get();
			if (tip != null && !"".equals(tip.getMessage())) {
				List<Card> cardList = new ArrayList<>();
				List<Lattice> latticeList = new ArrayList<>();
				Lattice lattice = new Lattice();
				lattice.setRecordId(taskTip.getMessage());
				lattice.setStaId(latticeId);
				lattice.getRecordMap().put(taskTip.getMessage(), num);
				lattice.setHost(host);
				lattice.setPort(port);
				lattice.setColNo(colNo);
				lattice.setMatInfo(new MatInfo(matId));
				lattice.setCurReserve(num);
				if (cabinetType.equals(CabinetType.TROL_DRAWER.name())) {
					lattice.setCurReserve(num * interval);
				}
				if (boardNo != null && !"".equals(boardNo)) {
					lattice.setBoardNo(Integer.valueOf(boardNo));
				}
				if (lockIndex != null && !"".equals(lockIndex)) {
					lattice.setLockIndex(Integer.valueOf(lockIndex));
				}
				if (boxIndex != null && !"".equals(boxIndex)) {
					lattice.setBoxIndex(Integer.valueOf(boxIndex));
				}
				lattice.setLevelHeight(levelHeight);
				latticeList.add(lattice);
				// 封装马达板传输数据
				Card card = new Card("", host, port, rowNo, levelHeight);
				card.setDoor(curDoor); // 设置当前关联的门
				card.setLatticeList(latticeList);
				cardList.add(card);

				if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A型柜
					tip.setCode(201);
					detABorrow.callMotor(cardList);
				} else if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
					callMotor(cardList);
				} else { // 行列式B型、储物柜、虚拟仓
					String cabinetName = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId,
							SetupKey.Cabinet.CABINET_NAME);
					List<ExtraCabinet> batchCabinetList = new ArrayList<>();
					ExtraCabinet cabinet = new ExtraCabinet(this.cabinetConfig.getSerialNo(), cabinetId, cabinetId,
							cabinetName, Integer.valueOf(boardNo), curDoor, doorHeight, cabinetType);
					cabinet.setCardList(cardList);
					batchCabinetList.add(cabinet);
					this.storageMediumService.collarUse(batchCabinetList);
				}
			} else {
				tip.setCode(201);
				tip.setMessage("服务端响应失败");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return tip;
	}

	@RequestMapping("/getDailyLimitByCart")
	public OpTip getDailyLimitByCart(HttpServletRequest request) {
		// 暂存柜共享开关
		String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
				SetupKey.TemCabinet.TEM_SHARE_SWITCH);
		if (StringUtils.isBlank(shareSwitch)) {
			shareSwitch = TrueOrFalse.FALSE.toString();
		}
		
		String accountId = super.getAccountInfo(request).getAccountId();
		String data = request.getParameter("data");
		JSONArray array = JSONArray.fromObject(data);
		JSONObject obj = null;
		Map<String, CartInfo> limitMap = new HashMap<>();
		// 获取用户限额信息
		UserInfo userInfo = this.toolService.getUserInfoByAccountId(accountId);
		String startTime = userInfo.getStartTime();
		String endTime = userInfo.getEndTime();
		Integer notReturnLimitNum = userInfo.getNotReturnLimitNum();
		Integer limitSumNum = userInfo.getLimitSumNum();

		if (notReturnLimitNum == null) {
			limitSumNum = 0;
		}

		if (userInfo.getLimitSumNum() == null) {
			notReturnLimitNum = 0;
		}

		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			CartInfo cartInfo = limitMap.get(obj.getString("matId"));
			if (cartInfo != null) {// 相同物料借出数量累计
				cartInfo.setNum(cartInfo.getNum() + obj.getInt("borrowNum"));
			} else {
				cartInfo = new CartInfo(obj.getInt("borrowNum"), obj.getString("borrowType"));
			}
			limitMap.put(obj.getString("matId"), cartInfo);
		}
		OpTip opTip = this.toolService.getDailyLimitByCart(shareSwitch, accountId, startTime, endTime, limitSumNum,
				notReturnLimitNum, limitMap);
		return opTip;
	}

	/**
	 * @description 发送选择信息到后台
	 */
	@RequestMapping("/sendCartToServer")
	public OpTip sendCartToServer(HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		
		
		// 柜子检测
		try {
			this.cabinetCheckService.check(mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(204, e1.getMessage());
		}
		OpTip tip = new OpTip(200, "机柜启动服务...");
		String accountId = super.getAccountInfo(request).getAccountId();
		String cart = request.getParameter("cart");
		JSONArray array = JSONArray.fromObject(cart);
		JSONObject obj = null;
		List<CartInfo> cartList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			cartList.add(new CartInfo(obj.getString("cartId")));
		}
		// 库存量校验
		tip = toolService.stockCkeck(mainCabinetId, cartList);
		if (tip.getCode() == 204) {
			return tip;
		}
		try {
			FutureTask<List<ExtraCabinet>> future = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
				@Override
				public List<ExtraCabinet> call() throws Exception {
					// 暂存柜共享开关
					String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
							SetupKey.TemCabinet.TEM_SHARE_SWITCH);
					if (StringUtils.isBlank(shareSwitch)) {
						shareSwitch = TrueOrFalse.FALSE.toString();
					}
					return toolService.sendCartToServer(mainCabinetId, cartList, shareSwitch, accountId);
				}
			});
			new Thread(future).start();
			List<ExtraCabinet> cabinetList = future.get();
			List<ExtraCabinet> batchCabinetList = new ArrayList<>();
			if (!cabinetList.isEmpty()) {
				// A型柜、PLC独立处理
				for (ExtraCabinet cabinet : cabinetList) {
					if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A型柜
						tip.setCode(201);
						detABorrow.callMotor(cabinet.getCardList());
					} else if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
						tip.setCode(203);
						callMotor(cabinet.getCardList());
					} else { // 行列式B型、储物柜、虚拟仓
						batchCabinetList.add(cabinet);
					}
				}
				if (!batchCabinetList.isEmpty()) {
					if (tip.getCode() == 203) {
						tip.setCode(205);
					}
					this.storageMediumService.collarUse(batchCabinetList);
				}
			} else {
				tip.setCode(204);
				tip.setMessage("库存不足！");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * @description 发送选择申请单到后台
	 */
	@RequestMapping("/sendApplyVoucherToServer")
	public OpTip sendApplyVoucherToServer(HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		// 柜子检测
		try {
			this.cabinetCheckService.check(mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(204, e1.getMessage());
		}
		OpTip tip = new OpTip(200, "机柜启动服务...");
		String accountId = super.getAccountInfo(request).getAccountId();
		String cart = request.getParameter("cart");
		JSONArray array = JSONArray.fromObject(cart);
		JSONObject obj = null;

		// 申请单映射
		Map<String, List<CartInfo>> cartMap = new HashMap<>();

		List<CartInfo> cartList;
		CartInfo info;
		String applyNo;
		String matId;
		Integer borrowNum;
		String borrowType;
		String receiveType;
		String receiveInfo;
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			applyNo = obj.getString("applyNo");
			matId = obj.getString("matId");
			borrowNum = obj.getInt("borrowNum");
			borrowType = obj.getString("borrowType");
			receiveType = obj.getString("receiveType");
			receiveInfo = obj.getString("receiveInfo");
			info = new CartInfo();
			info.setMatId(matId);
			info.setReceiveType(receiveType);
			info.setReceiveInfo(receiveInfo);
			info.setBorrowType(borrowType);
			info.setNum(borrowNum);
			cartList = cartMap.get(applyNo);
			if (cartList == null) {
				cartList = new ArrayList<>();
			}
			cartList.add(info);
			cartMap.put(applyNo, cartList);
		}
		// 库存量校验
		tip = this.toolService.stockCheck(mainCabinetId, cartMap);
		if (tip.getCode() == 204) {
			return tip;
		}
		try {
			FutureTask<List<ExtraCabinet>> future = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
				@Override
				public List<ExtraCabinet> call() throws Exception {
					// 暂存柜共享开关
					String shareSwitch = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId,
							SetupKey.TemCabinet.TEM_SHARE_SWITCH);
					if (StringUtils.isBlank(shareSwitch)) {
						shareSwitch = TrueOrFalse.FALSE.toString();
					}
					return toolService.sendApplyVoucherToServer(mainCabinetId, cartMap, shareSwitch, accountId);
				}
			});
			new Thread(future).start();
			List<ExtraCabinet> cabinetList = future.get();
			List<ExtraCabinet> batchCabinetList = new ArrayList<>();
			if (!cabinetList.isEmpty()) {
				// A型柜、PLC独立处理
				for (ExtraCabinet cabinet : cabinetList) {
					if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A型柜
						tip.setCode(201);
						detABorrow.callMotor(cabinet.getCardList());
					} else if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
						tip.setCode(203);
						callMotor(cabinet.getCardList());
					} else { // 行列式B型、储物柜、虚拟仓
						batchCabinetList.add(cabinet);
					}
				}
				if (!batchCabinetList.isEmpty()) {
					if (tip.getCode() == 203) {
						tip.setCode(205);
					}
					this.storageMediumService.collarUse(batchCabinetList);
				}
			} else {
				tip.setCode(204);
				tip.setMessage("库存不足！");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * @description 创建借出和暂存柜树
	 */
	@RequestMapping("/createBorrAndStorTree")
	public List<ZTreeNode> createBorrAndStorTree() {
		List<ZTreeNode> list = new ArrayList<>();
		list.add(new ZTreeNode("history", "", "待归还物料"));
		list.add(new ZTreeNode("subcabinet", "", "我的暂存柜"));
		return list;
	}

	/**
	 * @description 暂存
	 * @param matUseBillId 借出流水Id
	 * @param borrowNum    暂存数量
	 * @return
	 */
	@RequestMapping("/tempstor/{matUseBillId}/{borrowNum}")
	public OpTip tempstor(@PathVariable("matUseBillId") String matUseBillId, @PathVariable("borrowNum") int borrowNum,
			HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip(201, "暂存失败");
		try {
			opTip = this.toolService.tempStor(matUseBillId, borrowNum, accountId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return opTip;
	}

	/**
	 * @description 获取归还类型组
	 */
	@RequestMapping("/getReBackTypeMap")
	public Map<String, List<ReBackType>> getReBackTypeMap() {
		return this.toolService.getReBackTypeMap();
	}

	/**
	 * @description 绑定条形码归还
	 */
	@RequestMapping("/returnbackBybarcode")
	public OpTip returnbackBybarcode(HttpServletRequest request) {
		OpTip opTip = new OpTip(200, "归还成功");
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		String matBillId = request.getParameter("matBillId"); // 借出物料流水Id
		String matInfoId = request.getParameter("matInfoId"); // 归还物料Id
		int backNum = Integer.valueOf(request.getParameter("backNum")); // 归还数量
		String returnInfo = request.getParameter("returnInfo"); // 归还信息
		String isGetNewOne = request.getParameter("isGetNewOne"); // 依旧换新
		isGetNewOne = isGetNewOne == null ? "0" : isGetNewOne;
		String backWay = request.getParameter("backWay"); // 归还方式
		String barcode = request.getParameter("barcode"); // 条形码
		barcode = barcode == null ? "" : barcode;
//		String matInfoId = request.getParameter("matInfoId");
		String realLife = request.getParameter("realLife"); // 制造产量
		String serialNum = request.getParameter("serialNum"); // 产品流水号
		opTip = this.toolService.findBybarcode(barcode);
		if (opTip.getCode() == 200) {
			ReturnBackPrintInfo printInfo = this.toolService.getPrintInfo(matBillId, accountId, isGetNewOne, matInfoId,
					backNum, mainCabinetId, barcode, returnInfo, backWay, realLife, serialNum);
			String info = printInfo.getInfoMap().get(PrintInfoType.QRINFO);
			// 归还前判断（归还信息是否正确，以旧换新时是否满足领取权限）
			opTip = this.returnBackService.returnBack(printInfo, mainCabinetId, false);
			if (opTip.getCode() != 200) {
				return opTip;
			}
			try {
				ReturnBackUtil.putReturnBackInfo(info);
			} catch (Exception e) {
				e.printStackTrace();
				opTip = new OpTip(201, "归还失败");
			}
			if (isGetNewOne != null && "1".equals(isGetNewOne)) {
				try {
					this.borrowMatService.borrowMat(matBillId, accountId);
				} catch (Exception e) {
					e.printStackTrace();
					return opTip = new OpTip(201, "库存不足");
				}
			}
		}
		return opTip;
	}

	/**
	 * @description 归还打印
	 */
	@RequestMapping("/returnBack")
	public OpTip returnBack(HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		String matBillId = request.getParameter("matBillId"); // 借出物料流水Ids
		String matInfoId = request.getParameter("matInfoId"); // 归还物料Id
		int backNum = Integer.valueOf(request.getParameter("backNum")); // 归还数量
		String returnInfo = request.getParameter("returnInfo"); // 归还信息
		String isGetNewOne = request.getParameter("isGetNewOne"); // 以旧旧换新
		isGetNewOne = isGetNewOne == null ? "0" : isGetNewOne;
		String backWay = request.getParameter("backWay"); // 归还方式
		String barcode = request.getParameter("barcode"); // 归还码
		String realLife = request.getParameter("realLife"); // 制造产量
		String serialNum = request.getParameter("serialNum"); // 产品流水号
		ReturnBackPrintInfo printInfo = this.toolService.getPrintInfo(matBillId, accountId, isGetNewOne, matInfoId,
				backNum, mainCabinetId, barcode, returnInfo, backWay, realLife, serialNum);
		OpTip tip = this.returnBackService.returnBack(printInfo, mainCabinetId, true);
		if (tip.getCode() == 200) {// TODO 以后完善(条码值通过返回消息传输)
			Map<PrintInfoType, String> qrMap = printInfo.getInfoMap();
			String qrInfo = qrMap.get(PrintInfoType.QRINFO);
			if (qrInfo != null && !"".equals(qrInfo)) {
				String[] qrs = qrInfo.split(";");
				if (qrs.length > 0) {
					tip.setMessage(qrs[0]);
				}
			}
		} else {
			logger.error("归还失败:" + tip.getMessage());
		}
		return tip;
	}

	/**
	 * @description 打印确认
	 */
	@RequestMapping("/printConfirm")
	public OpTip printConfirm(HttpServletRequest request) {
		OpTip opTip = new OpTip(200, "打印成功<br/>");
		String qrCached = request.getParameter("qrCached");
		String matBillId = request.getParameter("matBillId");
		String isGetNewOne = request.getParameter("isGetNewOne");
		String accountId = super.getAccountInfo(request).getAccountId();
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		List<NoticeMgrInfo> noticeInfoList = this.toolService.editCount(mainCabinetId);
		if (noticeInfoList != null && noticeInfoList.size() > 0) {
			for (NoticeMgrInfo noticeInfo : noticeInfoList) {
				if (noticeInfo.getWarnValue() == 0) {
					continue;
				}
				if (noticeInfo.getNoticeType().equals("PRINT")) {
					if (noticeInfo.getCount() <= noticeInfo.getWarnValue()) {
						opTip.setMessage(opTip.getMessage() + "打印纸预警，请通知管理员更换<br/>");
					}
				} else if (noticeInfo.getNoticeType().equals("RECOVERY")) {
					if (noticeInfo.getCount() >= noticeInfo.getWarnValue()) {
						opTip.setMessage(opTip.getMessage() + "回收仓预警，请通知管理员回收<br/>");
					}
				}
			}
		}

		try {
			// 打印成功,如扫描仪配置了选项,则启动对应的回收扫描仪
			String recoveryComm = DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId,
					SetupKey.RecCabinet.REC_SCAN_COM);
			if (recoveryComm != null && !"".equals(recoveryComm)) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						MP3Player.play("AUDIO_C1.mp3");
					}
				}).start();
				scanUtil.start(mainCabinetId);
			} else { // 没有扫描仪,则直接将领用记录处理完成
				try {
					ReturnBackUtil.putReturnBackInfo(qrCached);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (isGetNewOne != null && "1".equals(isGetNewOne)) {
					try {
						this.borrowMatService.borrowMat(matBillId, accountId);
					} catch (Exception e) {
						e.printStackTrace();
						return opTip = new OpTip(201, "库存不足");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			opTip = new OpTip(201, "打印异常");
		}
		return opTip;
	}

	/**
	 * @description 操作副柜
	 */
	@RequestMapping("/opSubBox")
	public OpTip opSubBox(HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip(201, "归还失败");
		String subBoxId = request.getParameter("subBoxId");
		String matInfoId = request.getParameter("matInfoId");
		String num = request.getParameter("num");
		try {
			opTip = this.toolService.opSubBox(subBoxId, matInfoId, Integer.valueOf(num), accountId);
			opTip.setMessage("取料成功");
		} catch (Exception e) {
			opTip.setMessage("取料失败");
			e.printStackTrace();
		}
		return opTip;
	}

	/**
	 * @description 批量门操作
	 * @param opFlag 开关门操作标识 true 开门 false 关门
	 * @return
	 */
	@RequestMapping("/batchDoorOp")
	public OpTip batchDoorOp(@RequestParam("opFlag") Boolean opFlag) {
		OpTip tip = new OpTip(200, (opFlag != null && opFlag ? "开门" : "关门") + "进行中...");
		try {
			this.cabinetCheckService.check(DosthToolcabinetRunnerInit.mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(e1.getMessage());
		}
		try {
			this.doorService.batchDoorOp(opFlag);
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("批量门操作失败");
		}
		return tip;
	}

	/**
	 * @description plc操作
	 * @param cabinetId 柜体Id
	 * @param opType    操作类型
	 */
	@RequestMapping("/plcop")
	public OpTip plcop(@RequestParam("cabinetId") String cabinetId, @RequestParam("opType") String opType) {
		PlcOpType type = PlcOpType.valueOf(opType);
		OpTip tip = new OpTip(200, type.getDesc() + "进行中...");
		try {
			this.cabinetCheckService.check(cabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(e1.getMessage());
		}
		try {
			switch (type) {
			case ON_RESET_LIFT_COIL: // 料斗复位
			case ON_UP_COIL: // 料斗上升
			case ON_DOWN_COIL: // 料斗下降
				this.hopperService.op(cabinetId, type);
				break;
			case ON_OPEN_DOOR_COIL: // 开门
			case ON_CLOSE_DOOR_COIL: // 关门
				this.doorService.op(cabinetId, type);
				break;
			case RETURN_BACK_DOOR_REST: // 回收口复位
				this.returnBackDoorService.op(cabinetId, type);
				break;
			case ON_RESET_PLC_STATUS:
				// 软件重置硬件故障状态
				mediator.setShoppCartTakenOut();
				break;
			case RESTART_PRINTER: // 重启打印机
				PLCUtil.opPlc(type);
				break;
			default:
				tip = new OpTip(201, "未匹配到操作类型");
				break;
			}
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage(e.getMessage());
		}
		return tip;
	}

	/**
	 * @description 测试操作
	 */
	@RequestMapping("/testOp/{testWork}")
	public OpTip testOp(@PathVariable String testWork) {
		OpTip tip = new OpTip(200, "测试成功");
		try {
			tip = this.testWorkService.testWork(TestWorkType.valueOf(testWork));
		} catch (Exception e) {
			tip = new OpTip(201, e.getMessage());
		}
		return tip;
	}

	/**
	 * @description 确认待暂存信息
	 */
	@RequestMapping("/haveMatToStor")
	public OpTip haveMatToStor(HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip(200, "存在暂存物料");
		List<SubMatDetail> unSubDetailList = this.toolService.getUnSubMatInfoList(accountId);
		if (unSubDetailList == null || unSubDetailList.size() == 0) {
			opTip = new OpTip(201, "不存在待暂存物料！");
		}
		return opTip;
	}

	/**
	 * @description 打开副柜
	 * @param boxIndex  盒子索引位
	 * @param boardNo   栈号
	 * @param lockIndex 锁位针脚号
	 * @return
	 */
	@RequestMapping("/openBox")
	public OpTip openBox(@RequestParam String cabinetId, @RequestParam Integer boxIndex, @RequestParam int boardNo,
			@RequestParam int lockIndex) {
		// 柜子检测
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		try {
			this.cabinetCheckService.check(mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(204, e1.getMessage());
		}
		OpTip tip = new OpTip(200, "开启成功");
		try {
			this.lockBoardCabinetUtil.putElecLock(new ElecLock(cabinetId, boardNo, lockIndex, boxIndex));
		} catch (Exception e) {
			logger.error("开启锁控板异常:" + e.getMessage());
		}
		return tip;
	}

	/**
	 * @description 打开可控柜
	 * @param cabientId   柜体Id
	 * @param boxIndex    格子索引位
	 * @param boardNo     栈号
	 * @param lockIndex   货道索引号
	 * @param maxReserver 最大库存
	 * @param interval    间隔
	 * @return
	 */
	@RequestMapping("/openTrol")
	public OpTip openTrol(@RequestParam String cabinetId, @RequestParam Integer boxIndex, @RequestParam int boardNo,
			@RequestParam int lockIndex, @RequestParam int maxReserve, @RequestParam int interval) {
		OpTip tip = new OpTip(200, "开启成功");
		try {
			this.trolDrawerService.openTrol(new Integer(boardNo).byteValue(), lockIndex, maxReserve * interval);
		} catch (Exception e) {
			logger.error("开启可控柜异常:" + e.getMessage());
		}
		return tip;
	}

	/**
	 * @description 根据物料Id获取我要暂存信息
	 * @param matId
	 * @return
	 */
	@RequestMapping("/getMatDetail")
	public SubMatDetail getMatDetail(@RequestParam("matId") String matId, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		return this.toolService.getMatDetail(matId, accountId);
	}

	/**
	 * @description 校验新暂存柜权限
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkNewSubCabinet")
	public OpTip checkNewSubCabinet(HttpServletRequest request, @RequestParam("matId") String matId) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip("校验成功");
		try {
			opTip = this.toolService.checkNewSubCabinet(matId, accountId);
		} catch (Exception e) {
			opTip = new OpTip("校验失败");
		}
		return opTip;
	}

	/**
	 * @description 我要暂存
	 * @param matId    物料Id
	 * @param matNum   物料数量
	 * @param storType 暂存种别
	 * @param request
	 * @return
	 */
	@RequestMapping("/tmpNewStor")
	public ElecLock tmpNewStor(@RequestParam("matId") String matId, @RequestParam("matNum") int matNum,
			@RequestParam("storType") String storType, HttpServletRequest request, Model model) {
		String accountId = super.getAccountInfo(request).getAccountId();
		ElecLock lock = this.toolService.tmpNewStor(matId, matNum, storType, accountId);
		return lock;
	}

	/**
	 * @description 通讯马达板
	 * @param cardList 封装马达板数据 synchronized
	 */
	private synchronized void callMotor(List<Card> cardList) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		try {
			PLCUtil.setModbusConfig(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_IP),
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_PORT)),
					1);
			logger.info("到达目标位初始化");
			PLCUtil.clearArriveFlag();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PLCUtil.close();
		}
		WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, "启动取料...."));
		if (mediator.isCurrShoppingCartTakenOut()) {
			mediator.fillShiftCmdStack(cardList);
			uiCom.send(new SoftHandCommMsg(SoftHandComm.SHIFT, mediator.getNextShift()));
			MP3Player.play("AUDIO_D1.mp3");
		} else {
			MP3Player.play("AUDIO_D2.mp3");
		}
	}

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	@RequestMapping("/initAccountTree")
	public List<ZTreeNode> initAccountTree() {
		return this.authorService.initAccountTree();
	}

	/**
	 * @description 修改物料信息
	 * @param equDetailId
	 * @param value
	 * @return
	 */
	@RequestMapping("/updateDetailStaValue")
	public OpTip updateDetailStaValue(String equDetailId, Integer value, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		Boolean flag = this.toolService.updateDetailStaValue(equDetailId, value, accountId);
		if (flag != null && flag) {
			return new OpTip("修改成功");
		}
		return new OpTip(201, "修改失败");
	}

	/**
	 * @description 初始化借出权限树
	 * @return
	 */
	@RequestMapping("/initBorrowPopedomTree")
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("accountId") String accountId) {
		return this.toolService.initBorrowPopedomTree(accountId);
	}

	/**
	 * @description 完成补料清单
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/finishFeedingList")
	public OpTip finishFeedingList(@RequestParam("feedingListId") String feedingListId, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		return this.toolService.finishFeedingList(feedingListId, accountId);
	}

	/**
	 * @description 校验session
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkSession")
	public String checkSession(HttpServletRequest request) {
		try {
			String sessionTimeOutFlag = (String) request.getSession()
					.getAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG);
			System.err.println("sessionTimeOutFlag >>>>>>>>>>>>>>>>>>>" + sessionTimeOutFlag);
			if (sessionTimeOutFlag != null && "Y".equals(sessionTimeOutFlag)) {
				request.getSession().invalidate();
				return sessionTimeOutFlag;
			}
			return sessionTimeOutFlag;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @description 消除session
	 * @param session session标识
	 */
	@RequestMapping("/revokeSession")
	public String revokeSession(HttpSession session) {
		session.setAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG, "Y");
		return "succ";
	}

	/**
	 * @description 退出系统
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "/login";
	}

	/**
	 * @description 补料信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/feed")
	@ResponseBody
	public OpTip feed(HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		String feed = request.getParameter("feed"); // 补料信息
		String query = request.getParameter("query"); // 柜子ID
		Boolean flag = this.toolService.feed(feed, query, accountId);
		if (flag != null && flag) {
			return new OpTip("补料成功");
		}
		return new OpTip(201, "补料失败");
	}

	/**
	 * @description 读取柜子错误状态信息
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/readErrStatus")
	@ResponseBody
	public String readErrStatus(@RequestParam("cabinetId") String cabinetId) {
		StringBuilder cabinetErrStatus = new StringBuilder();
		String cabinetType = DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE);
		if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) {
			try {
				ProblemMonitor monitor = new ProblemMonitor() {
					@Override
					public void receiveMonitorData(MonitorData data) {
						cabinetErrStatus.append(data.toString());
					}
				};
				monitor.addAddrType(AddrType.ServoAlarm);
				// 验证是否为C型柜
				if (DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE)
						.equals(CabinetType.KNIFE_CABINET_C.name())) { // C型柜
					monitor.addAddrType(AddrType.LeftDoorSignal);
					monitor.addAddrType(AddrType.RightDoorSignal);
				} else { // 其他类型柜
					monitor.addAddrType(AddrType.DoorSignal);
				}
				monitor.startMonitor(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
						Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cabinetErrStatus.toString();
	}

	/**
	 * @description 重置门故障状态
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/resetDoorErr")
	@ResponseBody
	public String resetDoorErr(@RequestParam("cabinetId") String cabinetId) {
		String cabinetErrStatus = "门故障恢复成功";
		try {
			ProblemMonitor.resetDoorErr(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT)));
		} catch (Exception e) {
			cabinetErrStatus = "门故障恢复失败";
			e.printStackTrace();
		}
		return cabinetErrStatus;
	}

	/**
	 * @description 故障状态恢复
	 * @param cabinetId 当前柜子Id
	 * @return
	 */
	@RequestMapping("/resetProblem")
	@ResponseBody
	public String resetProblem(@RequestParam("cabinetId") String cabinetId) {
		try {
			CabinetConstant.collisionFlag = false;
			CabinetConstant.connectFlag = true;
			CabinetConstant.busyFlag = false;
			Boolean flag = this.toolService.resetProblem(cabinetId);
			if (flag != null && flag) {
				return "故障状态恢复成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "故障状态恢复成功失败";
	}

	/**
	 * @description 通知管理-重置计数
	 * @param num        数量
	 * @param noticeType 通知类型
	 * @return
	 */
	@RequestMapping("/resetNoticeMgr")
	@ResponseBody
	public OpTip resetNoticeMgr(@RequestParam("num") int num, @RequestParam("noticeType") String noticeType,
			Model model) {
		OpTip tip = new OpTip(200, "重置成功");
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		this.toolService.resetNoticeMgr(mainCabinetId, num, noticeType);
		return tip;
	}

	/**
	 * @description 连接校验
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/checkConnect")
	public OpTip checkConnect(@RequestParam("cabinetId") String cabinetId) {
		OpTip tip = new OpTip("连接状态校验");
		this.cabinetCheckService.checkConnect(cabinetId);
		return tip;
	}

	/**
	 * @description 借用信息推送
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/borrowPost")
	public void borrowPost(@RequestParam(value = "recordId") String recordId) {
		this.toolService.borrowPost(recordId);
	}

	/**
	 * @description 库存预警邮件
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/stockEmail")
	public void stockEmail(@RequestParam("cabinetId") String cabinetId) {
		this.toolService.stockEmail(cabinetId);
	}

	/**
	 * @description 推送领料单结果
	 * @param applyVoucherResult 领料单结果
	 */
	@RequestMapping("/sendApplyVoucherResult")
	public void sendApplyVoucherResult(@RequestParam("applyVoucherResult") String applyVoucherResult) {
		this.cnbaosiFeignService.sendApplyVoucherResult(applyVoucherResult);
	}

	/**
	 * @description 同步补料单
	 * @param feedingListId 补料单Id
	 */
	@RequestMapping("/syncFeedingList")
	public void syncFeedingList(@RequestParam("feedingListId") String feedingListId) {
		this.cnbaosiFeignService.syncFeedingList(feedingListId);
	}
}