package com.dosth.tool.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.base.tips.SuccessTip;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.ShiroController;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.Inventory;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.InventoryService;
import com.dosth.tool.vo.InventoryInfo;

/**
 * @description 盘点Controller
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/inventory")
public class InventoryController extends ShiroController {
	private static final String PREFIX = "/tool/inventory/";

	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private EquSettingRepository equSettingRepository;

	/**
	 * @description 跳转到主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * 创建已设置设备tree[储存设备]
	 */
	@RequestMapping("/tree")
	@ResponseBody
	public List<ZTreeNode> treeEqu() {
		return this.inventoryService.treeEqu();
	}

	/**
	 * @description 库存详情
	 * @param equInfoId 设备Id
	 * @return
	 */
	@RequestMapping("/getEquStockView")
	public String getEquStockView(Model model, @RequestParam("equInfoId") String equInfoId) {
		List<InventoryInfo> infoList = this.inventoryService.getEquStockView(equInfoId);
		model.addAttribute("infoList", infoList);
		if (equInfoId.startsWith("ZC_")) {
			return PREFIX + "subbox.html";
		}
		return PREFIX + "detail.html";
	}

	/**
	 * @description 盘点操作
	 * @param equInfoId     盘点对象Id 主柜或暂存柜
	 * @param inventoryVals 实盘信息
	 */
	@RequestMapping("/inventory")
	@ResponseBody
	public Tip inventory(@RequestParam("equInfoId") String equInfoId,
			@RequestParam("inventoryVals") String inventoryVals) {
		Tip tip = new SuccessTip();
		try {
			this.inventoryService.inventory(equInfoId, inventoryVals, super.getShiroAccount().getId());
			tip.setMessage("操作成功!");
		} catch (DoSthException e) {
			tip.setCode(e.getCode());
			tip.setMessage(e.getMessage());
		}
		return tip;
	}

	/**
	 * @description 跳转到主页面
	 */
	@RequestMapping("/query")
	public String query(Model model) {
		List<EquSetting> equSettingList = this.equSettingRepository.findAll();
		model.addAttribute("equSettingList", equSettingList);
		return PREFIX + "query.html";
	}

	/**
	 * @description 查询列表
	 */
	@RequestMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		try {
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		String beginTime = super.getPara("beginTime");
		String endTime = super.getPara("endTime");
		String matInfo = super.getPara("matInfo");
		String equName = super.getPara("equName");
		Page<Inventory> page = this.inventoryService.getPage(pageNo, pageSize, beginTime, endTime, matInfo, equName);
		return page;
	}

	/**
	 * @description 导出补料详情
	 * @param request
	 * @param response
	 * @param beginTime 起始时间
	 * @param endTime   截止时间
	 */
	@RequestMapping("/export/{beginTime}/{endTime}")
	@ResponseBody
	public void exportFeedingDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String beginTime, @PathVariable String endTime) throws IOException {
		this.inventoryService.exportInventory(request, response, beginTime, endTime);
	}
}