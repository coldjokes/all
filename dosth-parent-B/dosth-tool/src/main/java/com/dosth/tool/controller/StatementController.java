package com.dosth.tool.controller;

import java.util.Date;
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
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.controller.ShiroController;
import com.dosth.tool.common.warpper.StatementPageWarpper;
import com.dosth.tool.entity.Statement;
import com.dosth.tool.service.StatementService;

/**
 * @description 供应商核对Controller
 * @author guozhidong
 *
 */
@Controller
@RequestMapping("/statement")
public class StatementController extends ShiroController {

	private static final String PREFIX = "/tool/statement/";

	@Autowired
	private StatementService statementService;

	/**
	 * @description 主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		return PREFIX + "index.html";
	}

	/**
	 * @description 核对列表
	 * @param model
	 * @param manufacturerId 供应商Id
	 * @param startDate 起始日期
	 * @param endDate 截止日期
	 * @return
	 */
	@RequestMapping("/list")
	public String list(Model model, @RequestParam String manufacturerId, @RequestParam String startDate,
			@RequestParam String endDate) {
		// 重置未核对的数据
		this.statementService.statement(manufacturerId, startDate, endDate);	
		// 查询区间内的核对信息
		List<Statement> list = this.statementService.getStatementList(manufacturerId, startDate, endDate);
		model.addAttribute("list", list);
		return PREFIX + "list.html";
	}
	
	/**
	 * @description 添加核对
	 */
	@RequestMapping("/saveStatement")
	@ResponseBody
	public Tip save(@RequestParam("statementId") String statementId) {
		Tip tip = new SuccessTip();
		try {
			Statement statement = this.statementService.get(statementId);
			if (YesOrNo.NO.equals(statement.getIsHD())) {
				// 设置核对人员
				statement.setAccountId(super.getShiroAccount().getId());
				statement.setOpDate(new Date());
				statement.setIsHD(YesOrNo.YES);
				this.statementService.update(statement);
			} else {
				tip.setMessage("当前批次物料已被核对过");
			}
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("操作失败!");
		}
		return tip;
	}
	
	/**
	 * @description 核对明细
	 * @param statementId 核对Id
	 * @return
	 */
	@RequestMapping("/view")
	public String view(@RequestParam("statementId") String statementId, Model model) {
		model.addAttribute("recordList", this.statementService.getMatUseRecordView(statementId));
		return PREFIX + "view.html";
	}
	
	/**
	 * @description 导出核对明细
	 * @param statementId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportDetail/{statementId}")
	public void exportDetail(@PathVariable("statementId") String statementId, HttpServletRequest request, HttpServletResponse response) {
		this.statementService.exportDetail(statementId, request, response);
	}
	
	/**
	 * @description 初始化查询主页面
	 */
	@RequestMapping("/initQuery")
	public String initQuery(Model model) {
		model.addAttribute("columns", new StatementPageWarpper(null).createColumns(TableSelectType.RADIO));
		return PREFIX + "query.html";
	}

	/**
	 * @description 历史核对查询列表
	 */
	@RequestMapping("/page")
	@ResponseBody
	public Object page() {
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
		String startDate = super.getPara("startDate");
		String endDate = super.getPara("endDate");
		Page<Statement> page = this.statementService.getPager(pageNo, pageSize, startDate, endDate);
		return new StatementPageWarpper(page).invokeObjToMap();
	}
}