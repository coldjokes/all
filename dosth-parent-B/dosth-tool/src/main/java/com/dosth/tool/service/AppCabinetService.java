package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import com.dosth.app.dto.AppCabinet;
import com.dosth.app.dto.AppCart;
import com.dosth.app.dto.AppKnifes;
import com.dosth.app.dto.AppMatInfoType;
import com.dosth.app.dto.AppOrder;
import com.dosth.app.dto.AppOrderItem;
import com.dosth.app.dto.AppRecycleResult;
import com.dosth.app.dto.AppRecycleReview;
import com.dosth.app.dto.AppShopping;
import com.dosth.app.dto.AppSubscribe;
import com.dosth.remote.ResponseTip;

/**
 * 
 * @description 手机App请求Service
 * @author liweifeng,xutianpeng,guozhidong
 *
 */
public interface AppCabinetService {

	/**
	 * 查询所有主柜
	 * 
	 * @param page
	 * @param params
	 * @return
	 */
	public List<AppCabinet> getAppCabinetList(Map<String, String> page, Map<String, String> params);

	/**
	 * 按指定刀具查询所有主柜
	 * 
	 * @param page
	 * @param params
	 * @return
	 */
	public List<AppCabinet> getAppCabinetListByKnifes(Map<String, String> page, Map<String, String> params);

	/**
	 * 按指定主柜查询所有刀具
	 * 
	 * @param cupboardId
	 * @return
	 */
	public Map<String, List<AppKnifes>> getAppKnifesListByCupboardId(String cupboardId);

	/**
	 * 按刀具查询预约信息
	 * 
	 * @param page
	 * @param knifeId
	 * @return
	 */
	public List<AppSubscribe> getAppSubscribeList(Map<String, String> page, String knifeId);

	/**
	 * 按指定主柜指定刀具查询刀具信息
	 * 
	 * @param page
	 * @param params
	 * @return
	 */
	public List<AppKnifes> getAppKnifes(Map<String, String> page, Map<String, String> params);

	/**
	 * 按类型查询刀具
	 * 
	 * @return
	 */
	public List<AppMatInfoType> getAppTypeList();

	/**
	 * @description 订单查询
	 * @param page   分页信息
	 * @param params 请求参数
	 * @return
	 */
	public List<AppOrder> searchAppOrder(Map<String, String> page, Map<String, String> params);

	/**
	 * @description 再次预约
	 * @param orderId 订单编号
	 * @return
	 */
	public List<AppOrderItem> reOderAppOverdueOrder(String orderId);

	/**
	 * @description 购物车添加
	 * @param userId 用户Id
	 * @param knifeId 刀具Id
	 * @param shoppingTotal 数量
	 */
	public void shoppingInsert(String userId, String knifeId, int shoppingTotal);

	/**
	 * @description 购物车移除
	 * @param shoppingId 购物车项Id
	 */
	public void shoppingDelete(String shoppingId);

	/**
	 * @description 根据刀具类型获取刀具列表信息
	 * @param kindId 类型Id
	 * @return
	 */
	public List<AppKnifes> getAppKnifesByKind(String kindId);

	/**
	 * @description 购物车信息更新
	 * @param shoppingId 购物车项Id
	 * @param shopingTotal 购物数量
	 */
	public void shoppingUpdate(String shoppingId, int shopingTotal);

	/**
	 * @description 获取购物车列表
	 * @param userId 用户Id
	 * @return
	 */
	public List<AppCart> getShoppingList(String userId);

	/**
	 * @description 预约
	 * @param shopping 选中的购物车集合
	 * @param accountId 帐户Id
	 * @return
	 */
	public ResponseTip shoppingSubmit(AppShopping shopping, String accountId);

	/**
	 * @description 移除订单项
	 * @param purchaseId 订单项Id
	 */
	public void removePurchase(String purchaseId);

	/**
	 * @description 移除订单
	 * @param orderId 订单Id
	 */
	public void removeOrder(String orderId);
	
	/**
	 * @description 归还信息审核
	 * @param code 二维码
	 */
	public AppRecycleReview recycleInfo(String code);
	
	/**
	 * @description 审核不通过
	 * @param  code 二维码信息
     * @param  type 类型 0:与事实不符 1:确定数量 2:其他
     * @param  num 确定数量
     * @param  content 备注
	 */
	public AppRecycleResult recycleReject(String code, String type, int num, String contect, String userId);
	
	/**
	 * @description 审核通过
	 * @param code 二维码
	 */
	public AppRecycleResult recyclePass(String code, String userId);
}