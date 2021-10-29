package com.dosth.tool.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.ShiroController;
import com.dosth.tool.entity.BorrowInfo;
import com.dosth.tool.service.ScanCabinetService;
import com.dosth.util.OpTip;


@Controller
@RequestMapping("/recycReview")
public class RecycReviewController extends ShiroController {
	
	private static String PREFIX = "/tool/recycReview/";

	@Autowired
	private ScanCabinetService scanCabinetService;
	
	/**
	 * 跳转到班次设定列表的页面
	 */
	@RequestMapping("")
	public String index() {
		return PREFIX + "index.html";
	}
	
	/**
	 * 搜索
	 */
	@RequestMapping("/search/{barCode}")
	public String search(@PathVariable String barCode, Model model) {
		List<BorrowInfo> borrowInfo = this.scanCabinetService.recycleInfo(barCode);
		model.addAttribute("borrowInfo",borrowInfo);
		return PREFIX + "edit.html";
	}
	
	/**
	 * @description 审核通过
	 * @param 
	 * @return
	 */
	@RequestMapping("/review_pass/{barCode}")
	@ResponseBody
	public OpTip recyclePass(@PathVariable String barCode) {
		String accountId = super.getShiroAccount().getId();
		OpTip tip = this.scanCabinetService.recyclePass(barCode, accountId);
		return tip;
	}
	
	/**
	 * @description 不通过原因提交
	 * @param 
	 * @return
	 */
	@RequestMapping("/reject_submit/{barCode}/{num}/{mark}")
	@ResponseBody
	public OpTip rejectSubmit(@PathVariable("barCode") String code, @PathVariable("num") int num,
			@PathVariable("mark") String mark) {
		String accountId = super.getShiroAccount().getId();
		OpTip tip = this.scanCabinetService.recycleReject(code, num, mark, accountId);
		return tip;
	}
}
