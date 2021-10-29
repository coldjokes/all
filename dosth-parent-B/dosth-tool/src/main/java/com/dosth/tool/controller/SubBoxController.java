package com.dosth.tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.controller.BaseController;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.service.SubBoxService;
import com.dosth.util.OpTip;

/**
 * 暂存柜格子Controller
 * 
 * @author Weifeng.Li
 *
 */
@Controller
@RequestMapping("/subBox")
public class SubBoxController extends BaseController {

	@Autowired
	private SubBoxService subBoxService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ResponseBody
	public Object list() {
		int pageNo = 1;
		int pageSize = 10;
		try {
			pageNo = Integer.valueOf(super.getPara("offset")) / Integer.valueOf(super.getPara("limit"));
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {

		}
		String equSettingId = super.getPara("equSettingId");
		Page<SubBox> page = this.subBoxService.getPage(pageNo, pageSize, equSettingId);
		return page;
	}

	/**
	 * 修改-主柜/子柜
	 */
	@RequestMapping("/update")
	@ResponseBody
	public OpTip edit(@RequestParam("subBoxId") String subBoxId, @RequestParam("lockIndex") String lockIndex) {
		OpTip tip = new OpTip(200, "修改成功");
		SubBox subBox = this.subBoxService.get(subBoxId);
		subBox.setLockIndex(Integer.valueOf(lockIndex));
		this.subBoxService.update(subBox);
		return tip;
	}
}
