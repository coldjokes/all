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
 * @description ???????????????
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
	 * @description ????????????????????????
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
	 * ????????????ID??????IC??????
	 * 
	 * @param accountId ??????ID
	 * @return
	 */
	@RequestMapping("/getIcCard")
	public String getIcCard(@RequestParam("accountId") String accountId) {
		UserInfo user = toolService.getUserInfoByAccountId(accountId);
		return user.getIcCard();
	}

	/**
	 * @description ????????????
	 * @param accountId ??????Id
	 * @param cardNo    IC??????
	 * @param file      ??????
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
					tip.setMessage(tip.getMessage() + "???????????????????????????<br/>");
				} else {
					tip.setMessage(tip.getMessage() + "???????????????????????????<br/>");
				}
			} else {
				tip.setMessage(tip.getMessage() + "????????????????????????<br/>");
			}
		}

		if (cardNo != null && !"".equals(cardNo)) {
			boolean icFlag = this.authorService.icBind(accountId, cardNo, null);
			if (icFlag) {
				tip.setMessage(tip.getMessage() + "IC??????????????????<br/>");
			} else {
				tip.setMessage(tip.getMessage() + "IC??????????????????<br/>");
			}
		} else {
			tip.setMessage(tip.getMessage() + "IC????????????<br/>");
		}
		return tip;
	}

	/**
	 * @description ????????????
	 * @param accountId ??????Id
	 * @param cardNo    IC??????
	 * @param file      ??????
	 * @return
	 */
	@RequestMapping("/icbind")
	public OpTip icbind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo) {
		OpTip tip = new OpTip();
		if (cardNo != null && !"".equals(cardNo)) {
			boolean icFlag = this.authorService.icBind(accountId, cardNo, null);
			if (icFlag) {
				tip = new OpTip(200, "IC??????????????????");
			} else {
				tip = new OpTip(200, "IC??????????????????");
			}
		} else {
			tip = new OpTip(201, "IC????????????");
		}
		return tip;
	}

	/**
	 * @description Ic??????
	 * @param accountId ??????Id
	 * @param cardNo    Ic??????
	 * @param popedoms  ????????????
	 * @return
	 */
//	@RequestMapping("/icBind")
//	public OpTip icBind(@RequestParam("accountId") String accountId, @RequestParam("cardNo") String cardNo,
//			@RequestParam("popedoms") String popedoms) {
//		Boolean flag = this.authorService.icBind(accountId, cardNo, popedoms);
//		if (flag != null && flag) {
//			return new OpTip("????????????");
//		}
//		return new OpTip(201, "????????????");
//	}

//	/**
//	 * @description ??????????????????
//	 * @param accountId ??????Id
//	 * @param file      ??????
//	 * @return
//	 */
//	@RequestMapping("/faceBind")
//	@ResponseBody
//	public OpTip faceBind(@RequestParam(value = "accountId") String accountId,
//			@RequestParam(value = "file") String file) {
//		OpTip tip = new OpTip(200, "");
//		if (file == null) {
//			tip = new OpTip(201, "???????????????????????????");
//			return tip;
//		}
//		if (accountId == null) {
//			tip = new OpTip(201, "???????????????????????????");
//			return tip;
//		}
//
//		boolean faceFlag = this.authorService.faceBind(accountId, file);
//		if (faceFlag) {
//			tip = new OpTip(200, "????????????");
//			return tip;
//		} else {
//			tip = new OpTip(200, "????????????");
//			return tip;
//		}
//	}

	/**
	 * @description ??????????????????????????????
	 */
	@RequestMapping("/getDailyLimit")
	public OpTip getDailyLimit(@RequestParam("matId") String matId, @RequestParam("borrowNum") Integer borrowNum,
			HttpServletRequest request) {
		OpTip opTip = new OpTip();
		String accountId = super.getAccountInfo(request).getAccountId();
		// ????????????????????????
		UserInfo userInfo = this.toolService.getUserInfoByAccountId(accountId);
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		MatDetail matDetail = this.toolService.getMatRemainNumByMatId(mainCabinetId, matId);
		if (matDetail != null) {
			if (matDetail.getRemainNum() < 1) {
				return opTip = new OpTip(201, "????????????");
			}
		}
		opTip = this.toolService.getDailyLimit(accountId, matId, borrowNum, userInfo);
		return opTip;
	}

	/**
	 * @description ????????????
	 */
	@RequestMapping("/startBorr")
	public OpTip startBorr(@RequestParam("data") String data, HttpServletRequest request) {
		OpTip tip = new OpTip(200, "??????????????????...");
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

		// ?????????
		String boardNo = obj.getString("boardNo");
		String lockIndex = obj.getString("lockIndex");
		String boxIndex = obj.getString("boxIndex");
		// ?????????????????????
		int interval = obj.getInt("interval");
		// ????????????Id
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
		// ???????????????????????????
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
				// ???????????????????????????
				Card card = new Card("", host, port, rowNo, levelHeight);
				card.setDoor(curDoor); // ????????????????????????
				card.setLatticeList(latticeList);
				cardList.add(card);

				if (cabinetType.equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A??????
					tip.setCode(201);
					detABorrow.callMotor(cardList);
				} else if (cabinetType.equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
					callMotor(cardList);
				} else { // ?????????B???????????????????????????
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
				tip.setMessage("?????????????????????");
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return tip;
	}

	@RequestMapping("/getDailyLimitByCart")
	public OpTip getDailyLimitByCart(HttpServletRequest request) {
		// ?????????????????????
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
		// ????????????????????????
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
			if (cartInfo != null) {// ??????????????????????????????
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
	 * @description ???????????????????????????
	 */
	@RequestMapping("/sendCartToServer")
	public OpTip sendCartToServer(HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		
		
		// ????????????
		try {
			this.cabinetCheckService.check(mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(204, e1.getMessage());
		}
		OpTip tip = new OpTip(200, "??????????????????...");
		String accountId = super.getAccountInfo(request).getAccountId();
		String cart = request.getParameter("cart");
		JSONArray array = JSONArray.fromObject(cart);
		JSONObject obj = null;
		List<CartInfo> cartList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			cartList.add(new CartInfo(obj.getString("cartId")));
		}
		// ???????????????
		tip = toolService.stockCkeck(mainCabinetId, cartList);
		if (tip.getCode() == 204) {
			return tip;
		}
		try {
			FutureTask<List<ExtraCabinet>> future = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
				@Override
				public List<ExtraCabinet> call() throws Exception {
					// ?????????????????????
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
				// A?????????PLC????????????
				for (ExtraCabinet cabinet : cabinetList) {
					if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A??????
						tip.setCode(201);
						detABorrow.callMotor(cabinet.getCardList());
					} else if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
						tip.setCode(203);
						callMotor(cabinet.getCardList());
					} else { // ?????????B???????????????????????????
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
				tip.setMessage("???????????????");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * @description ??????????????????????????????
	 */
	@RequestMapping("/sendApplyVoucherToServer")
	public OpTip sendApplyVoucherToServer(HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		// ????????????
		try {
			this.cabinetCheckService.check(mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(204, e1.getMessage());
		}
		OpTip tip = new OpTip(200, "??????????????????...");
		String accountId = super.getAccountInfo(request).getAccountId();
		String cart = request.getParameter("cart");
		JSONArray array = JSONArray.fromObject(cart);
		JSONObject obj = null;

		// ???????????????
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
		// ???????????????
		tip = this.toolService.stockCheck(mainCabinetId, cartMap);
		if (tip.getCode() == 204) {
			return tip;
		}
		try {
			FutureTask<List<ExtraCabinet>> future = new FutureTask<>(new Callable<List<ExtraCabinet>>() {
				@Override
				public List<ExtraCabinet> call() throws Exception {
					// ?????????????????????
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
				// A?????????PLC????????????
				for (ExtraCabinet cabinet : cabinetList) {
					if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_DETA.name())) { // A??????
						tip.setCode(201);
						detABorrow.callMotor(cabinet.getCardList());
					} else if (cabinet.getCabinetType().equals(CabinetType.KNIFE_CABINET_PLC.name())) { // PLC
						tip.setCode(203);
						callMotor(cabinet.getCardList());
					} else { // ?????????B???????????????????????????
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
				tip.setMessage("???????????????");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * @description ???????????????????????????
	 */
	@RequestMapping("/createBorrAndStorTree")
	public List<ZTreeNode> createBorrAndStorTree() {
		List<ZTreeNode> list = new ArrayList<>();
		list.add(new ZTreeNode("history", "", "???????????????"));
		list.add(new ZTreeNode("subcabinet", "", "???????????????"));
		return list;
	}

	/**
	 * @description ??????
	 * @param matUseBillId ????????????Id
	 * @param borrowNum    ????????????
	 * @return
	 */
	@RequestMapping("/tempstor/{matUseBillId}/{borrowNum}")
	public OpTip tempstor(@PathVariable("matUseBillId") String matUseBillId, @PathVariable("borrowNum") int borrowNum,
			HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip(201, "????????????");
		try {
			opTip = this.toolService.tempStor(matUseBillId, borrowNum, accountId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return opTip;
	}

	/**
	 * @description ?????????????????????
	 */
	@RequestMapping("/getReBackTypeMap")
	public Map<String, List<ReBackType>> getReBackTypeMap() {
		return this.toolService.getReBackTypeMap();
	}

	/**
	 * @description ?????????????????????
	 */
	@RequestMapping("/returnbackBybarcode")
	public OpTip returnbackBybarcode(HttpServletRequest request) {
		OpTip opTip = new OpTip(200, "????????????");
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		String matBillId = request.getParameter("matBillId"); // ??????????????????Id
		String matInfoId = request.getParameter("matInfoId"); // ????????????Id
		int backNum = Integer.valueOf(request.getParameter("backNum")); // ????????????
		String returnInfo = request.getParameter("returnInfo"); // ????????????
		String isGetNewOne = request.getParameter("isGetNewOne"); // ????????????
		isGetNewOne = isGetNewOne == null ? "0" : isGetNewOne;
		String backWay = request.getParameter("backWay"); // ????????????
		String barcode = request.getParameter("barcode"); // ?????????
		barcode = barcode == null ? "" : barcode;
//		String matInfoId = request.getParameter("matInfoId");
		String realLife = request.getParameter("realLife"); // ????????????
		String serialNum = request.getParameter("serialNum"); // ???????????????
		opTip = this.toolService.findBybarcode(barcode);
		if (opTip.getCode() == 200) {
			ReturnBackPrintInfo printInfo = this.toolService.getPrintInfo(matBillId, accountId, isGetNewOne, matInfoId,
					backNum, mainCabinetId, barcode, returnInfo, backWay, realLife, serialNum);
			String info = printInfo.getInfoMap().get(PrintInfoType.QRINFO);
			// ???????????????????????????????????????????????????????????????????????????????????????
			opTip = this.returnBackService.returnBack(printInfo, mainCabinetId, false);
			if (opTip.getCode() != 200) {
				return opTip;
			}
			try {
				ReturnBackUtil.putReturnBackInfo(info);
			} catch (Exception e) {
				e.printStackTrace();
				opTip = new OpTip(201, "????????????");
			}
			if (isGetNewOne != null && "1".equals(isGetNewOne)) {
				try {
					this.borrowMatService.borrowMat(matBillId, accountId);
				} catch (Exception e) {
					e.printStackTrace();
					return opTip = new OpTip(201, "????????????");
				}
			}
		}
		return opTip;
	}

	/**
	 * @description ????????????
	 */
	@RequestMapping("/returnBack")
	public OpTip returnBack(HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		String matBillId = request.getParameter("matBillId"); // ??????????????????Ids
		String matInfoId = request.getParameter("matInfoId"); // ????????????Id
		int backNum = Integer.valueOf(request.getParameter("backNum")); // ????????????
		String returnInfo = request.getParameter("returnInfo"); // ????????????
		String isGetNewOne = request.getParameter("isGetNewOne"); // ???????????????
		isGetNewOne = isGetNewOne == null ? "0" : isGetNewOne;
		String backWay = request.getParameter("backWay"); // ????????????
		String barcode = request.getParameter("barcode"); // ?????????
		String realLife = request.getParameter("realLife"); // ????????????
		String serialNum = request.getParameter("serialNum"); // ???????????????
		ReturnBackPrintInfo printInfo = this.toolService.getPrintInfo(matBillId, accountId, isGetNewOne, matInfoId,
				backNum, mainCabinetId, barcode, returnInfo, backWay, realLife, serialNum);
		OpTip tip = this.returnBackService.returnBack(printInfo, mainCabinetId, true);
		if (tip.getCode() == 200) {// TODO ????????????(?????????????????????????????????)
			Map<PrintInfoType, String> qrMap = printInfo.getInfoMap();
			String qrInfo = qrMap.get(PrintInfoType.QRINFO);
			if (qrInfo != null && !"".equals(qrInfo)) {
				String[] qrs = qrInfo.split(";");
				if (qrs.length > 0) {
					tip.setMessage(qrs[0]);
				}
			}
		} else {
			logger.error("????????????:" + tip.getMessage());
		}
		return tip;
	}

	/**
	 * @description ????????????
	 */
	@RequestMapping("/printConfirm")
	public OpTip printConfirm(HttpServletRequest request) {
		OpTip opTip = new OpTip(200, "????????????<br/>");
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
						opTip.setMessage(opTip.getMessage() + "??????????????????????????????????????????<br/>");
					}
				} else if (noticeInfo.getNoticeType().equals("RECOVERY")) {
					if (noticeInfo.getCount() >= noticeInfo.getWarnValue()) {
						opTip.setMessage(opTip.getMessage() + "??????????????????????????????????????????<br/>");
					}
				}
			}
		}

		try {
			// ????????????,???????????????????????????,?????????????????????????????????
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
			} else { // ???????????????,????????????????????????????????????
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
						return opTip = new OpTip(201, "????????????");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			opTip = new OpTip(201, "????????????");
		}
		return opTip;
	}

	/**
	 * @description ????????????
	 */
	@RequestMapping("/opSubBox")
	public OpTip opSubBox(HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip(201, "????????????");
		String subBoxId = request.getParameter("subBoxId");
		String matInfoId = request.getParameter("matInfoId");
		String num = request.getParameter("num");
		try {
			opTip = this.toolService.opSubBox(subBoxId, matInfoId, Integer.valueOf(num), accountId);
			opTip.setMessage("????????????");
		} catch (Exception e) {
			opTip.setMessage("????????????");
			e.printStackTrace();
		}
		return opTip;
	}

	/**
	 * @description ???????????????
	 * @param opFlag ????????????????????? true ?????? false ??????
	 * @return
	 */
	@RequestMapping("/batchDoorOp")
	public OpTip batchDoorOp(@RequestParam("opFlag") Boolean opFlag) {
		OpTip tip = new OpTip(200, (opFlag != null && opFlag ? "??????" : "??????") + "?????????...");
		try {
			this.cabinetCheckService.check(DosthToolcabinetRunnerInit.mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(e1.getMessage());
		}
		try {
			this.doorService.batchDoorOp(opFlag);
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("?????????????????????");
		}
		return tip;
	}

	/**
	 * @description plc??????
	 * @param cabinetId ??????Id
	 * @param opType    ????????????
	 */
	@RequestMapping("/plcop")
	public OpTip plcop(@RequestParam("cabinetId") String cabinetId, @RequestParam("opType") String opType) {
		PlcOpType type = PlcOpType.valueOf(opType);
		OpTip tip = new OpTip(200, type.getDesc() + "?????????...");
		try {
			this.cabinetCheckService.check(cabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(e1.getMessage());
		}
		try {
			switch (type) {
			case ON_RESET_LIFT_COIL: // ????????????
			case ON_UP_COIL: // ????????????
			case ON_DOWN_COIL: // ????????????
				this.hopperService.op(cabinetId, type);
				break;
			case ON_OPEN_DOOR_COIL: // ??????
			case ON_CLOSE_DOOR_COIL: // ??????
				this.doorService.op(cabinetId, type);
				break;
			case RETURN_BACK_DOOR_REST: // ???????????????
				this.returnBackDoorService.op(cabinetId, type);
				break;
			case ON_RESET_PLC_STATUS:
				// ??????????????????????????????
				mediator.setShoppCartTakenOut();
				break;
			case RESTART_PRINTER: // ???????????????
				PLCUtil.opPlc(type);
				break;
			default:
				tip = new OpTip(201, "????????????????????????");
				break;
			}
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage(e.getMessage());
		}
		return tip;
	}

	/**
	 * @description ????????????
	 */
	@RequestMapping("/testOp/{testWork}")
	public OpTip testOp(@PathVariable String testWork) {
		OpTip tip = new OpTip(200, "????????????");
		try {
			tip = this.testWorkService.testWork(TestWorkType.valueOf(testWork));
		} catch (Exception e) {
			tip = new OpTip(201, e.getMessage());
		}
		return tip;
	}

	/**
	 * @description ?????????????????????
	 */
	@RequestMapping("/haveMatToStor")
	public OpTip haveMatToStor(HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip(200, "??????????????????");
		List<SubMatDetail> unSubDetailList = this.toolService.getUnSubMatInfoList(accountId);
		if (unSubDetailList == null || unSubDetailList.size() == 0) {
			opTip = new OpTip(201, "???????????????????????????");
		}
		return opTip;
	}

	/**
	 * @description ????????????
	 * @param boxIndex  ???????????????
	 * @param boardNo   ??????
	 * @param lockIndex ???????????????
	 * @return
	 */
	@RequestMapping("/openBox")
	public OpTip openBox(@RequestParam String cabinetId, @RequestParam Integer boxIndex, @RequestParam int boardNo,
			@RequestParam int lockIndex) {
		// ????????????
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		try {
			this.cabinetCheckService.check(mainCabinetId, true, true);
		} catch (Exception e1) {
			return new OpTip(204, e1.getMessage());
		}
		OpTip tip = new OpTip(200, "????????????");
		try {
			this.lockBoardCabinetUtil.putElecLock(new ElecLock(cabinetId, boardNo, lockIndex, boxIndex));
		} catch (Exception e) {
			logger.error("?????????????????????:" + e.getMessage());
		}
		return tip;
	}

	/**
	 * @description ???????????????
	 * @param cabientId   ??????Id
	 * @param boxIndex    ???????????????
	 * @param boardNo     ??????
	 * @param lockIndex   ???????????????
	 * @param maxReserver ????????????
	 * @param interval    ??????
	 * @return
	 */
	@RequestMapping("/openTrol")
	public OpTip openTrol(@RequestParam String cabinetId, @RequestParam Integer boxIndex, @RequestParam int boardNo,
			@RequestParam int lockIndex, @RequestParam int maxReserve, @RequestParam int interval) {
		OpTip tip = new OpTip(200, "????????????");
		try {
			this.trolDrawerService.openTrol(new Integer(boardNo).byteValue(), lockIndex, maxReserve * interval);
		} catch (Exception e) {
			logger.error("?????????????????????:" + e.getMessage());
		}
		return tip;
	}

	/**
	 * @description ????????????Id????????????????????????
	 * @param matId
	 * @return
	 */
	@RequestMapping("/getMatDetail")
	public SubMatDetail getMatDetail(@RequestParam("matId") String matId, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		return this.toolService.getMatDetail(matId, accountId);
	}

	/**
	 * @description ????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("/checkNewSubCabinet")
	public OpTip checkNewSubCabinet(HttpServletRequest request, @RequestParam("matId") String matId) {
		String accountId = super.getAccountInfo(request).getAccountId();
		OpTip opTip = new OpTip("????????????");
		try {
			opTip = this.toolService.checkNewSubCabinet(matId, accountId);
		} catch (Exception e) {
			opTip = new OpTip("????????????");
		}
		return opTip;
	}

	/**
	 * @description ????????????
	 * @param matId    ??????Id
	 * @param matNum   ????????????
	 * @param storType ????????????
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
	 * @description ???????????????
	 * @param cardList ????????????????????? synchronized
	 */
	private synchronized void callMotor(List<Card> cardList) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		try {
			PLCUtil.setModbusConfig(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_IP),
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(mainCabinetId, SetupKey.Plc.PLC_PORT)),
					1);
			logger.info("????????????????????????");
			PLCUtil.clearArriveFlag();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			PLCUtil.close();
		}
		WebSocketUtil.sendMsgSingle(mainCabinetId, new WsMsg(WsMsgType.TEXT, "????????????...."));
		if (mediator.isCurrShoppingCartTakenOut()) {
			mediator.fillShiftCmdStack(cardList);
			uiCom.send(new SoftHandCommMsg(SoftHandComm.SHIFT, mediator.getNextShift()));
			MP3Player.play("AUDIO_D1.mp3");
		} else {
			MP3Player.play("AUDIO_D2.mp3");
		}
	}

	/**
	 * @description ??????????????????
	 * @return
	 */
	@RequestMapping("/initAccountTree")
	public List<ZTreeNode> initAccountTree() {
		return this.authorService.initAccountTree();
	}

	/**
	 * @description ??????????????????
	 * @param equDetailId
	 * @param value
	 * @return
	 */
	@RequestMapping("/updateDetailStaValue")
	public OpTip updateDetailStaValue(String equDetailId, Integer value, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		Boolean flag = this.toolService.updateDetailStaValue(equDetailId, value, accountId);
		if (flag != null && flag) {
			return new OpTip("????????????");
		}
		return new OpTip(201, "????????????");
	}

	/**
	 * @description ????????????????????????
	 * @return
	 */
	@RequestMapping("/initBorrowPopedomTree")
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("accountId") String accountId) {
		return this.toolService.initBorrowPopedomTree(accountId);
	}

	/**
	 * @description ??????????????????
	 * @param feedingListId ????????????Id
	 * @return
	 */
	@RequestMapping("/finishFeedingList")
	public OpTip finishFeedingList(@RequestParam("feedingListId") String feedingListId, HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		return this.toolService.finishFeedingList(feedingListId, accountId);
	}

	/**
	 * @description ??????session
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
	 * @description ??????session
	 * @param session session??????
	 */
	@RequestMapping("/revokeSession")
	public String revokeSession(HttpSession session) {
		session.setAttribute(CabinetConfiguration.SESSION_TIMEOUT_FLAG, "Y");
		return "succ";
	}

	/**
	 * @description ????????????
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "/login";
	}

	/**
	 * @description ????????????
	 * @param request
	 * @return
	 */
	@RequestMapping("/feed")
	@ResponseBody
	public OpTip feed(HttpServletRequest request) {
		String accountId = super.getAccountInfo(request).getAccountId();
		String feed = request.getParameter("feed"); // ????????????
		String query = request.getParameter("query"); // ??????ID
		Boolean flag = this.toolService.feed(feed, query, accountId);
		if (flag != null && flag) {
			return new OpTip("????????????");
		}
		return new OpTip(201, "????????????");
	}

	/**
	 * @description ??????????????????????????????
	 * @param cabinetId ??????Id
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
				// ???????????????C??????
				if (DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Cabinet.CABINET_TYPE)
						.equals(CabinetType.KNIFE_CABINET_C.name())) { // C??????
					monitor.addAddrType(AddrType.LeftDoorSignal);
					monitor.addAddrType(AddrType.RightDoorSignal);
				} else { // ???????????????
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
	 * @description ?????????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/resetDoorErr")
	@ResponseBody
	public String resetDoorErr(@RequestParam("cabinetId") String cabinetId) {
		String cabinetErrStatus = "?????????????????????";
		try {
			ProblemMonitor.resetDoorErr(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_IP),
					Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(cabinetId, SetupKey.Plc.PLC_PORT)));
		} catch (Exception e) {
			cabinetErrStatus = "?????????????????????";
			e.printStackTrace();
		}
		return cabinetErrStatus;
	}

	/**
	 * @description ??????????????????
	 * @param cabinetId ????????????Id
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
				return "????????????????????????";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "??????????????????????????????";
	}

	/**
	 * @description ????????????-????????????
	 * @param num        ??????
	 * @param noticeType ????????????
	 * @return
	 */
	@RequestMapping("/resetNoticeMgr")
	@ResponseBody
	public OpTip resetNoticeMgr(@RequestParam("num") int num, @RequestParam("noticeType") String noticeType,
			Model model) {
		OpTip tip = new OpTip(200, "????????????");
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		this.toolService.resetNoticeMgr(mainCabinetId, num, noticeType);
		return tip;
	}

	/**
	 * @description ????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/checkConnect")
	public OpTip checkConnect(@RequestParam("cabinetId") String cabinetId) {
		OpTip tip = new OpTip("??????????????????");
		this.cabinetCheckService.checkConnect(cabinetId);
		return tip;
	}

	/**
	 * @description ??????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/borrowPost")
	public void borrowPost(@RequestParam(value = "recordId") String recordId) {
		this.toolService.borrowPost(recordId);
	}

	/**
	 * @description ??????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/stockEmail")
	public void stockEmail(@RequestParam("cabinetId") String cabinetId) {
		this.toolService.stockEmail(cabinetId);
	}

	/**
	 * @description ?????????????????????
	 * @param applyVoucherResult ???????????????
	 */
	@RequestMapping("/sendApplyVoucherResult")
	public void sendApplyVoucherResult(@RequestParam("applyVoucherResult") String applyVoucherResult) {
		this.cnbaosiFeignService.sendApplyVoucherResult(applyVoucherResult);
	}

	/**
	 * @description ???????????????
	 * @param feedingListId ?????????Id
	 */
	@RequestMapping("/syncFeedingList")
	public void syncFeedingList(@RequestParam("feedingListId") String feedingListId) {
		this.cnbaosiFeignService.syncFeedingList(feedingListId);
	}
}