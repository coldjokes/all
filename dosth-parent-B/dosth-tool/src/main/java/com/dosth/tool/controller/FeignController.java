package com.dosth.tool.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.dto.ElecLock;
import com.cnbaosi.dto.tool.ApplyVoucher;
import com.cnbaosi.dto.tool.FeignCodeName;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignWarehouseFeed;
import com.dosth.comm.LockPara;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.TypeNode;
import com.dosth.common.node.ZTreeNode;
import com.dosth.constant.EnumDoor;
import com.dosth.dto.BorrowNotice;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraBoxNum;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.Lattice;
import com.dosth.dto.StockTip;
import com.dosth.dto.TrolDrawerNotice;
import com.dosth.statistics.dto.MonthCost;
import com.dosth.tool.MyPage;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.common.util.StockEmailUtil;
import com.dosth.tool.entity.FeedingList;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.external.service.ExternalService;
import com.dosth.tool.rpc.CabinetRpcService;
import com.dosth.tool.rpc.StatisticsService;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.BorrowPopedomService;
import com.dosth.tool.service.CartService;
import com.dosth.tool.service.DailyLimitService;
import com.dosth.tool.service.EquDetailService;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.ExtraBoxNumSettingService;
import com.dosth.tool.service.FeedingDetailService;
import com.dosth.tool.service.FeedingListService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.tool.service.MatReturnBackService;
import com.dosth.tool.service.MatUseBillService;
import com.dosth.tool.service.MatUseRecordService;
import com.dosth.tool.service.NoticeMgrService;
import com.dosth.tool.service.SubBoxAccountRefService;
import com.dosth.tool.service.SubCabinetService;
import com.dosth.tool.service.ToolPullService;
import com.dosth.tool.service.WarehouseFeedService;
import com.dosth.toolcabinet.dto.BillInfo;
import com.dosth.toolcabinet.dto.BorrowGrid;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.ExtractionMethod;
import com.dosth.toolcabinet.dto.FeedDetailSta;
import com.dosth.toolcabinet.dto.MatDetail;
import com.dosth.toolcabinet.dto.NoticeMgrInfo;
import com.dosth.toolcabinet.dto.ReBackType;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.dto.RpcBorrowType;
import com.dosth.toolcabinet.dto.SubMatDetail;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.util.OpTip;

/**
 * @description ????????????????????????
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/feign")
public class FeignController {

	@Autowired
	private StockEmailUtil stockEmailUtil;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private CabinetRpcService cabinetRpcService;
	@Autowired
	private StatisticsService statisticsService;
	@Autowired
	private FeedingListService feedingListService;
	@Autowired
	private FeedingDetailService feedingDetailService;
	@Autowired
	private BorrowPopedomService borrowPopedomService;
	@Autowired
	private MatEquInfoService matEquInfoService;
	@Autowired
	private CartService cartService;
	@Autowired
	private SubBoxAccountRefService subBoxAccountRefService;
	@Autowired
	private SubCabinetService subCabinetService;
	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private DailyLimitService dailyLimitService;
	@Autowired
	private EquDetailService equDetailService;
	@Autowired
	private AdminService adminService;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private NoticeMgrService noticeMgrService;
	@Autowired
	private ToolPullService toolPullService;
	@Autowired
	private ExtraBoxNumSettingService extraBoxNumSettingService;
	@Autowired
	private MatReturnBackService matReturnBackService;
	@Autowired
	private MatUseBillService matUseBillService;
	@Autowired
	private MatUseRecordService matUseRecordService;
	@Autowired
	private WarehouseFeedService warehouseFeedService;
	@Autowired
	private ExternalService externalService;

	/**
	 * ???????????????ID????????????????????????
	 * 
	 * @param cabinetId ?????????ID
	 */
	@RequestMapping("/stockEmail")
	public void stockEmail(@RequestParam("cabinetId") String cabinetId) {
		this.stockEmailUtil.stockEmail(cabinetId);
	}

	/**
	 * ???????????????ID????????????????????????
	 * 
	 */
	@RequestMapping("/borrowPost")
	public void borrowPost(@RequestParam(value = "recordId") String recordId) {
		this.externalService.borrowPost(recordId);
	}

	/**
	 * @description ????????????????????????????????????
	 * @param serialNo ?????????
	 * @return
	 */
	@RequestMapping("/getCabinetSetupBySerialNo")
	public Map<String, Map<String, String>> getCabinetSetupBySerialNo(@RequestParam("serialNo") String serialNo) {
		return this.cabinetRpcService.getCabinetSetupBySerialNo(serialNo);
	}

	@RequestMapping("/getVer")
	public String getVer() throws Exception {
		return this.toolProperties.getVer();
	}

	/**
	 * @description ????????????Id????????????????????????
	 * @param cabinetId ??????Id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getCardList/{cabinetId}")
	public List<Card> getCardList(@PathVariable(value = "cabinetId") String cabinetId) throws Exception {
		return this.cabinetRpcService.getCardList(cabinetId);
	}

	@RequestMapping("/getLatticeValueMap/{cabinetId}")
	public Map<String, Integer> getLatticeValueMap(@PathVariable String cabinetId) throws Exception {
		return this.cabinetRpcService.getLatticeValueMap(cabinetId);
	}

	/**
	 * @description ?????????????????????????????????????????????????????????
	 * 
	 * @param equId     ?????? Id
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getBorrowTypeList/{cabinetId}/{accountId}")
	public List<RpcBorrowType> getBorrowTypeList(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId) {
		return this.borrowPopedomService.getBorrowTypeList(cabinetId, accountId);
	}

	/**
	 * @description ?????????????????????
	 * 
	 * @param cabinetId ?????????Id
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getCategoryTree/{cabinetId}/{accountId}/{type}")
	public List<TypeNode> getCategoryTree(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId, @PathVariable(value = "type") String type) {
		return this.borrowPopedomService.getCategoryTree(cabinetId, accountId, type);
	}

	/**
	 * @description ??????????????????????????????
	 * @param cabinetId ?????????Id
	 * @param equId     ??????Id
	 * @return
	 */
	@RequestMapping("/getMatListByEqu/{cabinetId}/{accountId}/{equId}")
	public List<MatDetail> getMatListByEqu(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId, @PathVariable(value = "equId") String equId) {
		return this.cabinetRpcService.getMatListByEqu(cabinetId, accountId, equId);
	}

	/**
	 * @description ????????????
	 * @param cabinetId ?????????Id
	 * @param cartList  ?????????<cartId>??????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/stockCkeck")
	public OpTip stockCkeck(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) List<CartInfo> cartList) {
		return this.cabinetRpcService.stockCkeck(cabinetId, cartList);
	}

	/**
	 * @description ???????????????????????????
	 * @param cabinetId ?????????Id
	 * @param cartList  ?????????<cartId>??????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/sendCartToServer")
	public List<ExtraCabinet> sendCartToServer(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) List<CartInfo> cartList,
			@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId) {
		return this.cabinetRpcService.sendCartToServer(cabinetId, cartList, shareSwitch, accountId);
	}

	/**
	 * @description ???????????????????????????
	 * @param cabinetId ?????????Id
	 * @param cartList  ?????????<cartId>??????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getBillInfo")
	public BillInfo getBillInfo(@RequestParam(value = "billId") String billId) {
		return this.matUseBillService.getBillInfo(billId);
	}

	/**
	 * @description ????????????
	 * @param borrowGrid     ??????????????????
	 * @param accountId      ??????Id
	 * @param receiveType    ????????????
	 * @param receiveInfo    ????????????
	 * @param isSyncStockNum ????????????????????????
	 */
	@RequestMapping("/borrowByGrid")
	public OpTip borrowByGrid(@RequestBody(required = true) BorrowGrid borrowGrid,
			@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "receiveType") String receiveType,
			@RequestParam(value = "receiveInfo") String receiveInfo) {
		return this.cabinetRpcService.borrowByGrid(borrowGrid, accountId, receiveType, receiveInfo);
	}

	/**
	 * @description ?????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getUnReturnTypeList")
	public List<FeignCodeName> getUnReturnTypeList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "cabinetId") String cabinetId) {
		return this.cabinetRpcService.getUnReturnTypeList(accountId, cabinetId);
	}

	/**
	 * @description ?????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getUnReturnList")
	public Map<String, List<SubMatDetail>> getUnReturnList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo) {
		return this.cabinetRpcService.getUnReturnList(accountId, serialNo);
	}

	/**
	 * @description ????????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getBackNormalList")
	public MyPage<SubMatDetail> getBackNormalList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo, @RequestParam("pageNo") int pageNo) {
		return this.cabinetRpcService.getBackNormalList(accountId, serialNo, pageNo);
	}

	/**
	 * @description ????????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getBackBatchList")
	public List<SubMatDetail> getBackBatchList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo) {
		return this.cabinetRpcService.getBackBatchList(accountId, serialNo);
	}

	/**
	 * @description ??????
	 * @param matUseBillId ??????????????????Id
	 * @param borrowNum    ????????????
	 * @param accountId    ??????Id
	 */
	@RequestMapping("/tempstor/{matUseBillId}/{borrowNum}/{accountId}")
	public OpTip tempStor(@PathVariable("matUseBillId") String matUseBillId, @PathVariable("borrowNum") int borrowNum,
			@PathVariable("accountId") String accountId) {
		OpTip tip = new OpTip("????????????");
		try {
			this.cabinetRpcService.tempStor(matUseBillId, borrowNum, accountId);
		} catch (Exception e) {
			tip.setCode(0);
			tip.setMessage("????????????");
		}
		return tip;
	}

	/**
	 * @description ?????????????????????
	 * @return
	 */
	@RequestMapping("/getReBackTypeMap")
	public Map<String, List<ReBackType>> getReBackTypeMap() {
		return this.cabinetRpcService.getReBackTypeMap();
	}

	/**
	 * @description ??????????????????
	 * @param matUseBillIds ????????????Id
	 * @param accountId     ?????????
	 * @param isGetNewOne   ????????????
	 * @param returnInfo    ????????????
	 * @param returnInfo    ????????????
	 * @param realLife      ????????????
	 * @param serialNum     ???????????????
	 * @return
	 *         <p>
	 *         ?????????????????????????????????: ??????????????????Id;???????????????Id;??????????????????;??????Id;????????????
	 *         matUseBillId;returnTypeId;accountId;isGetNewOne;matInfoId;barcode;returnInfo
	 *         </p>
	 */
	@RequestMapping("/getPrintInfo")
	public ReturnBackPrintInfo getPrintInfo(@RequestParam("matBillId") String matBillId,
			@RequestParam("accountId") String accountId, @RequestParam("isGetNewOne") String isGetNewOne,
			@RequestParam("matInfoId") String matInfoId, @RequestParam("backNum") int backNum,
			@RequestParam("cabinetId") String cabinetId, @RequestParam("barcode") String barcode,
			@RequestParam("returnInfo") String returnInfo, @RequestParam("backWay") String backWay,
			@RequestParam("realLife") String realLife, @RequestParam("serialNum") String serialNum) {
		ReturnBackPrintInfo infoList = this.cabinetRpcService.getPrintInfo(matBillId, accountId, isGetNewOne, matInfoId,
				backNum, cabinetId, barcode, returnInfo, backWay, realLife, serialNum);
		return infoList;
	}

	// ??????????????????????????????
	@RequestMapping("/findBybarcode")
	public OpTip findBybarcode(@RequestParam("barCode") String barCode) {
		OpTip tip = null;
		try {
			tip = this.cabinetRpcService.findBybarcode(barCode);
		} catch (Exception e) {
			tip.setCode(0);
			tip.setMessage("????????????");
		}
		return tip;
	}

	/**
	 * @description ????????????
	 * @param returnBackNo
	 * @return
	 */
	@RequestMapping("/returnBackSingle")
	public OpTip returnBackSingle(@RequestParam("returnBackNo") String returnBackNo) {
		OpTip tip = new OpTip(200, "????????????");
		try {
			this.cabinetRpcService.returnBackSingle(returnBackNo);
		} catch (Exception e) {
			tip = new OpTip(202, "????????????");
		}
		return tip;
	}

	/**
	 * @description ???????????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getSubCabinetRpcList")
	public List<SubMatDetail> getSubCabinetRpcList(@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId) {
		return this.cabinetRpcService.getSubCabinetRpcList(shareSwitch, accountId);
	}

	/**
	 * @description ????????????
	 * @param opType    ??????????????????
	 * @param subBoxId  ????????????Id
	 * @param matInfoId ????????????Id
	 * @param num       ????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/opSubBox")
	public OpTip opSubBox(@RequestParam("subBoxId") String subBoxId, @RequestParam("matInfoId") String matInfoId,
			@RequestParam("num") Integer num, @RequestParam("accountId") String accountId) {
		OpTip tip = new OpTip("????????????");
		try {
			this.cabinetRpcService.opSubBox(subBoxId, matInfoId, num, accountId);
		} catch (Exception e) {
			tip.setCode(0);
			tip.setMessage("????????????");
		}
		return tip;
	}

	/**
	 * @description ??????????????????
	 * @return
	 */
	@RequestMapping("/delDailyLimit")
	public void delDailyLimit(@RequestParam("accountId") String accountId) {
		this.dailyLimitService.delDailyLimit(accountId);
	}

	/**
	 * @description ?????????????????????
	 * @return
	 */
	@RequestMapping("/getThrYearPriceSumGroupByMonth")
	public Map<String, List<MonthCost>> getThrYearPriceSumGroupByMonth() {
		return this.statisticsService.getThrYearPriceSumGroupByMonth();
	}

	/**
	 * @description ???????????????????????????
	 * @return
	 */
	@RequestMapping("/getCurMonthGroupByMat")
	public Map<String, Integer> getCurMonthGroupByMat() {
		return this.statisticsService.getCurMonthGroupByMat();
	}

	/**
	 * @description ????????????????????????
	 * @return
	 */
	@RequestMapping("/getThrYCntGroupByMatType")
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType() {
		return this.statisticsService.getThrYCntGroupByMatType();
	}

	/**
	 * @description ?????????????????????????????????
	 * @return
	 */
	@RequestMapping("/getBorrowNumGroupByDept")
	public Map<String, Integer> getBorrowNumGroupByDept() {
		return this.statisticsService.getBorrowNumGroupByDept();
	}

	/**
	 * @description ????????????ID?????????????????????????????????
	 * @param cabinetID ??????ID
	 * @return Map ??? key?????????,value???IP:Port????????????????????????Map?????? "1" : "192.168.1.101:502" "2"
	 *         : "192.168.1.102:502" "3" : "192.168.1.103:502" ... "9" :
	 *         "192.168.1.109:502"
	 */
	@RequestMapping("/getMotorBoardIPMap")
	public Map<String, String> getMotorBoardIPMap(@RequestParam("cabinetID") String cabinetID) {
		return this.cabinetRpcService.getMotorBoardIPMap(cabinetID);
	}

	/**
	 * @description ????????????????????????ID?????????ID???????????????????????????
	 * @param appointmentID ????????????ID
	 * @return true-???????????? false-??????????????????
	 */
	@RequestMapping("/isAppointmentCompletedByID")
	public boolean isAppointmentCompletedByID(@RequestParam("appointmentID") String appointmentID) {
		return this.cabinetRpcService.isAppointmentCompletedByID(appointmentID);
	}

	/**
	 * @description ????????????????????????????????????
	 * @param appointmentID ????????????ID
	 * @param isCompleted   true - ??????; false - ?????????;
	 * @return ????????????,?????? ??? ??????
	 */
	@RequestMapping("/setAppointmentCompletedByID")
	public OpTip setAppointmentCompletedByID(@RequestParam("appointmentID") String appointmentID,
			@RequestParam("isCompleted") boolean isCompleted) {
		return this.cabinetRpcService.setAppointmentCompletedByID(appointmentID, isCompleted);
	}

	/**
	 * @description ??????????????????
	 * @param accountId ??????Id
	 * @param popedoms  ????????????
	 * @return
	 */
	@RequestMapping("/bindBorrowPopedoms")
	public Boolean bindBorrowPopedoms(@RequestParam("accountId") String accountId,
			@RequestParam("popedoms") String popedoms) {
		return this.borrowPopedomService.bindBorrowPopedoms(accountId, popedoms);
	}

	/**
	 * @description ??????????????????????????????????????????
	 * @param accountId ?????????Id
	 * @return
	 */
	@RequestMapping("/getSubBoxMapGroupByBoardNo")
	public Map<String, Map<String, List<LockPara>>> getSubBoxMapGroupByBoardNo(
			@RequestParam("cabinetId") String cabinetId) {
		return this.cabinetRpcService.getSubBoxMapGroupByBoardNo(cabinetId);
	}

	/**
	 * @description ??????????????????????????????
	 * @param cabinetId        ??????Id
	 * @param feedingAccountId ?????????Id
	 * @return
	 */
	@RequestMapping("/getWaitFinishFeedingList")
	public List<ExtractionMethod> getFeedingList(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("feedingAccountId") String feedingAccountId) {
		return this.feedingListService.getWaitFinishFeedingList(cabinetId, feedingAccountId);
	}

	/**
	 * @description ??????????????????Id????????????????????????
	 * @param feedingListId ????????????Id
	 * @return
	 */
	@RequestMapping("/getFeedingDetailListByListId")
	public List<Card> getFeedingDetailListByListId(@RequestParam("feedingListId") String feedingListId) {
		return this.feedingDetailService.getFeedingDetailListByListId(feedingListId);
	}

	/**
	 * @description ??????????????????
	 * @param feedingListId    ????????????Id
	 * @param feedingAccountId ?????????Id
	 * @return
	 */
	@RequestMapping("/finishFeedingList")
	public OpTip finishFeedingList(@RequestParam("feedingListId") String feedingListId,
			@RequestParam("feedingAccountId") String feedingAccountId) {
		OpTip tip = new OpTip("????????????");
		try {
			FeedingList feedingList = this.feedingListService.get(feedingListId);
			feedingList.setFeedingAccountId(feedingAccountId);
			feedingList.setFeedingTime(new Date());
			this.feedingListService.finishFeedingList(feedingList);
		} catch (Exception e) {
			tip = new OpTip(201, "????????????");
		}
		return tip;
	}

	/**
	 * @description ????????????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/initBorrowPopedomTree")
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("accountId") String accountId) {
		return this.borrowPopedomService.initBorrowPopedomTree(accountId);
	}

	/**
	 * @description ???????????????????????????(????????????)
	 * @param subboxId  ???????????????Id
	 * @param matInfoId ??????Id
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/outsubcabinet")
	public SubMatDetail outsubcabinet(@RequestParam("subboxId") String subboxId,
			@RequestParam("matInfoId") String matInfoId, @RequestParam("accountId") String accountId) {
		SubMatDetail detail = this.cabinetRpcService.outsubcabinet(subboxId, matInfoId, accountId);
		return detail;
	}

	/**
	 * @description ?????????????????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getUnSubMatInfoList")
	public List<SubMatDetail> getUnSubMatInfoList(@RequestParam("accountId") String accountId) {
		return this.cabinetRpcService.getUnSubMatInfoList(accountId);
	}

	/**
	 * @description ????????????Id????????????????????????
	 * @param matId     ??????Id
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getMatDetail")
	public SubMatDetail getMatDetail(@RequestParam("matId") String matId, @RequestParam("accountId") String accountId) {
		return this.cabinetRpcService.getMatDetail(matId, accountId);
	}

	/**
	 * @description ????????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/checkNewSubCabinet")
	public OpTip checkNewSubCabinet(@RequestParam("matId") String matId, @RequestParam("accountId") String accountId) {
		return this.subBoxAccountRefService.checkNewSubCabinet(matId, accountId);
	}

	/**
	 * @description ????????????
	 * @param matId     ??????Id
	 * @param matNum    ????????????
	 * @param storType  ????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/tmpNewStor")
	public ElecLock tmpNewStor(@RequestParam("matId") String matId, @RequestParam("matNum") int matNum,
			@RequestParam("storType") String storType, @RequestParam("accountId") String accountId) {
		return this.subCabinetService.tmpNewStor(matId, matNum, storType, accountId);
	}

	/**
	 * @description
	 * @param cabinetId  ??????Id
	 * @param curReserve ????????????
	 * @param name       ????????????
	 * @return
	 */
	@RequestMapping("/getPro")
	public Lattice getPro(@RequestParam("matId") String matId, @RequestParam("latticeId") String latticeId) {
		Lattice lattice = this.matEquInfoService.getPro(matId, latticeId);
		return lattice;
	}

	/**
	 * @description ???????????????
	 * @param
	 * @return
	 */
	@RequestMapping("/addCart")
	public void addCart(@RequestParam("matId") String matId, @RequestParam("type") String type,
			@RequestParam("num") int num, @RequestParam("accountId") String accountId,
			@RequestParam("receiveType") String receiveType, @RequestParam("receiveInfo") String receiveInfo,
			@RequestParam("cabinetId") String cabinetId) {
		this.cartService.addCart(matId, type, num, accountId, receiveType, receiveInfo, cabinetId);
	}

	/**
	 * @description ???????????????
	 * @param
	 * @return
	 */
	@RequestMapping("/selectCart")
	public List<CartInfo> selectCart(@RequestParam("accountId") String accountId,
			@RequestParam("cabinetId") String cabinetId) {
		return this.cartService.selectCart(accountId, cabinetId);
	}

	/**
	 * @description ???????????????
	 * @param
	 * @return
	 */
	@RequestMapping("/updateCart")
	public void updateCart(@RequestParam("cartId") String cartId, @RequestParam("num") int num) {
		this.cartService.updateCart(cartId, num);
	}

	/**
	 * @description ???????????????
	 * @param
	 * @return
	 */
	@RequestMapping("/delCart")
	public void delCart(@RequestParam("cartId") String cartId) {
		this.cartService.delCart(cartId);
	}

	/**
	 * @description ????????????????????????
	 * @param serialNo ????????????
	 * @return
	 */
	@RequestMapping("/getCabinetList")
	public List<ExtraCabinet> getCabinetList(@RequestParam("serialNo") String serialNo) {
		return this.equSettingService.getCabinetList(serialNo);
	}

	/**
	 * @description ??????????????????????????????
	 * @param accountId ??????Id
	 */
	@RequestMapping("/getDailyLimit")
	public OpTip getDailyLimit(@RequestParam("accountId") String accountId, @RequestParam("matId") String matId,
			@RequestParam("borrowNum") Integer borrowNum, @RequestBody UserInfo userInfo) {
		OpTip tip = this.dailyLimitService.getDailyLimit(accountId, matId, borrowNum, userInfo);
		return tip;
	}

	/**
	 * @description ?????????????????????????????????
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/getDailyLimitByCart")
	public OpTip getDailyLimitByCart(@RequestParam("shareSwitch") String shareSwitch,
			@RequestParam("accountId") String accountId, @RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime, @RequestParam("limitSumNum") Integer limitSumNum,
			@RequestParam("notReturnLimitNum") Integer notReturnLimitNum,
			@RequestBody(required = true) Map<String, CartInfo> limitMap) {
		OpTip tip = this.dailyLimitService.getDailyLimitByCart(shareSwitch, accountId, limitMap, startTime, endTime,
				limitSumNum, notReturnLimitNum);
		return tip;
	}

	/**
	 * @description ??????
	 * @param cabinetId
	 * @return
	 */
	@RequestMapping("/getCabinetId")
	public String getCabinetId(@RequestParam("cabinetId") String cabinetId) {
		return this.equSettingService.getEquSettingListByEquInfoId(cabinetId);
	}

	/**
	 * @description ??????
	 * @param cabinetId
	 * @return
	 */
	@RequestMapping("/getDoor")
	public EnumDoor getDoor(@RequestParam("ip") String ip) {
		return this.equDetailService.getDoor(ip);
	}

	/*
	 * @description ????????????Id??????????????????
	 * 
	 * @param accountId ??????Id
	 * 
	 * @return
	 */
	@RequestMapping("/getUserInfoByAccountId")
	public UserInfo getUserInfoByAccountId(@RequestParam("accountId") String accountId) {
		UserInfo userInfo = this.adminService.getUserInfo(accountId);
		return userInfo;
	}

	/**
	 * @description ????????????Id??????????????????
	 * @param cabinetId ??????Id
	 * @param query     ??????????????????
	 * @param search    ??????????????????
	 * @return
	 */
	@RequestMapping("/getFeedDetailStaList")
	public List<FeedDetailSta> getFeedDetailStaList(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("query") String query, @RequestParam("search") String search) {
		return this.equDetailStaService.getFeedDetailStaList(cabinetId, query, search);
	}

	/**
	 * @description ????????????
	 * @param feed      ????????????
	 * @param cabinetId ????????????Id
	 * @param accountId ??????Id
	 * @return
	 */
	@RequestMapping("/feed")
	public Boolean feed(@RequestParam("feed") String feed, @RequestParam("cabinetId") String cabinetId,
			@RequestParam("accountId") String accountId) {
		try {
			this.feedingListService.feed(feed, cabinetId, accountId, FeedType.HAND);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @description ????????????????????????
	 * @param cabinetId ????????????Id
	 * @param matId     ??????Id
	 * @return
	 */
	@RequestMapping("/getMatRemainNumByMatId")
	public MatDetail getMatRemainNumByMatId(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("matId") String matId) {
		return this.cabinetRpcService.getMatRemainNumByMatId(cabinetId, matId);
	}

	/**
	 * @description ??????????????????
	 * @param staId ???????????????
	 * @return
	 */
	@RequestMapping("/equStaProblem")
	public Boolean equStaProblem(@RequestParam("staId") String staId) {
		return this.equDetailStaService.equStaProblem(staId);
	}

	/**
	 * @description ??????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/resetProblem")
	public Boolean resetProblem(@RequestParam("cabinetId") String cabinetId) {
		return this.equDetailStaService.resetProblem(cabinetId);
	}

	/**
	 * 
	 * @description ????????????-????????????
	 */
	@RequestMapping("/editCount")
	public List<NoticeMgrInfo> editCount(@RequestParam("cabinetId") String cabinetId) {
		return this.noticeMgrService.editCount(cabinetId);
	}

	/**
	 * @description ????????????-????????????
	 */
	@RequestMapping("/getNoticeMgr")
	public List<NoticeMgrInfo> getNoticeMgr(@RequestParam("cabinetId") String cabinetId) {
		return this.noticeMgrService.getNoticeMgr(cabinetId);
	}

	/**
	 * @description ????????????-????????????
	 */
	@RequestMapping("/resetNoticeMgr")
	public NoticeMgrInfo resetNoticeMgr(@RequestParam("cabinetId") String cabinetId, @RequestParam("num") int num,
			@RequestParam("noticeType") String noticeType) {
		return this.noticeMgrService.resetNoticeMgr(cabinetId, num, noticeType);
	}

	/**
	 * @description ??????????????????
	 */
	@RequestMapping("/curNumCheck")
	public OpTip curNumCheck(@RequestParam("staId") String staId, @RequestParam("borrowNum") int borrowNum,
			@RequestParam("cabinetId") String cabinetId) {
		return this.equDetailStaService.curNumCheck(staId, borrowNum, cabinetId);
	}

	/**
	 * @description ??????????????????
	 * @param notice ??????????????????
	 * @return
	 */
	@RequestMapping("/notice")
	public OpTip notice(@RequestBody(required = true) BorrowNotice notice) {
		try {
			this.toolPullService.notice(notice);
		} catch (DoSthException e) {
			return new OpTip(e.getCode(), e.getMessage());
		}
		return new OpTip(200, notice.toString());
	}

	/**
	 * @description ?????????????????????
	 * @param accountId   ??????id
	 * @param extraBoxNum ???????????????
	 * @return
	 */
	@RequestMapping("/updateExtraBoxNum")
	public void updateExtraBoxNum(@RequestParam("accountId") String accountId,
			@RequestParam("extraBoxNum") String extraBoxNum) {
		this.extraBoxNumSettingService.updateExtraBoxNum(accountId, extraBoxNum);
	}

	/**
	 * @description ?????????????????????
	 * @param accountId ??????id
	 * @return
	 */
	@RequestMapping("/getExtraBoxNum")
	public ExtraBoxNum getExtraBoxNum(@RequestParam("accountId") String accountId) {
		return this.extraBoxNumSettingService.getExtraBoxNum(accountId);
	}

	/**
	 * @description ?????????????????????
	 * @param accountId ??????id
	 * @return
	 */
	@RequestMapping("/delExtraBoxNum")
	public void delExtraBoxNum(@RequestParam("accountId") String accountId) {
		this.extraBoxNumSettingService.delExtraBoxNum(accountId);
	}

	/**
	 * @description ???????????????????????????
	 * @param accountId ??????id
	 * @return
	 */
	@RequestMapping("/delNoticeMgr")
	public void delNoticeMgr(@RequestParam("accountId") String accountId) {
		this.noticeMgrService.delNoticeMgr(accountId);
	}

	/**
	 * @description ????????????Id????????????????????????
	 * @param mainCabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/getCabinetTreeList")
	public List<ExtraCabinet> getCabinetTreeList(@RequestParam("mainCabinetId") String mainCabinetId) {
		return this.equSettingService.getCabinetTreeList(mainCabinetId);
	}

	/**
	 * @description ?????????????????????
	 * @param billId ??????Id
	 * @return
	 */
	@RequestMapping("/isBackCheck")
	public Boolean isBackCheck(@RequestParam("returnBackNo") String returnBackNo) {
		return this.matReturnBackService.isBackCheck(returnBackNo);
	}

	/**
	 * @description ????????????????????????
	 * @param billId ??????Id
	 * @return
	 */
	@RequestMapping("/getReturnInfo")
	public List<ReturnBackPrintInfo> getReturnInfo(@RequestParam("returnBackNo") String returnBackNo) {
		return this.matReturnBackService.getReturnInfo(returnBackNo);
	}

	/**
	 * @description ????????????
	 * @param mainCabinetId ??????Id
	 */
	@RequestMapping("/getStockTip")
	public List<StockTip> getStockTip(@RequestParam("mainCabinetId") String mainCabinetId) {
		return this.equDetailStaService.getStockTip(mainCabinetId);
	}

	/**
	 * @description ????????????Id??????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/getWareHouseAliasByCabinetId")
	public String getWareHouseAliasByCabinetId(@RequestParam("cabinetId") String cabinetId) {
		return this.equSettingService.get(cabinetId).getWareHouseAlias();
	}

	/**
	 * @description ????????????Id??????????????????
	 * @param cabinetId ??????Id
	 * @return
	 */
	@RequestMapping("/getMatNumMapByCabinetId")
	public Map<String, Integer> getMatNumMapByCabinetId(@RequestParam("cabinetId") String cabinetId) {
		return this.equDetailStaService.getMatNumMapByCabinetId(cabinetId);
	}

	/**
	 * @description ??????????????????????????????
	 * @param barCode ????????????
	 * @return
	 */
	@RequestMapping("/getMatDetailByBarCode")
	public MatDetail getMatDetailByBarCode(@RequestParam("barCode") String barCode) {
		MatEquInfo info = this.matEquInfoService.getMatDetailByBarCode(barCode);
		MatDetail detail = new MatDetail();
		if (info != null) {
			detail.setMatId(info.getId());
			detail.setMatName(info.getMatEquName());
			detail.setBarCode(info.getBarCode());
			detail.setSpec(info.getSpec());
			detail.setPackNum(info.getNum());
			if (info.getBorrowType() != null) {
				detail.setBorrowTypeCode(info.getBorrowType().name());
			}
			if (info.getIcon() != null && !"".equals(info.getIcon())) {
				detail.setPic(this.toolProperties.getImgUrlPath() + info.getIcon());
			}
			if (info.getManufacturer() != null) {
//                detail.setManufacturerId(info.getManufacturerId());
//                detail.setManufacturerName(info.getManufacturer().getManufacturerName());
			}
		}
		return detail;
	}

	/**
	 * @description ????????????
	 * @param cabinetId  ?????????Id
	 * @param voucherMap ???????????????
	 * @return
	 */
	@RequestMapping("/stockCheck")
	public OpTip stockCheck(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) Map<String, List<CartInfo>> voucherMap) {
		return this.cabinetRpcService.stockCheck(cabinetId, voucherMap);
	}

	/**
	 * @description ???????????????????????????
	 * @param cabinetId  ?????????Id
	 * @param voucherMap ???????????????
	 * @param accountId  ??????Id
	 * @return
	 */
	@RequestMapping("/sendApplyVoucherToServer")
	public List<ExtraCabinet> sendApplyVoucherToServer(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) Map<String, List<CartInfo>> voucherMap,
			@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId) {
		return this.cabinetRpcService.sendApplyVoucherToServer(cabinetId, voucherMap, shareSwitch, accountId);
	}

	/**
	 * @description ?????????????????????
	 * @param applyVoucherResult ???????????????|????????????Id1,????????????Id2
	 * @return
	 */
	@RequestMapping("/getApplyVoucherResult")
	public List<ApplyVoucher> getApplyVoucherResult(@RequestParam("applyVoucherResult") String applyVoucherResult) {
		return this.matUseRecordService.getApplyVoucherResult(applyVoucherResult);
	}

	/**
	 * @description ????????????????????????(????????????????????????)
	 * @param feedingListId ????????????Id
	 * @return
	 */
	@RequestMapping("/syncFeedingList")
	public FeignFeedingList syncFeedingList(@RequestParam("feedingListId") String feedingListId) {
		return this.feedingListService.getFeeding(feedingListId);
	}

	/**
	 * @description ??????????????????????????????
	 * @param feedingListId ????????????Id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/warehouseFeedList")
	public List<FeignWarehouseFeed> warehouseFeedList(@RequestParam("feedingListId") String feedingListId)
			throws IOException {
		return this.warehouseFeedService.getWarehouseFeedList(feedingListId);
	}

	/**
	 * @description ???????????????????????????
	 * @param feedingListId ????????????Id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/warehouseSave")
	public void warehouseSave(@RequestParam("feedNo") String feedNo, @RequestParam("message") String message,
			@RequestParam("result") Boolean result) throws IOException {
		this.warehouseFeedService.warehouseSave(feedNo, message, result);
	}

	/**
	 * @description ???????????????????????????
	 * @param notice ?????????????????????
	 * @return
	 */
	@RequestMapping("/noticeTrolDrawerNotice")
	public OpTip notice(@RequestBody(required = true) TrolDrawerNotice notice) {
		try {
			this.toolPullService.noticeTrolDrawerNotice(notice);
		} catch (DoSthException e) {
			return new OpTip(e.getCode(), e.getMessage());
		}
		return new OpTip(200, notice.toString());
	}
}