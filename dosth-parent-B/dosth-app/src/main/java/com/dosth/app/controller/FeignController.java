package com.dosth.app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dosth.app.dto.AppAdvice;
import com.dosth.app.dto.AppCabinet;
import com.dosth.app.dto.AppCard;
import com.dosth.app.dto.AppCart;
import com.dosth.app.dto.AppCloseDoor;
import com.dosth.app.dto.AppHelper;
import com.dosth.app.dto.AppKnifes;
import com.dosth.app.dto.AppLogin;
import com.dosth.app.dto.AppMatInfoType;
import com.dosth.app.dto.AppOrder;
import com.dosth.app.dto.AppOrderItem;
import com.dosth.app.dto.AppRecycleInput;
import com.dosth.app.dto.AppRecycleResult;
import com.dosth.app.dto.AppRecycleReview;
import com.dosth.app.dto.AppShopping;
import com.dosth.app.dto.AppSubscribe;
import com.dosth.app.dto.AppToken;
import com.dosth.app.dto.AppUser;
import com.dosth.app.dto.OrderSearchCondition;
import com.dosth.app.dto.SessionUser;
import com.dosth.app.service.AdminAppService;
import com.dosth.app.service.CabinetAppService;
import com.dosth.app.service.ToolAppService;
import com.dosth.constant.Constants;
import com.dosth.remote.ResponseTip;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * @description App请求Controller
 * @author guozhidong
 *
 */
@RestController
@RequestMapping("/app")
public class FeignController {
	
	private static final Logger logger = LoggerFactory.getLogger(FeignController.class);
	
	@Autowired
	private AdminAppService adminAppService;
	@Autowired
	private ToolAppService toolAppService;
	@Autowired
	private CabinetAppService cabinetAppService;
	
	/**
	 * @description app登录
	 * @param username 用户名
	 * @param userPassword 用户密码
	 * @param request
	 * @return
	 */
	@ApiOperation(value = "1、app登录")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "login", value = "用户登录信息", paramType = "body", required = true, dataType = "AppLogin") })
	@RequestMapping(value = "/user/login", method = RequestMethod.POST)
	public AppToken login(@RequestBody AppLogin login, HttpServletRequest request) {
		logger.info("app登录");
		AppToken appToken = new AppToken(); 
		String tokenId = request.getSession().getId();
		AppUser appUser = this.adminAppService.getCacheAppUser(tokenId);
		if (appUser == null) {
			appUser = this.adminAppService.checkAppUser(login.getUserName(), login.getUserPassword());
		//	this.adminAppService.putCacheAppUser(tokenId, appUser);
		}
		appToken.setToken(tokenId);
		appToken.setUser(appUser);
		request.getSession().setAttribute("user", appUser);
		return appToken;
	}
	
//	/**
//	 * @description 用户注册
//	 * @param user 用户注册信息
//	 * @return
//	 */
//	@ApiOperation(value = "17、用户注册")
//	@ApiImplicitParam(name = "user", value = "用户注册信息", paramType = "body", required = true, dataType = "AppUser")
//	@RequestMapping(value = "/user/register", method = RequestMethod.POST)
//	public int userRegister(@RequestBody AppUser user) {
//		logger.info("用户注册");
//		try {
//			ResponseTip tip = this.adminAppService.userRegister(user);
//			if (tip.getCode() == Constants.OP_SUCCESS) {
//				return 1;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0;
//	}

	/**
	 * @description 问题反馈
	 * @param advice 
	 * @return
	 */
	@ApiOperation(value = "2、问题反馈")
	@ApiImplicitParam(name = "advice", value = "问题反馈信息", paramType = "body", required = true, dataType = "AppAdvice")
	@RequestMapping(value = "/advice/insert", method = RequestMethod.POST)
	public int adviceInsert(@RequestBody AppAdvice advice) {
		logger.info("问题反馈");
		try {
			ResponseTip tip = this.adminAppService.insertAdvice(advice);
			if (tip.getCode() == Constants.OP_SUCCESS) {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @description 柜机列表
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param multiple 综合排序
	 * @param regionId 区域Id
	 * @param subscribe 预约量
	 * @param inventory 库存
	 * @return
	 */
	@ApiOperation(value = "3、柜机列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "当前页码", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "multiple", value = "综合排序，1 正序，0 倒序", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "regionId", value = "区域Id", paramType = "query", required = true, dataType = "String"),
			@ApiImplicitParam(name = "subscribe", value = "预约量, 1 正序，0 倒序", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "inventory", value = "库存, 1 正序，0 倒序", paramType = "query", required = true, dataType = "int") })
	@RequestMapping(value = "/cupboard/list", method = RequestMethod.POST)
	public List<AppCabinet> getCabinetList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "multiple", defaultValue = "1") int multiple,
			@RequestParam(value = "regionId", defaultValue = "1") String regionId,
			@RequestParam(value = "subscribe", defaultValue = "1") int subscribe,
			@RequestParam(value = "inventory", defaultValue = "1") int inventory) {
		logger.info("机柜列表");
		return this.toolAppService.getCabinetList(pageNo, pageSize, multiple, regionId, subscribe, inventory);
	}

	/**
	 * @description 机柜搜索
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param keyWord 查询字符串
	 * @param knifeId 刀具Id,可以为空，如果不为空那么就查询有这个刀具的所有机柜
	 * @return
	 */
	@ApiOperation(value = "4、柜机搜索")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "当前页码", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "keyWord", value = "查询字符串", paramType = "query", dataType = "String"),
			@ApiImplicitParam(name = "knifeId", value = "刀具Id,可以为空，如果不为空那么就查询有这个刀具的所有机柜", paramType = "query", required = false, dataType = "String") })
	@RequestMapping(value = "/cupboard/search", method = RequestMethod.POST)
	public List<AppCabinet> search(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "keyWord", defaultValue = "1") String keyWord,
			@RequestParam(value = "knifeId", defaultValue = "1") String knifeId) {
		logger.info("机柜搜索");
		return this.toolAppService.search(pageNo, pageSize, keyWord, knifeId);
	}

	/**
	 * @description 按指定主柜查询所有刀具
	 * 
	 * @param cupboardId 刀具柜Id
	 * @return
	 */
	@ApiOperation(value = "5、刀具列表")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cabinet", value = "机柜信息", paramType = "body", required = true, dataType = "AppCabinet") })
	@RequestMapping(value = "/knife/list", method = RequestMethod.POST)
	public List<AppCard> getAppKnivesListByCupboardId(@RequestBody AppCabinet cabinet) {
		logger.info("刀具列表");
		Map<String, List<AppKnifes>> map = this.toolAppService.getAppKnivesListByCupboardId(cabinet.getCupboardId());
		List<AppCard> cardList = new ArrayList<>();
		AppCard card;
		List<AppKnifes> knifeList;
		for (Entry<String, List<AppKnifes>> entry : map.entrySet()) {
			card = new AppCard(entry.getKey());
			knifeList = entry.getValue();
			for (AppKnifes knife : knifeList) {
				card.getKnives().add(knife);
			}
			cardList.add(card);
		}
		Collections.sort(cardList, new Comparator<AppCard>() {
			@Override
			public int compare(AppCard o1, AppCard o2) {
				return 1;
			}
		});
		return cardList;
	}

	/**
	 * 6、按刀具查询预约信息
	 * 
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param knifeId 刀具Id
	 * @return
	 */
	@ApiOperation(value = "6、刀具预约信息")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "当前页码", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "knifeId", value = "刀具Id", paramType = "query", required = true, dataType = "String") })
	@RequestMapping(value = "/knife/subscribe", method = RequestMethod.POST)
	public List<AppSubscribe> getAppSubscribeList(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "knifeId", required = false) String knifeId) {
		logger.info("6、刀具预约信息");
		return this.toolAppService.getAppSubscribeList(pageNo, pageSize, knifeId);
	}

	/**
	 * @description 7、按指定主柜指定刀具查询刀具信息
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param keywords 刀具查询字符串
	 * @param cupboardId 机柜Id
	 * @return
	 */
	@ApiOperation(value = "7、刀具搜索")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageNo", value = "当前页码", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "pageSize", value = "每页大小", paramType = "query", required = true, dataType = "int"),
			@ApiImplicitParam(name = "keywords", value = "刀具查询字符串", paramType = "query", required = false, dataType = "String"),
			@ApiImplicitParam(name = "cupboardId", value = "机柜Id", paramType = "query", required = false, dataType = "String") })
	@RequestMapping(value = "/knife/search", method = RequestMethod.POST)
	public List<AppKnifes> getAppKnives(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "keywords", required = false) String keywords,
			@RequestParam(value = "cupboardId", required = false) String cupboardId) {
		logger.info("7、刀具搜索");
		return this.toolAppService.getAppKnives(pageNo, pageSize, keywords, cupboardId);
	}

	/**
	 * 8、按类型查询刀具
	 * 
	 * @return
	 */
	@ApiOperation(value = "8、分类类型列表")
	@ApiResponses({ @ApiResponse(code = 400, message = "请求参数没填好"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/kind/list", method = RequestMethod.POST)
	public List<AppMatInfoType> getAppTypeList() {
		logger.info("8、分类类型列表");
		return this.toolAppService.getAppTypeList();
	}

	/**
	 * @description 购物车添加
	 * @param userId        用户Id
	 * @param knifeId       物料Id
	 * @param shoppingTotal 添加数量
	 * @return
	 */
	@ApiOperation(value = "9、购物车添加")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "knife", value = "刀具信息", paramType = "body", required = true, dataType = "AppKnifes")})
	@RequestMapping(value = "/shopping/insert", method = RequestMethod.POST)
	public int shoppingInsert(@RequestBody AppKnifes knife, HttpServletRequest request) {
		logger.info("9、购物车添加");
		try {
			AppUser user = (AppUser) request.getSession().getAttribute("user");
			ResponseTip tip = this.toolAppService.shoppingInsert(user.getUserId(), knife.getKnifeId(), knife.getCounter());
			if (tip.getCode() == Constants.OP_SUCCESS) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("9、购物车添加:" + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	@ApiOperation(value = "10、购物车删除")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cart", value = "购物车项", paramType = "body", required = true, dataType = "AppCart") })
	@RequestMapping(value = "/shopping/delete", method = RequestMethod.POST)
	public int shoppingDelete(@RequestBody AppCart cart) {
		logger.info("10、购物车删除");
		try {
			ResponseTip tip = this.toolAppService.shoppingDelete(cart.getShoppingId());
			if (tip.getCode() == Constants.OP_SUCCESS) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("10、购物车删除:" + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	@ApiOperation(value = "11、购物车列表")
	@RequestMapping(value = "/shopping/list", method = RequestMethod.POST)
	public List<AppCart> shoppingList(HttpServletRequest request) {
		logger.info("11、购物车列表");
		try {
			AppUser user = (AppUser) request.getSession().getAttribute("user");
			return this.toolAppService.shoppingList(user.getUserId());
		} catch (Exception e) {
			logger.error("11、购物车列表:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@ApiOperation(value = "12、购物车信息更新")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "cart", value = "购物车信息更新", paramType = "body", required = true, dataType = "AppCart") })
	@RequestMapping(value = "/shopping/update", method = RequestMethod.POST)
	public int shoppingUpdate(@RequestBody AppCart cart) {
		logger.info("12、购物车信息更新");
		try {
			ResponseTip tip = this.toolAppService.shoppingUpdate(cart.getShoppingId(), cart.getShoppingTotal());
			if (tip.getCode() == Constants.OP_SUCCESS) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("12、购物车信息更新:" + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @description 13 订单查询
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @param userId 用户Id
	 * @param orderType 订单类型, 0待使用;1 已使用;-1 已失效
	 * @return
	 */
	// 订单类型, 0待使用;1 已使用;-1 已失效
	@ApiOperation(value = "13、订单查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "condition", value = "订单查询条件", paramType = "body", required = true, dataType = "OrderSearchCondition") })
	@RequestMapping(value = "/order/search", method = RequestMethod.POST)
	public List<AppOrder> searchAppOrder(@RequestBody OrderSearchCondition condition, HttpServletRequest request) {
		logger.info("13、订单查询");
		try {
			AppUser user = (AppUser) request.getSession().getAttribute("user");
			List<AppOrder> list = this.toolAppService.searchAppOrder(condition.getPage().getPageNo(), 
					condition.getPage().getPageSize(), user.getUserId(), condition.getParams().getOrderType());
			return list;
		} catch (Exception e) {
			logger.error("13、订单查询:" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 再次预约
	 * @param purchaseId 订单编号
	 * @return
	 */
	@ApiOperation(value = "14、再次预约")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "order", value = "订单", paramType = "body", required = true, dataType = "AppOrder") })
	@RequestMapping(value = "/purchase/update", method = RequestMethod.POST)
	public List<AppOrderItem> reOrderAppOverdueOrder(@RequestBody AppOrder order) {
		logger.info("14、再次预约");
		return this.toolAppService.reOrderAppOverdueOrder(order.getOrderId());
	}
	
	/**
	 * @description 分类查询
	 * @param kindId 类型Id
	 * @return
	 */
	@ApiOperation(value = "15、分类查询")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "类型", paramType = "body", required = true, dataType = "AppMatInfoType") })
	@RequestMapping(value = "/knife/kind_list", method = RequestMethod.POST)
	public List<AppKnifes> getAppKnifesByKind(@RequestBody AppMatInfoType type) {
		logger.info("15、分类查询");
		return this.toolAppService.getAppKnifesByKind(type.getKindId());
	}
	
	/**
	 * @description 帮助中心
	 * @return
	 */
	@ApiOperation(value = "16、帮助中心")
	@RequestMapping(value = "/help/search", method = RequestMethod.POST)
	public List<AppHelper> getAppHelperList() {
		logger.info("16、帮助中心");
		return this.adminAppService.getAppHelperList();
	}
	
	/**
	 * @description 用户信息校验
	 * @return
	 */
	@ApiOperation(value = "19、用户信息校验")
	@RequestMapping(value = "/user/info", method = RequestMethod.POST)
	public SessionUser getUserInfo(HttpServletRequest request) {
		logger.info("19、用户信息校验");
		SessionUser user = new SessionUser();
		AppUser appUser = (AppUser) request.getSession().getAttribute("user");
		user.setUser(appUser);
		request.getSession().setAttribute("user", appUser);
		return user;
	}

	
	/**
	 * @description 预约
	 * @param shopping 订单集合
	 * @return
	 */
	@ApiOperation(value = "20、预约")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "shopping", value = "预约购物车集合", paramType = "body", required = true, dataType = "AppShopping") })
	@RequestMapping(value = "/shopping/submit_shopping", method = RequestMethod.POST)
	public int shoppingSubmit(@RequestBody AppShopping shopping, HttpServletRequest request) {
		logger.info("20、预约");
		try {
			AppUser user = (AppUser) request.getSession().getAttribute("user");
			this.toolAppService.shoppingSubmit(shopping, user.getUserId());
		} catch (Exception e) {
			logger.error("20、预约错误:" + e.getMessage());
			e.printStackTrace();
		}
		return 1;
	}
	
	@ApiOperation(value = "21、取消预约单项")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "item", value = "订单项", paramType = "body", required = true, dataType = "AppOrderItem") })
	@RequestMapping(value = "/purchase/remove", method = RequestMethod.POST)
	public int purchaseRemove(@RequestBody AppOrderItem item) {
		logger.info("21、取消预约单项");
		try {
			ResponseTip tip = this.toolAppService.removePurchase(item.getPurchaseId());
			if (tip.getCode() == Constants.OP_SUCCESS) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("21、取消预约单项错误:" + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	
	@ApiOperation(value = "22、取消预约订单")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "order", value = "订单", paramType = "body", required = true, dataType = "AppOrder") })
	@RequestMapping(value = "/order/remove", method = RequestMethod.POST)
	public int orderRemove(@RequestBody AppOrder order) {
		logger.info("22、取消预约订单");
		try {
			ResponseTip tip = this.toolAppService.removeOrder(order.getOrderId());
			if (tip.getCode() == Constants.OP_SUCCESS) {
				return 1;
			}
		} catch (Exception e) {
			logger.error("22、取消预约订单错误:" + e.getMessage());
			e.printStackTrace();
		}
		return 0;
	}
	

	/**
	 * @description 23、归还信息审核
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "23、归还信息审核")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "input", value = "入参信息", paramType = "body", required = true, dataType = "AppRecycleInput") })
	@RequestMapping(value = "/recycle/code_info", method = RequestMethod.POST)
	public AppRecycleReview recycleInfo(@RequestBody AppRecycleInput input, HttpServletRequest request) {
		return this.toolAppService.recycleInfo(input.getCode());
	}
	
	/**
	 * @description 24、审核不通过
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "24、审核不通过")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "input", value = "入参信息", paramType = "body", required = true, dataType = "AppRecycleInput") })
	@RequestMapping(value = "/recycle/review_reject", method = RequestMethod.POST)
	public AppRecycleResult recycleReject(@RequestBody AppRecycleInput input, HttpServletRequest request) {
		AppUser user = (AppUser) request.getSession().getAttribute("user");
		return this.toolAppService.recycleReject(input.getCode(), input.getType(), input.getNum(), input.getContent(), user.getUserId());
	}
	
	/**
	 * @description 25、审核通过
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "25、审核通过")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "input", value = "入参信息", paramType = "body", required = true, dataType = "AppRecycleInput") })
	@RequestMapping(value = "/recycle/review_pass", method = RequestMethod.POST)
	public AppRecycleResult recyclePass(@RequestBody AppRecycleInput input, HttpServletRequest request) {
		AppUser user = (AppUser) request.getSession().getAttribute("user");
		return this.toolAppService.recyclePass(input.getCode(), user.getUserId());
	}
	
	/**
	 * @description 26、关门信号
	 * @param 
	 * @return
	 */
	@ApiOperation(value = "26、关门信号")
	@ApiImplicitParams({
	@ApiImplicitParam(name = "input", value = "入参信息", paramType = "body", required = true, dataType = "AppCloseDoor") })
	@RequestMapping(value = "/user/send_cmd", method = RequestMethod.POST)
	public String closeDoor(@RequestBody AppCloseDoor input) {
		return this.cabinetAppService.closeDoor(input.getCmd());
	}

}