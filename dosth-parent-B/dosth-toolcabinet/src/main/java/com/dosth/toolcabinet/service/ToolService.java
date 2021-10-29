package com.dosth.toolcabinet.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cnbaosi.dto.ElecLock;
import com.cnbaosi.dto.tool.ApplyVoucher;
import com.cnbaosi.dto.tool.FeignCodeName;
import com.cnbaosi.dto.tool.FeignFeedingList;
import com.cnbaosi.dto.tool.FeignWarehouseFeed;
import com.dosth.comm.LockPara;
import com.dosth.comm.node.TypeNode;
import com.dosth.comm.node.ZTreeNode;
import com.dosth.constant.EnumDoor;
import com.dosth.dto.BorrowNotice;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.Lattice;
import com.dosth.dto.StockTip;
import com.dosth.dto.TrolDrawerNotice;
import com.dosth.toolcabinet.MyPage;
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
 * @description 工具访问接口
 * 
 * @author guozhidong
 *
 */
@FeignClient("service-tool")
public interface ToolService {

	/**
	 * 根据刀具柜ID发送库存预警邮件
	 * 
	 * @param cabinetId 刀具柜ID
	 */
	@RequestMapping("/feign/stockEmail")
	public void stockEmail(@RequestParam("cabinetId") String cabinetId);

	/**
	 * 借用信息推送
	 * 
	 */
	@RequestMapping("/feign/borrowPost")
	public void borrowPost(@RequestParam(value = "recordId") String recordId);

	/**
	 * @description 根据序列号获取刀具柜信息
	 * @param serialNo 序列号
	 * @return
	 */
	@RequestMapping("/feign/getCabinetSetupBySerialNo")
	public Map<String, Map<String, String>> getCabinetSetupBySerialNo(@RequestParam("serialNo") String serialNo);

	/**
	 * @description 根据设备和用户领取权限获取领取类型集合
	 * 
	 * @param equId     设备 Id
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getVer")
	public String getVer();

	/**
	 * @description 根据设备和用户领取权限获取领取类型集合
	 * 
	 * @param equId     设备 Id
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getBorrowTypeList/{cabinetId}/{accountId}")
	public List<RpcBorrowType> getBorrowTypeList(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId);

	/**
	 * @description 获取板子集合
	 * 
	 * @param cabinetId 柜子Id
	 * 
	 * @return
	 */
	@RequestMapping("/feign/getCardList/{cabinetId}")
	public List<Card> getCardList(@PathVariable(value = "cabinetId") String cabinetId);

	/**
	 * @description 获取刀具柜单元格数量信息
	 * 
	 * @param equBar 刀具柜编码
	 * @return
	 */
	@RequestMapping("/feign/getLatticeValueMap/{cabinetId}")
	public Map<String, Integer> getLatticeValueMap(@PathVariable(value = "cabinetId") String cabinetId);

	/**
	 * @description 获取物料树
	 * 
	 * @param cabinetId 刀具柜Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getCategoryTree/{cabinetId}/{accountId}/{type}")
	public List<TypeNode> getCategoryTree(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId, @PathVariable(value = "type") String type);

	/**
	 * @description 获取物料类型树
	 * 
	 * @param cabinetId 刀具柜Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getMatTypeTree/{cabinetId}/{accountId}")
	public List<TypeNode> getMatTypeTree(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId);

	/**
	 * @description 根据种别获取物料关联
	 * @param cabinetId 刀具柜Id
	 * @param equId     种别Id
	 * @return
	 */
	@RequestMapping("/feign/getMatListByEqu/{cabinetId}/{accountId}/{equId}")
	public List<MatDetail> getMatListByEqu(@PathVariable(value = "cabinetId") String cabinetId,
			@PathVariable(value = "accountId") String accountId, @PathVariable(value = "equId") String equId);

	/**
	 * @description 库存校验
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/stockCkeck")
	public OpTip stockCkeck(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) List<CartInfo> cartList);

	/**
	 * @description 库存校验
	 * @param cabinetId 刀具柜Id
	 * @param cartMap   申请单详情
	 * @return
	 */
	@RequestMapping("/feign/stockCheck")
	public OpTip stockCheck(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) Map<String, List<CartInfo>> cartMap);

	/**
	 * @description 发送购物车到服务端
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/sendCartToServer")
	public List<ExtraCabinet> sendCartToServer(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) List<CartInfo> cartList,
			@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId);

	/**
	 * @description 发送申请单到服务端
	 * @param cabinetId  刀具柜Id
	 * @param voucherMap 申请单集合
	 * @param accountId  帐户Id
	 * @return
	 */
	@RequestMapping("/feign/sendApplyVoucherToServer")
	public List<ExtraCabinet> sendApplyVoucherToServer(@RequestParam(value = "cabinetId") String cabinetId,
			@RequestBody(required = true) Map<String, List<CartInfo>> voucherMap,
			@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId);

	/**
	 * @description 以旧换新查询流水信息
	 * @param billId
	 * @return
	 */
	@RequestMapping("/feign/getBillInfo")
	public BillInfo getBillInfo(@RequestParam(value = "billId") String billId);

	/**
	 * @description 栅栏借出
	 * @param borrowGrid  栅栏借出信息
	 * @param accountId   帐户Id
	 * @param receiveType 借出类型
	 * @param receiveInfo 借出信息
	 */
	@RequestMapping("/feign/borrowByGrid")
	public OpTip borrowByGrid(@RequestBody(required = true) BorrowGrid borrowGrid,
			@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "receiveType") String receiveType,
			@RequestParam(value = "receiveInfo") String receiveInfo);

	/**
	 * @description 获取未归还类型
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getUnReturnTypeList")
	public List<FeignCodeName> getUnReturnTypeList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "cabinetId") String cabinetId);

	/**
	 * @description 获取未归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getUnReturnList")
	public Map<String, List<SubMatDetail>> getUnReturnList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo);

	/**
	 * @description 获取常规归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getBackNormalList")
	public MyPage<SubMatDetail> getBackNormalList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo, @RequestParam("pageNo") int pageNo);

	/**
	 * @description 获取批量归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getBackBatchList")
	public List<SubMatDetail> getBackBatchList(@RequestParam(value = "accountId") String accountId,
			@RequestParam(value = "serialNo") String serialNo);

	/**
	 * @description 获取我的暂存柜信息
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getSubCabinetRpcList")
	public List<SubMatDetail> getSubCabinetRpcList(@RequestParam(value = "shareSwitch") String shareSwitch,
			@RequestParam(value = "accountId") String accountId);

	/**
	 * @description 暂存
	 * @param matUseBillId 借出物料流水Id
	 * @param borrowNum    暂存数量
	 * @param accountId    帐户Id
	 */
	@RequestMapping("/feign/tempstor/{matUseBillId}/{borrowNum}/{accountId}")
	public OpTip tempStor(@PathVariable("matUseBillId") String matUseBillId, @PathVariable("borrowNum") int borrowNum,
			@PathVariable("accountId") String accountId);

	/**
	 * @description 获取归还类型组
	 * @return
	 */
	@RequestMapping("/feign/getReBackTypeMap")
	public Map<String, List<ReBackType>> getReBackTypeMap();

	/**
	 * @description 单个归还
	 * @param returnBackNo 归还流水号
	 * @return
	 */
	@RequestMapping("/feign/returnBackSingle")
	public OpTip returnBackSingle(@RequestParam("returnBackNo") String returnBackNo);

	/**
	 * @description 条形码查询
	 * @return
	 */
	@RequestMapping("/feign/findBybarcode")
	public OpTip findBybarcode(@RequestParam("barCode") String barCode);

	/**
	 * @description 操作副柜
	 * @param subBoxId  副柜盒子Id
	 * @param matInfoId 物料信息Id
	 * @param num       操作数量
	 * @param accountId 帐号Id
	 * @return
	 */
	@RequestMapping("/feign/opSubBox")
	public OpTip opSubBox(@RequestParam("subBoxId") String subBoxId, @RequestParam("matInfoId") String matInfoId,
			@RequestParam("num") Integer num, @RequestParam("accountId") String accountId);

	/**
	 * @description 获取打印信息
	 * @param matUseBillIds 物料领取Id
	 * @param accountId     归还人
	 * @param returnInfo    归还信息
	 * @param isGetNewOne   以旧换新
	 * @param barcode       绑定二维码
	 * @param realLife      制造产量
	 * @param serialNum     产品流水号
	 * @return
	 *         <p>
	 *         二维码数据格式内容如下: 物料领用序号Id;归还类型Id;归还人帐号Id;以旧换新标识;物料Id
	 *         matUseBillId;returnTypeId;accountId;returnInfo;isGetNewOne;matInfoId;barcode
	 *         </p>
	 */
	@RequestMapping("/feign/getPrintInfo")
	public ReturnBackPrintInfo getPrintInfo(@RequestParam("matBillId") String matBillId,
			@RequestParam("accountId") String accountId, @RequestParam("isGetNewOne") String isGetNewOne,
			@RequestParam("matInfoId") String matInfoId, @RequestParam("backNum") int backNum,
			@RequestParam("cabinetId") String cabinetId, @RequestParam("barcode") String barcode,
			@RequestParam("returnInfo") String returnInfo, @RequestParam("backWay") String backWay,
			@RequestParam("realLife") String realLife, @RequestParam("serialNum") String serialNum);

	// The sessoin below for mobile App
	/**
	 * @description 根据柜子ID获取柜子马达板配置信息
	 * @param cabinetID 柜子ID
	 * @return Map 中 key是行号,value是IP:Port，例如一个返回的Map对象 "1" : "192.168.1.101:502" "2"
	 *         : "192.168.1.102:502" "3" : "192.168.1.103:502" ... "9" :
	 *         "192.168.1.109:502"
	 */
	@RequestMapping("/feign/getMotorBoardIPMap")
	public Map<String, String> getMotorBoardIPMap(@RequestParam("cabinetID") String cabinetID);

	/**
	 * @description 根据手机预约取料ID获得此ID对应的取料是否完成
	 * @param appointmentID 手机预约ID
	 * @return true-取料完成 false-预约未被取出
	 */
	@RequestMapping("/feign/isAppointmentCompletedByID")
	public boolean isAppointmentCompletedByID(@RequestParam("appointmentID") String appointmentID);

	/**
	 * @description 设置手机预约取料是否完成
	 * @param appointmentID 手机预约ID
	 * @param isCompleted   true - 完成; false - 未完成;
	 * @return 操作信息,成功 或 失败
	 */
	@RequestMapping("/feign/setAppointmentCompletedByID")
	public OpTip setAppointmentCompletedByID(@RequestParam("appointmentID") String appointmentID,
			@RequestParam("isCompleted") boolean isCompleted);
	// End,The sessoin below for mobile App

	/**
	 * @description 获取暂存柜盒子集合按栈号分组
	 * @param accountId 暂存柜Id
	 * @return
	 */
	@RequestMapping("/feign/getSubBoxMapGroupByBoardNo")
	public Map<String, Map<String, List<LockPara>>> getSubBoxMapGroupByBoardNo(
			@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 获取主柜详情
	 * @param cabinetId 主柜Id
	 * @return
	 */
	@RequestMapping("/feign/getDetailStaMap")
	public Map<String, List<Lattice>> getDetailStaMap(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 修改物料库存数量
	 * @param equDetailId 槽位Id
	 * @param value       添加数量
	 * @param accountId   帐户Id
	 * @return
	 */
	@RequestMapping("/feign/updateDetailStaValue")
	public Boolean updateDetailStaValue(@RequestParam("equDetailId") String equDetailId,
			@RequestParam("value") Integer value, @RequestParam("accountId") String accountId);

	/**
	 * @description 绑定借出权限
	 * @param accountId 帐户Id
	 * @param popedoms  借出权限
	 * @return
	 */
	@RequestMapping("/feign/bindBorrowPopedoms")
	public Boolean bindBorrowPopedoms(@RequestParam("accountId") String accountId,
			@RequestParam("popedoms") String popedoms);

	/**
	 * @description 获取待完成的补料清单
	 * @param cabinetId        柜子Id
	 * @param feedingAccountId 补料人Id
	 * @return
	 */
	@RequestMapping("/feign/getWaitFinishFeedingList")
	public List<ExtractionMethod> getFeedingList(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("feedingAccountId") String feedingAccountId);

	/**
	 * @description 根据补料清单Id获取清单明细列表
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/feign/getFeedingDetailListByListId")
	public List<Card> getFeedingDetailListByListId(@RequestParam("feedingListId") String feedingListId);

	/**
	 * @description 完成补料清单
	 * @param feedingListId    补料清单Id
	 * @param feedingAccountId 补料人Id
	 * @return
	 */
	@RequestMapping("/feign/finishFeedingList")
	public OpTip finishFeedingList(@RequestParam("feedingListId") String feedingListId,
			@RequestParam("feedingAccountId") String feedingAccountId);

	/**
	 * @description 初始化借出权限树
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/feign/initBorrowPopedomTree")
	public List<ZTreeNode> initBorrowPopedomTree(@RequestParam("accountId") String accountId);

	/**
	 * @description 暂存柜获取物料信息(我要取料)
	 * @param subboxId  暂存柜盒子Id
	 * @param matInfoId 物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/outsubcabinet")
	public SubMatDetail outsubcabinet(@RequestParam("subboxId") String subboxId,
			@RequestParam("matInfoId") String matInfoId, @RequestParam("accountId") String accountId);

	/**
	 * @description 获取未暂存过的物料列表
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getUnSubMatInfoList")
	public List<SubMatDetail> getUnSubMatInfoList(@RequestParam("accountId") String accountId);

	/**
	 * @description 根据物料Id获取我要暂存信息
	 * @param matId     物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/getMatDetail")
	public SubMatDetail getMatDetail(@RequestParam("matId") String matId, @RequestParam("accountId") String accountId);

	/**
	 * @description 校验新暂存柜权限
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/checkNewSubCabinet")
	public OpTip checkNewSubCabinet(@RequestParam("matId") String matId, @RequestParam("accountId") String accountId);

	/**
	 * @description 我要暂存
	 * @param matId     物料Id
	 * @param matNum    物料数量
	 * @param storType  暂存种别
	 * @param accountId 帐户Id
	 * @return
	 */
	@RequestMapping("/feign/tmpNewStor")
	public ElecLock tmpNewStor(@RequestParam("matId") String matId, @RequestParam("matNum") int matNum,
			@RequestParam("storType") String storType, @RequestParam("accountId") String accountId);

	/**
	 * @description 产品详情
	 * @param cabinetId  柜子Id
	 * @param curReserve 当前数量
	 * @param name       物料名称
	 * @return
	 */
	@RequestMapping("/feign/getPro")
	public Lattice getPro(@RequestParam("matId") String matId, @RequestParam("latticeId") String latticeId);

	/**
	 * @description 增加购物车
	 * @param latticeId 格子Id
	 * @return
	 */
	@RequestMapping("/feign/addCart")
	public void addCart(@RequestParam("matId") String matId, @RequestParam("type") String type,
			@RequestParam("num") int num, @RequestParam("accountId") String accountId,
			@RequestParam("receiveType") String receiveType, @RequestParam("receiveInfo") String receiveInfo,
			@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 购物车详情
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/selectCart")
	public List<CartInfo> selectCart(@RequestParam("accountId") String accountId,
			@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 修改购物车
	 * @param cartId 购物车Id
	 * @return
	 */
	@RequestMapping("/feign/updateCart")
	public void updateCart(@RequestParam("cartId") String cartId, @RequestParam("num") int num);

	/**
	 * @description 删除购物车
	 * @param cartId 购物车Id
	 * @return
	 */
	@RequestMapping("/feign/delCart")
	public void delCart(@RequestParam("cartId") String cartId);

	/**
	 * @description 获取柜子列表信息
	 * @param serialNo 柜子序号
	 * @return
	 */
	@RequestMapping("/feign/getCabinetList")
	public List<ExtraCabinet> getCabinetList(@RequestParam("serialNo") String serialNo);

	/**
	 * @description 立即领取每日限额判断CabinetInfo
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getDailyLimit")
	public OpTip getDailyLimit(@RequestParam("accountId") String accountId, @RequestParam("matId") String matId,
			@RequestParam("borrowNum") Integer borrowNum, @RequestBody UserInfo userInfo);

	/**
	 * @description 购物车领取每日限额判断
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getDailyLimitByCart")
	public OpTip getDailyLimitByCart(@RequestParam("shareSwitch") String shareSwitch,
			@RequestParam("accountId") String accountId, @RequestParam("startTime") String startTime,
			@RequestParam("endTime") String endTime, @RequestParam("limitSumNum") Integer limitSumNum,
			@RequestParam("notReturnLimitNum") Integer notReturnLimitNum,
			@RequestBody(required = true) Map<String, CartInfo> limitMap);

	/**
	 * @description 获取子柜Id
	 * @param cabinetId
	 * @return
	 */
	@RequestMapping("/feign/getCabinetId")
	public String getCabinetId(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 获取子柜Id
	 * @param cabinetId
	 * @return
	 */
	@RequestMapping("/feign/getDoor")
	public EnumDoor getDoor(@RequestParam("ip") String ip);

	/**
	 * @description 根据帐号Id获取用户信息
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/getUserInfoByAccountId")
	public UserInfo getUserInfoByAccountId(@RequestParam("accountId") String accountId);

	/**
	 * @description 根据柜子Id获取库存列表
	 * @param cabinetId 柜子Id
	 * @param query     查询范围条件
	 * @param search    查询过滤条件
	 * @return
	 */
	@RequestMapping("/feign/getFeedDetailStaList")
	public List<FeedDetailSta> getFeedDetailStaList(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("query") String query, @RequestParam("search") String search);

	/**
	 * @description 手动补料
	 * @param feed      补料信息
	 * @param cabinetId 当前柜子Id
	 * @param accountId 账户Id
	 * @return
	 */
	@RequestMapping("/feign/feed")
	public Boolean feed(@RequestParam("feed") String feed, @RequestParam("cabinetId") String cabinetId,
			@RequestParam("accountId") String accountId);

	/**
	 * @description 获取物料剩余数量
	 * @param cabinetId 当前柜子Id
	 * @param matId     物料Id
	 * @return
	 */
	@RequestMapping("/feign/getMatRemainNumByMatId")
	public MatDetail getMatRemainNumByMatId(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("matId") String matId);

	/**
	 * @description 柜子故障报备
	 * @param staId 柜子索引号
	 * @return
	 */
	@RequestMapping("/feign/equStaProblem")
	public Boolean equStaProblem(@RequestParam("staId") String staId);

	/**
	 * @description 故障状态恢复
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/feign/resetProblem")
	public Boolean resetProblem(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 通知管理-修改计数
	 */
	@RequestMapping("/feign/editCount")
	public List<NoticeMgrInfo> editCount(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 通知管理-查询计数
	 */
	@RequestMapping("/feign/getNoticeMgr")
	public List<NoticeMgrInfo> getNoticeMgr(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 通知管理-重置计数
	 */
	@RequestMapping("/feign/resetNoticeMgr")
	public NoticeMgrInfo resetNoticeMgr(@RequestParam("cabinetId") String cabinetId, @RequestParam("num") int num,
			@RequestParam("noticeType") String noticeType);

	/**
	 * @description 当前库存检查
	 */
	@RequestMapping("/feign/curNumCheck")
	public OpTip curNumCheck(@RequestParam("staId") String staId, @RequestParam("borrowNum") int borrowNum,
			@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 领用信息通讯
	 * @param notice 领用通讯对象
	 * @return
	 */
	@RequestMapping("/feign/notice")
	public OpTip notice(@RequestBody(required = true) BorrowNotice notice);

	/**
	 * @description 根据主柜Id获取柜体树形集合
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	@RequestMapping("/feign/getCabinetTreeList")
	public List<ExtraCabinet> getCabinetTreeList(@RequestParam("mainCabinetId") String mainCabinetId);

	/**
	 * @description 判断是否已归还
	 * @param billId 流水Id
	 * @return
	 */
	@RequestMapping("/feign/isBackCheck")
	public Boolean isBackCheck(@RequestParam("returnBackNo") String returnBackNo);

	/**
	 * @description 获取归还扫码信息
	 * @param billId 流水Id
	 * @return
	 */
	@RequestMapping("/feign/getReturnInfo")
	public List<ReturnBackPrintInfo> getReturnInfo(@RequestParam("returnBackNo") String returnBackNo);

	/**
	 * @description 获取库存
	 * @param mainCabinetId 主柜Id
	 */
	@RequestMapping("/feign/getStockTip")
	public List<StockTip> getStockTip(@RequestParam("mainCabinetId") String mainCabinetId);

	/**
	 * @description 根据柜体Id获取库别名称
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@RequestMapping("/feign/getWareHouseAliasByCabinetId")
	public String getWareHouseAliasByCabinetId(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 根据柜子Id获取物料余量
	 * @param cabinetId 柜子Id
	 * @return
	 */
	@RequestMapping("/feign/getMatNumMapByCabinetId")
	public Map<String, Integer> getMatNumMapByCabinetId(@RequestParam("cabinetId") String cabinetId);

	/**
	 * @description 根据编码获取物料详情
	 * @param barCode 物料编码
	 * @return
	 */
	@RequestMapping("/feign/getMatDetailByBarCode")
	public MatDetail getMatDetailByBarCode(@RequestParam("barCode") String barCode);

	/**
	 * @description 获取申请单结果
	 * @param applyVoucherResult 申请单序号|领料记录Id1,领料记录Id2
	 * @return
	 */
	@RequestMapping("/feign/getApplyVoucherResult")
	public List<ApplyVoucher> getApplyVoucherResult(@RequestParam("applyVoucherResult") String applyVoucherResult);

	/**
	 * @description 获取补料清单详情(用于推送到第三方)
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/feign/syncFeedingList")
	public FeignFeedingList syncFeedingList(@RequestParam("feedingListId") String feedingListId);

	/**
	 * @description 获取补料确认的入库单
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/feign/warehouseFeedList")
	public List<FeignWarehouseFeed> warehouseFeedList(@RequestParam("feedingListId") String feedingListId);

	/**
	 * @description 入库单确认
	 * @param feedingListId 补料清单Id
	 * @return
	 */
	@RequestMapping("/feign/warehouseSave")
	public void warehouseSave(@RequestParam("feedNo") String feedNo, @RequestParam("message") String message,
			@RequestParam("result") Boolean result);

	/**
	 * @description 可控抽屉柜数据通讯
	 * @param notice 可控抽屉柜数据
	 * @return
	 */
	@RequestMapping("/feign/noticeTrolDrawerNotice")
	public OpTip notice(@RequestBody(required = true) TrolDrawerNotice notice);
}