package com.dosth.toolcabinet.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.comm.node.ZTreeNode;
import com.dosth.dto.ExtraCabinet;
import com.dosth.dto.Lattice;
import com.dosth.enums.CabinetType;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.config.CabinetConfig;
import com.dosth.toolcabinet.dto.CartInfo;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.toolcabinet.util.BaseController;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 * @description 购物车Controller
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/cart")
public class CartController extends BaseController {

	@Autowired
	private CabinetConfig cabinetConfig;
	@Autowired
	private ToolService toolService;

	/**
	 * @description 跳转到 暂存/归还 页面
	 * @return
	 */
	@RequestMapping("/goToBorrandstor")
	public String borrandstor(Model model) {
		List<ZTreeNode> list = new ArrayList<>();
		list.add(new ZTreeNode("history", "", "待归还物料"));

		// 过滤暂存柜
		List<ExtraCabinet> cabinetTreeList = this.toolService
				.getCabinetTreeList(DosthToolcabinetRunnerInit.mainCabinetId);
		List<ExtraCabinet> temCabinet = cabinetTreeList.stream()
				.filter(cabinetInfo -> cabinetInfo.getCabinetType().equals(CabinetType.TEM_CABINET.name()))
				.collect(Collectors.toList());

		if (temCabinet != null && temCabinet.size() > 0) {
			list.add(new ZTreeNode("subcabinet", "", "我的暂存柜"));
		}
		model.addAttribute("hasTempCabinet", temCabinet != null && temCabinet.size() > 0);

		List<ExtraCabinet> recCabinet = cabinetTreeList.stream()
				.filter(cabinetInfo -> cabinetInfo.getCabinetType().equals(CabinetType.RECOVERY_CABINET.name()))
				.collect(Collectors.toList());
		if (recCabinet != null && recCabinet.size() > 0) {
			for (ExtraCabinet rec : recCabinet) {
				String recCom = DosthToolcabinetRunnerInit.getCabinetParam(rec.getCabinetId(),
						SetupKey.RecCabinet.REC_SCAN_COM);
				if (recCom != null && !"".equals(recCom)) {
					model.addAttribute("recCom", recCom);
				}
			}
		}

		model.addAttribute("list", list);
		model.addAttribute("selectedType", "subcabinet");
		return "borrandstor";
	}

	/**
	 * @description 初始化购物车
	 * @param type 借出类型
	 */
	@RequestMapping("/initCart")
	public String initCart(Model model, HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		List<CartInfo> cartInfoList = this.toolService.selectCart(accountId, mainCabinetId);
		model.addAttribute("cabinetId", mainCabinetId);
		model.addAttribute("cartInfoList", cartInfoList);
		return "cart";
	}

	/**
	 * @description 详情增加购物车
	 * @param latticeId 格子Id
	 * @param num       借出数量
	 */
	@RequestMapping(value = "/proAddCart", method = RequestMethod.POST)
	@ResponseBody
	public int proAddCart(@RequestParam("matId") String matId, @RequestParam("type") String type,
			@RequestParam("num") int num, @RequestParam("receiveType") String receiveType,
			@RequestParam("receiveInfo") String receiveInfo, @RequestParam("remainNum") int remainNum, Model model,
			HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		List<CartInfo> cartInfoList = this.toolService.selectCart(accountId, mainCabinetId);

		// 根据物料ID分组
		Map<String, List<CartInfo>> cartInfoMap = cartInfoList.stream()
				.collect(Collectors.groupingBy(CartInfo::getMatId));

		for (Entry<String, List<CartInfo>> entry : cartInfoMap.entrySet()) {
			String materialId = entry.getKey();
			List<CartInfo> cartList = entry.getValue();

			// 获取同种物料总数量
			int number = 0;
			if (StringUtils.isNotBlank(materialId) && materialId.equals(matId)) {
				number = cartList.stream().mapToInt(CartInfo::getNum).sum();
			}

			if (CollectionUtils.isNotEmpty(cartList)) {
				for (CartInfo cart : cartList) {
					if (type.equals("PACK") && cart.getBorrowTypeName().equals("METER")) {
						if (num * cart.getPackNum() + number > remainNum * cart.getPackNum()) {
							return 0;
						}
					}
					if (type.equals("METER") && cart.getBorrowTypeName().equals("METER")) {
						if (num + number > remainNum * cart.getPackNum()) {
							return 0;
						}
					}
					if (type.equals("PACK") && cart.getBorrowTypeName().equals("PACK")) {
						if (num + number > remainNum) {
							return 0;
						}
					}
				}
			}
		}

		this.toolService.addCart(matId, type, num, accountId, receiveType, receiveInfo, mainCabinetId);
		List<CartInfo> cartList = this.toolService.selectCart(accountId, mainCabinetId);
		int nums = 0;
		for (CartInfo info : cartList) {
			nums += info.getNum();
		}
		model.addAttribute("type", type);
		model.addAttribute("cabinetId", mainCabinetId);
		return nums;
	}

	/**
	 * @description 增加购物车
	 * @param latticeId 格子Id
	 * @param num       借出数量
	 */
	@RequestMapping(value = "/addCart", method = RequestMethod.POST)
	@ResponseBody
	public int addCart(@RequestParam("matId") String matId, @RequestParam("type") String type,
			@RequestParam("num") int num, @RequestParam("remainNum") int remainNum,
			@RequestParam("receiveType") String receiveType, @RequestParam("receiveInfo") String receiveInfo,
			Model model, HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		List<CartInfo> cartInfoList = this.toolService.selectCart(accountId, mainCabinetId);

		// 根据物料ID分组
		Map<String, List<CartInfo>> cartInfoMap = cartInfoList.stream().collect(Collectors.groupingBy(CartInfo::getMatId));
		String materialId;
		List<CartInfo> cartList;
		for (Entry<String, List<CartInfo>> entry : cartInfoMap.entrySet()) {
			materialId = entry.getKey();
			if (!matId.equals(materialId)) {
				continue;
			}
			cartList = entry.getValue();

			// 获取同种物料总数量
			int number = cartList.stream().mapToInt(CartInfo::getNum).sum();

			if (CollectionUtils.isNotEmpty(cartList)) {
				for (CartInfo cart : cartList) {
					if (type.equals("PACK") && cart.getBorrowTypeName().equals("METER")) {
						if (num * cart.getPackNum() + number > remainNum * cart.getPackNum()) {
							return 0;
						}
					}
					if (type.equals("METER") && cart.getBorrowTypeName().equals("METER")) {
						if (num + number > remainNum * cart.getPackNum()) {
							return 0;
						}
					}
					if (type.equals("PACK") && cart.getBorrowTypeName().equals("PACK")) {
						if (num + number > remainNum) {
							return 0;
						}
					}
				}
			}
		}

		this.toolService.addCart(matId, type, num, accountId, receiveType, receiveInfo, mainCabinetId);
		cartList = this.toolService.selectCart(accountId, mainCabinetId);
		int nums = 0;
		for (CartInfo info : cartList) {
			nums += info.getNum();
		}
		model.addAttribute("type", type);
		model.addAttribute("cabinetId", mainCabinetId);
		return nums;
	}

	/**
	 * @description 产品详情
	 * @param matId       物料Id
	 * @param num         储位当前储量
	 * @param cabinetId   柜体Id
	 * @param latticeId   储位Id
	 * @param cabinetType 柜体类型KNIFE_CABINET
	 *                    默认弹簧柜,STORE_CABINET储物柜,VIRTUAL_WAREHOUSE虚拟仓
	 * @param comm        储物柜通讯串口
	 * @param boardNo     储物柜通讯栈号
	 * @param lockIndex   储物柜索引针脚号
	 * @param boxIndex    储物柜索引号
	 * @param remainNum 剩余数量
	 * @param rowNo       行号
	 * @param interval 间隔数
	 * @param model
	 */
	@RequestMapping("/pro")
	public String pro(@RequestParam("matId") String matId, @RequestParam("num") int num,
			@RequestParam("cabinetId") String cabinetId, @RequestParam("latticeId") String latticeId,
			@RequestParam("cabinetType") String cabinetType, @RequestParam("comm") String comm,
			@RequestParam("boardNo") String boardNo, @RequestParam("lockIndex") String lockIndex,
			@RequestParam("boxIndex") String boxIndex, @RequestParam("remainNum") int remainNum,
			@RequestParam("rowNo") String rowNo, @RequestParam("interval") int interval, Model model) {
		Lattice lattice = this.toolService.getPro(matId, latticeId);
		model.addAttribute("matId", matId);
		model.addAttribute("remainNum", remainNum);
		model.addAttribute("num", num);
		model.addAttribute("cabinetId", cabinetId);
		model.addAttribute("lattice", lattice);
		model.addAttribute("cabinetType", cabinetType);
		model.addAttribute("comm", comm);
		model.addAttribute("boardNo", boardNo);
		model.addAttribute("lockIndex", lockIndex);
		model.addAttribute("boxIndex", boxIndex);
		model.addAttribute("rowNo", rowNo);
		model.addAttribute("interval", interval);
		return "pro";
	}

	/**
	 * @description 修改购物车
	 * @param num    数量
	 * @param cartId 购物车Id
	 */
	@RequestMapping(value = "/updateCart", method = RequestMethod.POST)
	@ResponseBody
	public int updateCart(@RequestParam("cartId") String cartId, @RequestParam("num") int num, Model model,
			HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		this.toolService.updateCart(cartId, num);
		List<CartInfo> cartInfoList = this.toolService.selectCart(accountId, mainCabinetId);
		int nums = 0;
		for (CartInfo info : cartInfoList) {
			nums += info.getNum();
		}
		return nums;
	}

	/**
	 * @description 删除购物车
	 * @param cartId 购物车Id
	 */
	@RequestMapping(value = "/delCart", method = RequestMethod.POST)
	@ResponseBody
	public int delCart(Model model, HttpServletRequest request) {
		String mainCabinetId = DosthToolcabinetRunnerInit.getCabinetParam(this.cabinetConfig.getSerialNo(),
				SetupKey.Cabinet.MAIN_CABINET_ID);
		String accountId = super.getAccountInfo(request).getAccountId();
		String cart = request.getParameter("cart");
		JSONArray array = JSONArray.fromObject(cart);
		JSONObject obj = null;
		for (int i = 0; i < array.size(); i++) {
			obj = JSONObject.fromObject(array.get(i));
			String cartId = obj.getString("cartId");
			this.toolService.delCart(cartId);
		}
		List<CartInfo> cartInfoList = this.toolService.selectCart(accountId, mainCabinetId);
		int nums = 0;
		for (CartInfo info : cartInfoList) {
			nums += info.getNum();
		}
		return nums;
	}
}