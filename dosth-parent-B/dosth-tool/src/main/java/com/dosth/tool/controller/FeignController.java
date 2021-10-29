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
 * @description 柜子远程接口定义
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
	 * 根据刀具柜ID发送库存预警邮件
	 * 
	 * @param cabinetId 刀具柜ID
	 */
	@RequestMapping("/stockEmail")
	public void stockEmail(@RequestParam("cabinetId") String cabinetId) {
		this.stockEmailUtil.stockEmail(cabinetId);
	}

	/**
	 * 根据刀具柜ID发送库存预警邮件
	 * 
	 */
	@RequestMapping("/borrowPost")
	public void borrowPost(@RequestParam(value = "recordId") String recordId) {
		this.externalService.borrowPost(recordId);
	}

	/**
	 * @description 根据序列号获取刀具柜信息
	 * @param serialNo 序列号
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
	 * @description 根据柜子Id获取所有货位集合
	 * @param cabinetId 柜子Id
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
	 * @description 根据设备和用户领取权限获取领取类型集合
	 * 
	 * @param equId     设备 Id
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/getBorrowTypeList/{cabinetId}/{accountId}")
	public List<RpcBorrowType> getBorrowTypeList(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId) {
		return this.borrowPopedomService.getBorrowTypeList(cabinetId, accountId);
	}

	/**
	 * @description 获取物料类型树
	 * 
	 * @param cabinetId 刀具柜Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getCategoryTree/{cabinetId}/{accountId}/{type}")
	public List<TypeNode> getCategoryTree(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId, @PathVariable(value = "type") String type) {
		return this.borrowPopedomService.getCategoryTree(cabinetId, accountId, type);
	}

	/**
	 * @description 根据种别获取物料关联
	 * @param cabinetId 刀具柜Id
	 * @param equId     种别Id
	 * @return
	 */
	@RequestMapping("/getMatListByEqu/{cabinetId}/{accountId}/{equId}")
	public List<MatDetail> getMatListByEqu(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId, @PathVariable(value = "equId") String equId) {
		return this.cabinetRpcService.getMatListByEqu(cabinetId, accountId, equId);
	}

	/**
	 * @description 库存校验
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/stockCkeck")
	public OpTip stockCkeck(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) List<CartInfo> cartList) {
		return this.cabinetRpcService.stockCkeck(cabinetId, cartList);
	}

	/**
	 * @description 发送购物车到服务端
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
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
	 * @description 发送购物车到服务端
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getBillInfo")
	public BillInfo getBillInfo(@RequestParam(value = "billId") String billId) {
		return this.matUseBillService.getBillInfo(billId);
	}

	/**
	 * @description 栅栏借出
	 * @param borrowGrid     栅栏借出信息
	 * @param accountId      帐户Id
	 * @param receiveType    借出类型
	 * @param receiveInfo    借出信息
	 * @param isSyncStockNum 是否同步库存数量
	 */
	@RequestMapping("/borrowByGrid")
	public OpTip borrowByGrid(@RequestBody(required = true) BorrowGrid borrowGrid,
			@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "receiveType") String receiveType,
			@RequestParam(value = "receiveInfo") String receiveInfo) {
		return this.cabinetRpcService.borrowByGrid(borrowGrid, accountId, receiveType, receiveInfo);
	}

	/**
	 * @description 获取未归还类型
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getUnReturnTypeList")
	public List<FeignCodeName> getUnReturnTypeList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "cabinetId") String cabinetId) {
		return this.cabinetRpcService.getUnReturnTypeList(accountId, cabinetId);
	}

	/**
	 * @description 获取未归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getUnReturnList")
	public Map<String, List<SubMatDetail>> getUnReturnList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo) {
		return this.cabinetRpcService.getUnReturnList(accountId, serialNo);
	}

	/**
	 * @description 获取常规归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getBackNormalList")
	public MyPage<SubMatDetail> getBackNormalList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo, @RequestParam("pageNo") int pageNo) {
		return this.cabinetRpcService.getBackNormalList(accountId, serialNo, pageNo);
	}

	/**
	 * @description 获取批量归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getBackBatchList")
	public List<SubMatDetail> getBackBatchList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo) {
		return this.cabinetRpcService.getBackBatchList(accountId, serialNo);
	}

	/**
	 * @description 暂存
	 * @param matUseBillId 借出物料流水Id
	 * @param borrowNum    暂存数量
	 * @param accountId    帐户Id
	 */
	@RequestMapping("/tempstor/{matUseBillId}/{borrowNum}/{accountId}")
	public OpTip tempStor(@PathVariable("matUseBillId") String matUseBillId, @PathVariable("borrowNum") int borrowNum,
			@PathVariable("accountId") String accountId) {
		OpTip tip = new OpTip("暂存成功");
		try {
			this.cabinetRpcService.tempStor(matUseBillId, borrowNum, accountId);
		} catch (Exception e) {
			tip.setCode(0);
			tip.setMessage("暂存失败");
		}
		return tip;
	}

	/**
	 * @description 获取归还类型组
	 * @return
	 */
	@RequestMapping("/getReBackTypeMap")
	public Map<String, List<ReBackType>> getReBackTypeMap() {
		return this.cabinetRpcService.getReBackTypeMap();
	}

	/**
	 * @description 获取打印信息
	 * @param matUseBillIds 物料领取Id
	 * @param accountId     归还人
	 * @param isGetNewOne   以旧换新
	 * @param returnInfo    归还信息
	 * @param returnInfo    归还信息
	 * @param realLife      制造产量
	 * @param serialNum     产品流水号
	 * @return
	 *         <p>
	 *         二维码数据格式内容如下: 物料领用序号Id;归还人帐号Id;以旧换新标识;物料Id;归还信息
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

	// 查询条形码是否在使用
	@RequestMapping("/findBybarcode")
	public OpTip findBybarcode(@RequestParam("barCode") String barCode) {
		OpTip tip = null;
		try {
			tip = this.cabinetRpcService.findBybarcode(barCode);
		} catch (Exception e) {
			tip.setCode(0);
			tip.setMessage("归还失败");
		}
		return tip;
	}

	/**
	 * @description 单个归还
	 * @param returnBackNo
	 * @return
	 */
	@RequestMapping("/returnBackSingle")
	public OpTip returnBackSingle(@RequestParam("returnBackNo") String returnBackNo) {
		OpTip tip = new OpTip(200, "归还成功");
		try {
			this.cabinetRpcService.returnBackSingle(returnBackNo);
		} catch (Exception e) {
			tip = new OpTip(202, "归还失败");
		}
		return tip;
	}

	/**
	 * @description 获取我的暂存柜信息
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getSubCabinetRpcList")
	public List<SubMatDetail> getSubCabinetRpcList(@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId) {
		return this.cabinetRpcService.getSubCabinetRpcList(shareSwitch, accountId);
	}

	/**
	 * @description 操作副柜
	 * @param opType    副柜操作类型
	 * @param subBoxId  副柜盒子Id
	 * @param matInfoId 物料信息Id
	 * @param num       操作数量
	 * @param accountId 帐号Id
	 * @return
	 */
	@RequestMapping("/opSubBox")
	public OpTip opSubBox(@RequestParam("subBoxId") String subBoxId, @RequestParam("matInfoId") String matInfoId,
			@RequestParam("num") Integer num, @RequestParam("accountId") String accountId) {
		OpTip tip = new OpTip("归还成功");
		try {
			this.cabinetRpcService.opSubBox(subBoxId, matInfoId, num, accountId);
		} catch (Exception e) {
			tip.setCode(0);
			tip.setMessage("归还失败");
		}
		return tip;
	}

	/**
	 * @description 删除每日限额
	 * @return
	 */
	@RequestMapping("/delDailyLimit")
	public void delDailyLimit(@RequestParam("accountId") String accountId) {
		this.dailyLimitService.delDailyLimit(accountId);
	}

	/**
	 * @description 近三年成本趋势
	 * @return
	 */
	@RequestMapping("/getThrYearPriceSumGroupByMonth")
	public Map<String, List<MonthCost>> getThrYearPriceSumGroupByMonth() {
		return this.statisticsService.getThrYearPriceSumGroupByMonth();
	}

	/**
	 * @description 当前月物料数量分布
	 * @return
	 */
	@RequestMapping("/getCurMonthGroupByMat")
	public Map<String, Integer> getCurMonthGroupByMat() {
		return this.statisticsService.getCurMonthGroupByMat();
	}

	/**
	 * @description 三年物料类型分布
	 * @return
	 */
	@RequestMapping("/getThrYCntGroupByMatType")
	public Map<String, Map<String, Integer>> getThrYCntGroupByMatType() {
		return this.statisticsService.getThrYCntGroupByMatType();
	}

	/**
	 * @description 按部门统计领取数量分组
	 * @return
	 */
	@RequestMapping("/getBorrowNumGroupByDept")
	public Map<String, Integer> getBorrowNumGroupByDept() {
		return this.statisticsService.getBorrowNumGroupByDept();
	}

	/**
	 * @description 根据柜子ID获取柜子马达板配置信息
	 * @param cabinetID 柜子ID
	 * @return Map 中 key是行号,value是IP:Port，例如一个返回的Map对象 "1" : "192.168.1.101:502" "2"
	 *         : "192.168.1.102:502" "3" : "192.168.1.103:502" ... "9" :
	 *         "192.168.1.109:502"
	 */
	@RequestMapping("/getMotorBoardIPMap")
	public Map<String, String> getMotorBoardIPMap(@RequestParam("cabinetID") String cabinetID) {
		return this.cabinetRpcService.getMotorBoardIPMap(cabinetID);
	}

	/**
	 * @description 根据手机预约取料ID获得此ID对应的取料是否完成
	 * @param appointmentID 手机预约ID
	 * @return true-取料完成 false-预约未被取出
	 */
	@RequestMapping("/isAppointmentCompletedByID")
	public boolean isAppointmentCompletedByID(@RequestParam("appointmentID") String appointmentID) {
		return this.cabinetRpcService.isAppointmentCompletedByID(appointmentID);
	}

	/**
	 * @description 设置手机预约取料是否完成
	 * @param appointmentID 手机预约ID
	 * @param isCompleted   true - 完成; false - 未完成;
	 * @return 操作信息,成功 或 失败
	 */
	@RequestMapping("/setAppointmentCompletedByID")
	public OpTip setAppointmentCompletedByID(@RequestParam("appointmentID") String appointmentID,
			@RequestParam("isCompleted") boolean isCompleted) {
		return this.cabinetRpcService.setAppointmentCompletedByID(appointmentID, isCompleted);
	}

	/**
	 * @description 绑定借出权限
	 * @param accountId 帐户Id
	 * @param popedoms  借出权限
	 * @return
	 */
	@RequestMapping("/bindBorrowPopedoms")
	public Boolean bindBorrowPopedoms(@RequestParam("accountId") String accountId,
			@RequestParam("popedoms") String popedoms) {
		return this.borrowPopedomService.bindBorrowPopedoms(accountId, popedoms);
	}

	/**
	 * @description 获取暂存柜盒子集合按栈号分组
	 * @param accountId 暂存柜Id
	 * @return
	 */
	@RequestMapping("/getSubBoxMapGroupByBoardNo")
	public Map<String, Map<String, List<LockPara>>> getSubBoxMapGroupByBoardNo(
			@RequestParam("cabinetId") String cabinetId) {
		return this.cabinetRpcService.getSubBoxMapGroupByBoardNo(cabinetId);
	}

	/**
	 * @description 获取待完成的补料清单
	 * @param cabinetId        柜子Id
	 * @param feedingAccountId 补料人Id
	 * @return
	 */
	@RequestMapping("/getWaitFinishFeedingList")
	public List<ExtractionMethod> getFeedingList(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("feedingAccountId") String feedingAccountId) {
		return this.feedingListService.getWaitFinishFeedingList(cabinetId, feedingAccountId);
	}

	/**
	 * @description 根据补料清单Id获取清单明细列表
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/getFeedingDetailListByListId")
	public List<Card> getFeedingDetailListByListId(@RequestParam("feedingListId") String feedingListId) {
		return this.feedingDetailService.getFeedingDetailListByListId(feedingListId);
	}

	/**
	 * @description 完成补料清单
	 * @param feedingListId    补料清单Id
	 * @param feedingAccountId 补料人Id
	 * @return
	 */
	@RequestMapping("/finishFeedingList")
	public OpTip finishFeedingList(@RequestParam("feedingListId") String feedingListId,
			@RequestParam("feedingAccountId") String feedingAccountId) {
		OpTip tip = new OpTip("补料成功");
		try {
			FeedingList feedingList = this.feedingListService.get(feedingListId);
			feedingList.setFeedingAccountId(feedingAccountId);
			feedingList.setFeedingTime(new Date());
			this.feedingListService.finishFeedingList(feedingList);
		} catch (Exception e) {
			tip = new OpTip(201, "补料失败");
		}
		return tip;
	}

	/**
	 * @description 初始化借出权限树
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/initBorrowPopedomTree")
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("accountId") String accountId) {
		return this.borrowPopedomService.initBorrowPopedomTree(accountId);
	}

	/**
	 * @description 暂存柜获取物料信息(我要取料)
	 * @param subboxId  暂存柜盒子Id
	 * @param matInfoId 物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/outsubcabinet")
	public SubMatDetail outsubcabinet(@RequestParam("subboxId") String subboxId,
			@RequestParam("matInfoId") String matInfoId, @RequestParam("accountId") String accountId) {
		SubMatDetail detail = this.cabinetRpcService.outsubcabinet(subboxId, matInfoId, accountId);
		return detail;
	}

	/**
	 * @description 获取未暂存过的物料列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getUnSubMatInfoList")
	public List<SubMatDetail> getUnSubMatInfoList(@RequestParam("accountId") String accountId) {
		return this.cabinetRpcService.getUnSubMatInfoList(accountId);
	}

	/**
	 * @description 根据物料Id获取我要暂存信息
	 * @param matId     物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/getMatDetail")
	public SubMatDetail getMatDetail(@RequestParam("matId") String matId, @RequestParam("accountId") String accountId) {
		return this.cabinetRpcService.getMatDetail(matId, accountId);
	}

	/**
	 * @description 校验新暂存柜权限
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/checkNewSubCabinet")
	public OpTip checkNewSubCabinet(@RequestParam("matId") String matId, @RequestParam("accountId") String accountId) {
		return this.subBoxAccountRefService.checkNewSubCabinet(matId, accountId);
	}

	/**
	 * @description 我要暂存
	 * @param matId     物料Id
	 * @param matNum    物料数量
	 * @param storType  暂存种别
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/tmpNewStor")
	public ElecLock tmpNewStor(@RequestParam("matId") String matId, @RequestParam("matNum") int matNum,
			@RequestParam("storType") String storType, @RequestParam("accountId") String accountId) {
		return this.subCabinetService.tmpNewStor(matId, matNum, storType, accountId);
	}

	/**
	 * @description
	 * @param cabinetId  柜子Id
	 * @param curReserve 当前数量
	 * @param name       物料名称
	 * @return
	 */
	@RequestMapping("/getPro")
	public Lattice getPro(@RequestParam("matId") String matId, @RequestParam("latticeId") String latticeId) {
		Lattice lattice = this.matEquInfoService.getPro(matId, latticeId);
		return lattice;
	}

	/**
	 * @description 增加购物车
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
	 * @description 购物车详情
	 * @param
	 * @return
	 */
	@RequestMapping("/selectCart")
	public List<CartInfo> selectCart(@RequestParam("accountId") String accountId,
			@RequestParam("cabinetId") String cabinetId) {
		return this.cartService.selectCart(accountId, cabinetId);
	}

	/**
	 * @description 修改购物车
	 * @param
	 * @return
	 */
	@RequestMapping("/updateCart")
	public void updateCart(@RequestParam("cartId") String cartId, @RequestParam("num") int num) {
		this.cartService.updateCart(cartId, num);
	}

	/**
	 * @description 删除购物车
	 * @param
	 * @return
	 */
	@RequestMapping("/delCart")
	public void delCart(@RequestParam("cartId") String cartId) {
		this.cartService.delCart(cartId);
	}

	/**
	 * @description 获取柜子列表信息
	 * @param serialNo 柜子序号
	 * @return
	 */
	@RequestMapping("/getCabinetList")
	public List<ExtraCabinet> getCabinetList(@RequestParam("serialNo") String serialNo) {
		return this.equSettingService.getCabinetList(serialNo);
	}

	/**
	 * @description 立即领取每日限额判断
	 * @param accountId 帐户Id
	 */
	@RequestMapping("/getDailyLimit")
	public OpTip getDailyLimit(@RequestParam("accountId") String accountId, @RequestParam("matId") String matId,
			@RequestParam("borrowNum") Integer borrowNum, @RequestBody UserInfo userInfo) {
		OpTip tip = this.dailyLimitService.getDailyLimit(accountId, matId, borrowNum, userInfo);
		return tip;
	}

	/**
	 * @description 购物车领取每日限额判断
	 * @param accountId 帐户Id
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
	 * @description 获取
	 * @param cabinetId
	 * @return
	 */
	@RequestMapping("/getCabinetId")
	public String getCabinetId(@RequestParam("cabinetId") String cabinetId) {
		return this.equSettingService.getEquSettingListByEquInfoId(cabinetId);
	}

	/**
	 * @description 获取
	 * @param cabinetId
	 * @return
	 */
	@RequestMapping("/getDoor")
	public EnumDoor getDoor(@RequestParam("ip") String ip) {
		return this.equDetailService.getDoor(ip);
	}

	/*
	 * @description 根据帐号Id获取用户信息
	 * 
	 * @param accountId 账户Id
	 * 
	 * @return
	 */
	@RequestMapping("/getUserInfoByAccountId")
	public UserInfo getUserInfoByAccountId(@RequestParam("accountId") String accountId) {
		UserInfo userInfo = this.adminService.getUserInfo(accountId);
		return userInfo;
	}

	/**
	 * @description 根据柜子Id获取库存列表
	 * @param cabinetId 柜子Id
	 * @param query     查询范围条件
	 * @param search    查询过滤条件
	 * @return
	 */
	@RequestMapping("/getFeedDetailStaList")
	public List<FeedDetailSta> getFeedDetailStaList(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("query") String query, @RequestParam("search") String search) {
		return this.equDetailStaService.getFeedDetailStaList(cabinetId, query, search);
	}

	/**
	 * @description 手动补料
	 * @param feed      补料信息
	 * @param cabinetId 当前柜子Id
	 * @param accountId 账户Id
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
	 * @description 获取物料剩余数量
	 * @param cabinetId 当前柜子Id
	 * @param matId     物料Id
	 * @return
	 */
	@RequestMapping("/getMatRemainNumByMatId")
	public MatDetail getMatRemainNumByMatId(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("matId") String matId) {
		return this.cabinetRpcService.getMatRemainNumByMatId(cabinetId, matId);
	}

	/**
	 * @description 柜子故障报备
	 * @param staId 柜子索引号
	 * @return
	 */
	@RequestMapping("/equStaProblem")
	public Boolean equStaProblem(@RequestParam("staId") String staId) {
		return this.equDetailStaService.equStaProblem(staId);
	}

	/**
	 * @description 故障状态恢复
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/resetProblem")
	public Boolean resetProblem(@RequestParam("cabinetId") String cabinetId) {
		return this.equDetailStaService.resetProblem(cabinetId);
	}

	/**
	 * 
	 * @description 通知管理-修改计数
	 */
	@RequestMapping("/editCount")
	public List<NoticeMgrInfo> editCount(@RequestParam("cabinetId") String cabinetId) {
		return this.noticeMgrService.editCount(cabinetId);
	}

	/**
	 * @description 通知管理-查询计数
	 */
	@RequestMapping("/getNoticeMgr")
	public List<NoticeMgrInfo> getNoticeMgr(@RequestParam("cabinetId") String cabinetId) {
		return this.noticeMgrService.getNoticeMgr(cabinetId);
	}

	/**
	 * @description 通知管理-重置计数
	 */
	@RequestMapping("/resetNoticeMgr")
	public NoticeMgrInfo resetNoticeMgr(@RequestParam("cabinetId") String cabinetId, @RequestParam("num") int num,
			@RequestParam("noticeType") String noticeType) {
		return this.noticeMgrService.resetNoticeMgr(cabinetId, num, noticeType);
	}

	/**
	 * @description 当前库存检查
	 */
	@RequestMapping("/curNumCheck")
	public OpTip curNumCheck(@RequestParam("staId") String staId, @RequestParam("borrowNum") int borrowNum,
			@RequestParam("cabinetId") String cabinetId) {
		return this.equDetailStaService.curNumCheck(staId, borrowNum, cabinetId);
	}

	/**
	 * @description 领用信息通讯
	 * @param notice 领用通讯对象
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
	 * @description 修改暂存柜数量
	 * @param accountId   账户id
	 * @param extraBoxNum 暂存柜数量
	 * @return
	 */
	@RequestMapping("/updateExtraBoxNum")
	public void updateExtraBoxNum(@RequestParam("accountId") String accountId,
			@RequestParam("extraBoxNum") String extraBoxNum) {
		this.extraBoxNumSettingService.updateExtraBoxNum(accountId, extraBoxNum);
	}

	/**
	 * @description 查询暂存柜数量
	 * @param accountId 账户id
	 * @return
	 */
	@RequestMapping("/getExtraBoxNum")
	public ExtraBoxNum getExtraBoxNum(@RequestParam("accountId") String accountId) {
		return this.extraBoxNumSettingService.getExtraBoxNum(accountId);
	}

	/**
	 * @description 删除暂存柜数量
	 * @param accountId 账户id
	 * @return
	 */
	@RequestMapping("/delExtraBoxNum")
	public void delExtraBoxNum(@RequestParam("accountId") String accountId) {
		this.extraBoxNumSettingService.delExtraBoxNum(accountId);
	}

	/**
	 * @description 删除通知管理收件人
	 * @param accountId 账户id
	 * @return
	 */
	@RequestMapping("/delNoticeMgr")
	public void delNoticeMgr(@RequestParam("accountId") String accountId) {
		this.noticeMgrService.delNoticeMgr(accountId);
	}

	/**
	 * @description 根据主柜Id获取柜体树形集合
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	@RequestMapping("/getCabinetTreeList")
	public List<ExtraCabinet> getCabinetTreeList(@RequestParam("mainCabinetId") String mainCabinetId) {
		return this.equSettingService.getCabinetTreeList(mainCabinetId);
	}

	/**
	 * @description 判断是否已归还
	 * @param billId 流水Id
	 * @return
	 */
	@RequestMapping("/isBackCheck")
	public Boolean isBackCheck(@RequestParam("returnBackNo") String returnBackNo) {
		return this.matReturnBackService.isBackCheck(returnBackNo);
	}

	/**
	 * @description 获取归还扫码信息
	 * @param billId 流水Id
	 * @return
	 */
	@RequestMapping("/getReturnInfo")
	public List<ReturnBackPrintInfo> getReturnInfo(@RequestParam("returnBackNo") String returnBackNo) {
		return this.matReturnBackService.getReturnInfo(returnBackNo);
	}

	/**
	 * @description 获取库存
	 * @param mainCabinetId 主柜Id
	 */
	@RequestMapping("/getStockTip")
	public List<StockTip> getStockTip(@RequestParam("mainCabinetId") String mainCabinetId) {
		return this.equDetailStaService.getStockTip(mainCabinetId);
	}

	/**
	 * @description 根据柜体Id获取库别名称
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/getWareHouseAliasByCabinetId")
	public String getWareHouseAliasByCabinetId(@RequestParam("cabinetId") String cabinetId) {
		return this.equSettingService.get(cabinetId).getWareHouseAlias();
	}

	/**
	 * @description 根据柜子Id获取物料余量
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/getMatNumMapByCabinetId")
	public Map<String, Integer> getMatNumMapByCabinetId(@RequestParam("cabinetId") String cabinetId) {
		return this.equDetailStaService.getMatNumMapByCabinetId(cabinetId);
	}

	/**
	 * @description 根据编码获取物料详情
	 * @param barCode 物料编码
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
	 * @description 库存校验
	 * @param cabinetId  刀具柜Id
	 * @param voucherMap 申请单详情
	 * @return
	 */
	@RequestMapping("/stockCheck")
	public OpTip stockCheck(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) Map<String, List<CartInfo>> voucherMap) {
		return this.cabinetRpcService.stockCheck(cabinetId, voucherMap);
	}

	/**
	 * @description 发送申请单到服务端
	 * @param cabinetId  刀具柜Id
	 * @param voucherMap 申请单集合
	 * @param accountId  帐户Id
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
	 * @description 获取申请单结果
	 * @param applyVoucherResult 申请单序号|领料记录Id1,领料记录Id2
	 * @return
	 */
	@RequestMapping("/getApplyVoucherResult")
	public List<ApplyVoucher> getApplyVoucherResult(@RequestParam("applyVoucherResult") String applyVoucherResult) {
		return this.matUseRecordService.getApplyVoucherResult(applyVoucherResult);
	}

	/**
	 * @description 获取补料清单详情(用于推送到第三方)
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/syncFeedingList")
	public FeignFeedingList syncFeedingList(@RequestParam("feedingListId") String feedingListId) {
		return this.feedingListService.getFeeding(feedingListId);
	}

	/**
	 * @description 获取补料确认的入库单
	 * @param feedingListId 补料清单Id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/warehouseFeedList")
	public List<FeignWarehouseFeed> warehouseFeedList(@RequestParam("feedingListId") String feedingListId)
			throws IOException {
		return this.warehouseFeedService.getWarehouseFeedList(feedingListId);
	}

	/**
	 * @description 入库单确认结果保存
	 * @param feedingListId 补料清单Id
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("/warehouseSave")
	public void warehouseSave(@RequestParam("feedNo") String feedNo, @RequestParam("message") String message,
			@RequestParam("result") Boolean result) throws IOException {
		this.warehouseFeedService.warehouseSave(feedNo, message, result);
	}

	/**
	 * @description 可控抽屉柜数据通讯
	 * @param notice 可控抽屉柜数据
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