package com.dosth.tool.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.dosth.tool.service.AppCabinetService;

/**
 * APPController
 * 
 * @author liweifeng
 *
 */
@RestController
@RequestMapping("/app")
public class AppCabinetServiceController {
	
	@Autowired
	private AppCabinetService appCabinetService;

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
	@RequestMapping("/cupboard/list")
	public List<AppCabinet> getCabinetList(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("multiple") int multiple, @RequestParam("regionId") String regionId,
			@RequestParam("subscribe") int subscribe, @RequestParam("inventory") int inventory) {
		Map<String, String> page = new HashMap<>();
		page.put("pageNo", String.valueOf(pageNo));
		page.put("pageSize", String.valueOf(pageSize));
		Map<String, String> params = new HashMap<>();		
		params.put("inventory", String.valueOf(inventory));
		List<AppCabinet> appCabinetList = this.appCabinetService.getAppCabinetList(page, params);
		return appCabinetList;
	}

	/**
	 * @description 4、机柜搜索
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param keyWord 查询字符串
	 * @param knifeId 刀具Id
	 * @return
	 */
	@RequestMapping("/cupboard/search")
	public List<AppCabinet> search(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("keyWord") String keyWord, @RequestParam("knifeId") String knifeId) {
		Map<String, String> page = new HashMap<>();
		page.put("pageNo", String.valueOf(pageNo));
		page.put("pageSize", String.valueOf(pageSize));
		Map<String, String> params = new HashMap<>();		
		params.put("keyword", keyWord);		
		params.put("knifeId", knifeId);
		List<AppCabinet> appCabinetList = this.appCabinetService.getAppCabinetListByKnifes(page, params);
		return appCabinetList;
	}

	/**
	 * @description 5、刀具列表
	 * @param cupboardId 刀具柜Id
	 * @return
	 */
	@RequestMapping("/knife/list")
	public Map<String, List<AppKnifes>> getAppKnivesListByCupboardId(@RequestParam("cupboardId") String cupboardId) {
		Map<String, List<AppKnifes>> appKnivesMap = this.appCabinetService.getAppKnifesListByCupboardId(cupboardId);
		return appKnivesMap;
	}

	/**
	 * @description 6、刀具预约信息
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param knifeId 刀具Id
	 * @return
	 */
	@RequestMapping("/knife/subscribe")
	public List<AppSubscribe> getAppSubscribeList(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize, @RequestParam("knifeId") String knifeId) {
		Map<String, String> page = new HashMap<>();
		page.put("pageNo", String.valueOf(pageNo));
		page.put("pageSize", String.valueOf(pageSize));
		List<AppSubscribe> appSubscribeList = this.appCabinetService.getAppSubscribeList(page, knifeId);
		return appSubscribeList;
	}

	/**
	 * @description 7、刀具搜索
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param keywords 刀具查询字符串
	 * @param cupboardId 机柜Id
	 * @return
	 */
	@RequestMapping("/knife/search")
	public List<AppKnifes> getAppKnives(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("keywords") String keywords, @RequestParam("cupboardId") String cupboardId) {
		Map<String, String> page = new HashMap<>();
		page.put("pageNo", String.valueOf(pageNo));
		page.put("pageSize", String.valueOf(pageSize));
		Map<String, String> params = new HashMap<>();		
		params.put("keywords", keywords);		
		params.put("cupboardId", cupboardId);
		List<AppKnifes> appKnivesList = this.appCabinetService.getAppKnifes(page, params);
		return appKnivesList;
	}

	/**
	 * @description 8、获取刀具类型集合
	 * @return
	 */
	@RequestMapping("/kind/list")
	public List<AppMatInfoType> getAppTypeList() {
		List<AppMatInfoType> appMatInfoTypeList = this.appCabinetService.getAppTypeList();
		return appMatInfoTypeList;
	}

	/**
	 * @description 9、添加到购物车
	 * @param userId        用户Id
	 * @param knifeId       刀具Id
	 * @param shoppingTotal 购物车数量
	 * @return
	 */
	@RequestMapping("/shopping/insert")
	public ResponseTip shoppingInsert(@RequestParam("userId") String userId, @RequestParam("knifeId") String knifeId,
			@RequestParam("shoppingTotal") int shoppingTotal) {
		ResponseTip tip = new ResponseTip("添加成功");
		this.appCabinetService.shoppingInsert(userId, knifeId, shoppingTotal);
		return tip;
	}

	/**
	 * @description 10、购物车移除
	 * @param shoppingId 购物车项
	 * @return
	 */
	@RequestMapping("/shopping/delete")
	public ResponseTip shoppingDelete(@RequestParam("shoppingId") String shoppingId) {
		ResponseTip tip = new ResponseTip("移除成功");
		this.appCabinetService.shoppingDelete(shoppingId);
		return tip;
	}

	/**
	 * @description 11、获取购物车列表
	 * @param userId 用户Id
	 * @return
	 */
	@RequestMapping("/shopping/list")
	public List<AppCart> shoppingList(@RequestParam("userId") String userId) {
		return this.appCabinetService.getShoppingList(userId);
	}

	/**
	 * @description 12、购物车信息更新
	 * @param shoppingId   购物车项
	 * @param shopingTotal 数量
	 * @return
	 */
	@RequestMapping("/shopping/update")
	public ResponseTip shoppingUpdate(@RequestParam("shoppingId") String shoppingId,
			@RequestParam("shopingTotal") int shopingTotal) {
		ResponseTip tip = new ResponseTip("更新成功");
		this.appCabinetService.shoppingUpdate(shoppingId, shopingTotal);
		return tip;
	}

	/**
	 * @description 13、订单查询
	 * @param pageNo    当前页码
	 * @param pageSize  每页大小
	 * @param userId    用户Id
	 * @param orderType 订单类型, 0待使用;1 已使用;-1 已失效
	 * @return
	 */
	@RequestMapping("/order/search")
	public List<AppOrder> searchAppOrder(@RequestParam("pageNo") int pageNo, @RequestParam("pageSize") int pageSize,
			@RequestParam("userId") String userId, @RequestParam("orderType") String orderType) {
		Map<String, String> page = new HashMap<>();
		page.put("pageNo", String.valueOf(pageNo));
		page.put("pageSize", String.valueOf(pageSize));
		Map<String, String> params = new HashMap<>();		
		params.put("userId", userId);		
		params.put("orderType", orderType);
		List<AppOrder> orderList = this.appCabinetService.searchAppOrder(page, params);
		return orderList;
	}

	/**
	 * @description 14、再次预约
	 * @param orderId 订单编号
	 * @return
	 */
	@RequestMapping("/purchase/update")
	public List<AppOrderItem> reOrderAppOverdueOrder(@RequestParam String orderId) {
		List<AppOrderItem> appOrderItemList = this.appCabinetService.reOderAppOverdueOrder(orderId);
		return appOrderItemList;
	}

	/**
	 * @description 15、根据类型获取刀具列表
	 * @param kindId 类型Id
	 * @return
	 */
	@RequestMapping("/knife/kind_list")
	public List<AppKnifes> getAppKnifesByKind(@RequestParam("kindId") String kindId) {
		return this.appCabinetService.getAppKnifesByKind(kindId);
	}
	
	/**
	 * 
	 * @description 20、预约
	 * @param shopping 选中的购物车集合
	 * @param accountId 帐户Id
	 */
	@RequestMapping(value = "/shopping/submit_shopping")
	public ResponseTip shoppingSubmit(@RequestBody AppShopping shopping, @RequestParam String accountId) {
		ResponseTip tip = this.appCabinetService.shoppingSubmit(shopping, accountId);
		return tip;
	}
	

	/**
	 * @description 21、取消预约
	 * @param purchaseId 预约项Id
	 * @return
	 */
	@RequestMapping(value = "/purchase/remove")
	public ResponseTip removePurchase(@RequestParam String purchaseId) {
		ResponseTip tip = new ResponseTip("取消成功");
		try {
			this.appCabinetService.removePurchase(purchaseId);
		} catch (Exception e) {
			tip = new ResponseTip(201, "取消失败");
			e.printStackTrace();
		}
		return tip;
	}

	/**
	 * @description 22、取消订单
	 * @param orderId 订单Id
	 * @return
	 */
	@RequestMapping(value = "/order/remove")
	public ResponseTip removeOrder(@RequestParam String orderId) {
		ResponseTip tip = new ResponseTip("取消成功");
		try {
			this.appCabinetService.removeOrder(orderId);
		} catch (Exception e) {
			tip = new ResponseTip(201, "取消失败");
			e.printStackTrace();
		}
		return tip;
	}
	
	/**
	 * @description 归还信息审核
	 * @param 
	 * @return
	 */
	@RequestMapping("/recycle/code_info")
	public AppRecycleReview recycleInfo(@RequestParam("code") String code) {
		return this.appCabinetService.recycleInfo(code);
	}
	
	/**
	 * @description 审核不通过
	 * @param 
	 * @return
	 */
	@RequestMapping("/recycle/review_reject")
	public AppRecycleResult recycleReject(@RequestParam("code") String code, @RequestParam("type") String type,
			@RequestParam("num") int num, @RequestParam("content") String content, @RequestParam("userId") String userId) {
		return this.appCabinetService.recycleReject(code, type, num, content, userId);
	}
	
	/**
	 * @description 审核通过
	 * @param 
	 * @return
	 */
	@RequestMapping("/recycle/review_pass")
	public AppRecycleResult recyclePass(@RequestParam("code") String code, @RequestParam("userId") String userId) {
		return this.appCabinetService.recyclePass(code, userId);
	}
}