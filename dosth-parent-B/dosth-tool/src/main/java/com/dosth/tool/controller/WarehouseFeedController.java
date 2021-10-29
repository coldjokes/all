package com.dosth.tool.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnbaosi.dto.tool.ExternalWarehouseFeed;
import com.dosth.common.controller.ShiroController;
import com.dosth.tool.common.state.FeedType;
import com.dosth.tool.service.WarehouseFeedService;
import com.dosth.util.OpTip;

/**
 * @description 入库单补料Controller
 * @author chenlei
 *
 */
@Controller
@RequestMapping("/warehouseFeed")
public class WarehouseFeedController extends ShiroController {

	private final static String PREFIX = "/tool/warehouseFeed/";

	@Autowired
	private WarehouseFeedService warehouseFeedService;

	@RequestMapping("")
	public String index() {
		return PREFIX + "index.html";
	}

	/**
	 * 获取入库单
	 */
	@ResponseBody
	@RequestMapping("/getFeedList")
	public Page<ExternalWarehouseFeed> getFeedList() throws IOException {
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
		String cabinetId = super.getPara("cabinetId");
		Page<ExternalWarehouseFeed> page = this.warehouseFeedService.getFeedList(pageNo, pageSize, cabinetId);
		return page;
		
		
	}

	/**
	 * 入库单补料清单
	 */
	@RequestMapping("/warehouseFeedList")
	@ResponseBody
	public OpTip warehouseFeedList(HttpServletRequest request) throws IOException {
		String accountId = super.getShiroAccount().getId();
		String arrs = request.getParameter("arrs");
		String equSettingId = request.getParameter("equSettingId");
		OpTip tip = this.warehouseFeedService.warehouseFeedList(arrs, equSettingId, accountId, FeedType.API);
		return tip;
	}


}