package com.dosth.tool.rpc;

import java.util.List;
import java.util.Map;

import com.cnbaosi.dto.tool.FeignCodeName;
import com.dosth.comm.LockPara;
import com.dosth.dto.Card;
import com.dosth.dto.ExtraCabinet;
import com.dosth.tool.MyPage;
import com.dosth.toolcabinet.dto.BorrowGrid;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.dto.MatDetail;
import com.dosth.toolcabinet.dto.ReBackType;
import com.dosth.toolcabinet.dto.ReturnBackPrintInfo;
import com.dosth.toolcabinet.dto.SubMatDetail;
import com.dosth.util.OpTip;

/**
 * @description 柜子远程访问接口
 * 
 * @author guozhidong
 *
 */
public interface CabinetRpcService {

	/**
	 * @description 根据序列号获取刀具柜信息
	 * @param serialNo 序列号
	 * @return
	 */
	public Map<String, Map<String, String>> getCabinetSetupBySerialNo(String serialNo);

	/**
	 * @description 获取板子集合
	 * 
	 * @param cabinetId 刀具柜Id
	 * @return
	 */
	public List<Card> getCardList(String cabinetId);

	/**
	 * @description 获取刀具柜单元格数量信息
	 * 
	 * @param cabinetId 刀具柜Id
	 * @return
	 */
	public Map<String, Integer> getLatticeValueMap(String cabinetId);

	/**
	 * @description 根据种别获取物料关联
	 * @param cabinetId 刀具柜Id
	 * @param accountId 帐户Id
	 * @param equId     种别Id
	 * @return
	 */
	public List<MatDetail> getMatListByEqu(String cabinetId, String accountId, String matId);

	/**
	 * @description 校验库存
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public OpTip stockCkeck(String cabinetId, List<CartInfo> cartList);

	/**
	 * @description 发送购物车到服务端
	 * @param cabinetId 刀具柜Id
	 * @param cartList  购物车<cartId>列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<ExtraCabinet> sendCartToServer(String cabinetId, List<CartInfo> cartList, String shareSwitch,
			String accountId);

	/**
	 * @description 栅栏借出
	 * @param borrowGrid  栅栏借出信息
	 * @param accountId   帐户Id
	 * @param receiveType 借出类型
	 * @param receiveInfo 借出信息
	 */
	public OpTip borrowByGrid(BorrowGrid borrowGrid, String accountId, String receiveType, String receiveInfo);

	/**
	 * @description 获取待归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<SubMatDetail> getUnReturnList(String accountId);

	/**
	 * @description 获取未归还类型
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<FeignCodeName> getUnReturnTypeList(String accountId, String cabinetId);

	/**
	 * @description 获取未归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public Map<String, List<SubMatDetail>> getUnReturnList(String accountId, String serialNo);

	/**
	 * @description 获取待归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public MyPage<SubMatDetail> getBackNormalList(String accountId, String serialNo, int pageNo);

	/**
	 * @description 获取待归还列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<SubMatDetail> getBackBatchList(String accountId, String serialNo);

	/**
	 * @description 获取我的暂存柜集合
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<SubMatDetail> getSubCabinetRpcList(String shareSwitch, String accountId);

	/**
	 * @description 暂存
	 * @param matUseBillId 借出物料流水Id
	 * @param borrowNum    暂存数量
	 * @param accountId    帐户Id
	 */
	public void tempStor(String matUseBillId, int borrowNum, String accountId);

	/**
	 * @description 获取归还类型组
	 */
	public Map<String, List<ReBackType>> getReBackTypeMap();

	/**
	 * @description 归还
	 * @param returnBackNo
	 */
	public void returnBackSingle(String returnBackNo) throws Exception;

	/**
	 * @description 查询条形码是否在使用
	 * @param barcode 归还条形码
	 */
	public OpTip findBybarcode(String barCode);

	/**
	 * @description 操作副柜
	 * @param subBoxId  副柜盒子Id
	 * @param matInfoId 物料信息Id
	 * @param num       操作数量
	 * @param accountId 帐号Id
	 * @return
	 */
	public void opSubBox(String subBoxId, String matInfoId, Integer num, String accountId);

	/**
	 * @description 获取打印信息
	 * @param matUseBillIds 物料领取Id
	 * @param accountId     归还人
	 * @param isGetNewOne   以旧换新
	 * @param barcode       绑定二维码
	 * @param returnInfo    归还信息
	 * @param realLife      制造产量
	 * @param serialNum     产品流水号
	 * @return
	 *         <p>
	 *         二维码数据格式内容如下: 物料领用序号Id;归还人帐号Id;以旧换新标识;物料Id;绑定二维码;归还信息
	 *         matUseBillId;accountId;isGetNewOne;matInfoId;barcode;returnInfo
	 *         </p>
	 */
	public ReturnBackPrintInfo getPrintInfo(String matBillId, String accountId, String isGetNewOne, String matInfoId,
			int backNum, String cabinetId, String barcode, String returnInfo, String backWay, String realLife,
			String serialNum);

	/**
	 * @description 根据柜子ID获取柜子马达板配置信息
	 * @param cabinetID 柜子ID
	 * @return Map 中 key是行号,value是IP:Port，例如一个返回的Map对象 "1" : "192.168.1.101:502" "2"
	 *         : "192.168.1.102:502" "3" : "192.168.1.103:502" ... "9" :
	 *         "192.168.1.109:502"
	 */
	public Map<String, String> getMotorBoardIPMap(String cabinetID);

	/**
	 * @description 根据手机预约取料ID获得此ID对应的取料是否完成
	 * @param appointmentID 手机预约ID
	 * @return true-取料完成 false-预约未被取出
	 */
	public boolean isAppointmentCompletedByID(String appointmentID);

	/**
	 * @description 设置手机预约取料是否完成
	 * @param appointmentID 手机预约ID
	 * @param isCompleted   true - 完成; false - 未完成;
	 * @return 操作信息,成功 或 失败
	 */
	public OpTip setAppointmentCompletedByID(String appointmentID, boolean isCompleted);

	/**
	 * @description 获取暂存柜盒子集合按栈号分组
	 * @param accountId 暂存柜Id
	 * @return
	 */
	public Map<String, Map<String, List<LockPara>>> getSubBoxMapGroupByBoardNo(String cabinetId);

	/**
	 * @description 获取未暂存过的物料列表
	 * @param accountId 帐户Id
	 * @return
	 */
	public List<SubMatDetail> getUnSubMatInfoList(String accountId);

	/**
	 * @description 暂存柜获取物料信息(我要取料)
	 * @param subboxId  暂存柜盒子Id
	 * @param matInfoId 物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	public SubMatDetail outsubcabinet(String subboxId, String matInfoId, String accountId);

	/**
	 * @description 根据物料Id获取我要暂存信息
	 * @param matId     物料Id
	 * @param accountId 帐户Id
	 * @return
	 */
	public SubMatDetail getMatDetail(String matId, String accountId);

	/**
	 * @description 获取物料剩余数量
	 * @param cabinetId 当前柜子Id
	 * @param matId     物料Id
	 * @return
	 */
	public MatDetail getMatRemainNumByMatId(String cabinetId, String matId);

	/**
	 * @description 库存校验
	 * @param cabinetId  刀具柜Id
	 * @param voucherMap 申请单详情
	 * @return
	 */
	public OpTip stockCheck(String cabinetId, Map<String, List<CartInfo>> voucherMap);

	/**
	 * @description 发送申请单到服务端
	 * @param cabinetId  刀具柜Id
	 * @param voucherMap 申请单集合
	 * @param accountId  帐户Id
	 * @return
	 */
	public List<ExtraCabinet> sendApplyVoucherToServer(String cabinetId, Map<String, List<CartInfo>> voucherMap,
			String shareSwitch, String accountId);
}