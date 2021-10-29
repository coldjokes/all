package com.dosth.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
 * @description 工具访问远程接口
 * 
 * @author guozhidong
 *
 */
@FeignClient("service-tool")
public interface ToolAppService {

	/**
	 * @description 3、获取柜子列表数据
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param multiple 综合排序
	 * @param regionId 区域Id
	 * @param subscribe 预约量
	 * @param inventory 库存
	 * @return
	 */
	@RequestMapping("/app/cupboard/list")
	public List<AppCabinet> getCabinetList(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("multiple") int multiple, @RequestParam("regionId") String regionId,
			@RequestParam("subscribe") int subscribe, @RequestParam("inventory") int inventory);

	/**
	 * @description 4、机柜搜索
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param keyWord 查询字符串
	 * @param knifeId 刀具Id
	 * @return
	 */
	@RequestMapping("/app/cupboard/search")
	public List<AppCabinet> search(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("keyWord") String keyWord, @RequestParam("knifeId") String knifeId);

	/**
	 * @description 5、刀具列表
	 * @param cupboardId 刀具柜Id
	 * @return
	 */
	@RequestMapping("/app/knife/list")
	public Map<String, List<AppKnifes>> getAppKnivesListByCupboardId(@RequestParam("cupboardId") String cupboardId);

	/**
	 * @description 6、刀具预约信息
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param knifeId 刀具Id
	 * @return
	 */
	@RequestMapping("/app/knife/subscribe")
	public List<AppSubscribe> getAppSubscribeList(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize, @RequestParam("knifeId") String knifeId);

	/**
	 * @description 7、刀具搜索
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param keywords 刀具查询字符串
	 * @param cupboardId 机柜Id
	 * @return
	 */
	@RequestMapping("/app/knife/search")
	public List<AppKnifes> getAppKnives(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("keywords") String keywords, @RequestParam("cupboardId") String cupboardId);

	/**
	 * @description 8、获取刀具类型集合
	 * @return
	 */
	@RequestMapping("/app/kind/list")
	public List<AppMatInfoType> getAppTypeList();

	/**
	 * @description 9、添加到购物车
	 * @param userId        用户Id
	 * @param knifeId       刀具Id
	 * @param shoppingTotal 购物车数量
	 * @return
	 */
	@RequestMapping(value = "/app/shopping/insert", method = RequestMethod.GET)
	public ResponseTip shoppingInsert(@RequestParam("userId") String userId, @RequestParam("knifeId") String knifeId,
			@RequestParam("shoppingTotal") int shoppingTotal);

	/**
	 * @description 10、购物车移除
	 * @param shoppingId 购物车项
	 * @return
	 */
	@RequestMapping("/app/shopping/delete")
	public ResponseTip shoppingDelete(@RequestParam("shoppingId") String shoppingId);

	/**
	 * @description 11、获取购物车列表
	 * @param userId 用户Id
	 * @return
	 */
	@RequestMapping("/app/shopping/list")
	public List<AppCart> shoppingList(@RequestParam("userId") String userId);

	/**
	 * @description 12、购物车信息更新
	 * @param shoppingId   购物车项
	 * @param shopingTotal 数量
	 * @return
	 */
	@RequestMapping("/app/shopping/update")
	public ResponseTip shoppingUpdate(@RequestParam("shoppingId") String shoppingId,
			@RequestParam("shopingTotal") int shopingTotal);
	
	/**
	 * @description 13、订单查询
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param userId    用户Id
	 * @param orderType 订单类型, 0待使用;1 已使用;-1 已失效
	 * @return
	 */
	@RequestMapping("/app/order/search")
	public List<AppOrder> searchAppOrder(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("userId") String userId, @RequestParam("orderType") String orderType);

	/**
	 * @description 14、再次预约
	 * @param orderId 订单编号
	 * @return
	 */
	@RequestMapping("/app/purchase/update")
	public List<AppOrderItem> reOrderAppOverdueOrder(@RequestParam("orderId") String orderId);

	/**
	 * @description 15、根据类型获取刀具列表
	 * @param kindId 类型Id
	 * @return
	 */
	@RequestMapping("/app/knife/kind_list")
	public List<AppKnifes> getAppKnifesByKind(@RequestParam("kindId") String kindId);
	
	/**
	 * @description 20、预约
	 * @param shopping 选中的购物车集合
	 * @param accountId 帐户Id
	 */
	@RequestMapping(value = "/app/shopping/submit_shopping")
	public ResponseTip shoppingSubmit(@RequestBody AppShopping shopping, @RequestParam("accountId") String accountId);

	/**
	 * @description 21、取消预约
	 * @param purchaseId 预约项Id
	 * @return
	 */
	@RequestMapping(value = "/app/purchase/remove")
	public ResponseTip removePurchase(@RequestParam("purchaseId") String purchaseId);

	/**
	 * @description 22、取消订单
	 * @param orderId 订单Id
	 * @return
	 */
	@RequestMapping(value = "/app/order/remove")
	public ResponseTip removeOrder(@RequestParam("orderId") String orderId);
	
	/**
	 * @description 归还信息审核
	 * @param 
	 * @return
	 */
	@RequestMapping("/app/recycle/code_info")
	public AppRecycleReview recycleInfo(@RequestParam("code") String code);
	
	/**
	 * @description 审核不通过
	 * @param 
	 * @return
	 */
	@RequestMapping("/app/recycle/review_reject")
	public AppRecycleResult recycleReject(@RequestParam("code") String code, @RequestParam("type") String type,
			@RequestParam("num") int num, @RequestParam("content") String content, @RequestParam("userId") String userId);
	
	/**
	 * @description 审核通过
	 * @param 
	 * @return
	 */
	@RequestMapping("/app/recycle/review_pass")
	public AppRecycleResult recyclePass(@RequestParam("code") String code, @RequestParam("userId") String userId);
	
}