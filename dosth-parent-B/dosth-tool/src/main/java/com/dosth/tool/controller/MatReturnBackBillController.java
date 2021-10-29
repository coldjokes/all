package com.dosth.tool.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.controller.ShiroController;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.VerifyMode;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.entity.RestitutionType;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.RestitutionTypeRepository;
import com.dosth.tool.repository.UserRepository;
import com.dosth.tool.service.MatReturnBackService;

/**
 * @description 归还管理Controller
 * @author liweifeng
 *
 */
@Controller
@RequestMapping("/matReturnBackBill")
public class MatReturnBackBillController extends ShiroController {

	private static String PREFIX = "/tool/matReturnBackBill/";

	@Autowired
	private MatReturnBackService matReturnBackService;
	@Autowired
	MatReturnBackRepository matReturnBackRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private RestitutionTypeRepository restitutionTypeRepository;

	/**
	 * @description 跳转页面
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<EquSetting> equSettingList = this.equSettingRepository.findAll();
		List<RestitutionType> returnTypeList = this.restitutionTypeRepository.getReturnBackList();
		model.addAttribute("equSettingList", equSettingList);
		model.addAttribute("returnTypeList", returnTypeList);
		return PREFIX + "index.html";
	}

	/**
	 * @description 正常确认
	 */
	@RequestMapping("/normalConfirm/{arrs}")
	@BussinessLog(businessName = "正常确认")
	@ResponseBody
	public Tip normalConfirm(@PathVariable String[] arrs) {
		String accountId = super.getShiroAccount().getId();
		for (int i = 0; i < arrs.length; i++) {
			String matReturnBackId = arrs[i];
			MatReturnBack matReturnBack = this.matReturnBackService.get(matReturnBackId);
			if (matReturnBack != null) {
				matReturnBack.setConfirmUser(this.userRepository.findUserByAccountId(accountId).getUserName());
				matReturnBack.setConfirmDate(new Date());
				matReturnBack.setConfirmMode(VerifyMode.NORCONF);
				matReturnBack.setAuditStatus(AuditStatus.PASS);
				matReturnBack.setNum(matReturnBack.getMatUseBill().getMatUseRecord().getMatInfo().getNum());
				matReturnBack.setRemark("");
				matReturnBack.setBarCode("");

				this.matReturnBackService.update(matReturnBack);
			}
		}
		return SUCCESS_TIP;
	}

	/**
	 * @description 跳转扫描确认页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/scanView")
	public String scanView(Model model) {
		return PREFIX + "scanView.html";
	}

//	/**
//	 * @description 扫描确认
//	 */
//	@RequestMapping("/scanConfirm")
//	@BussinessLog(businessName = "扫描确认")
//	@ResponseBody
//	public Tip scanConfirm(HttpServletRequest request) {
//		String accountId = super.getShiroAccount().getId();
//		String arrs = request.getParameter("matReturnBackId");
//		String[] arrsList = arrs.split(";");
//		for (int i = 0; i < arrsList.length; i++) {
//			String matReturnBackId = arrsList[i];
//			MatReturnBackVerify matReturnBackVerify = this.matReturnBackVerifyService.get(matReturnBackId);
//			if (matReturnBackVerify != null) {
//				continue;
//			}
//			matReturnBackVerify = new MatReturnBackVerify();
//			matReturnBackVerify.setMode(VerifyMode.SCANCONF);
//			matReturnBackVerify.setOpDate(new Date());
//			matReturnBackVerify.setMatReturnBackId(matReturnBackId);
//			matReturnBackVerify.setAccountId(accountId);
//			this.matReturnBackVerifyService.save(matReturnBackVerify);
//		}
//		verifyScanCompoment.stopMonitorScan();
//		return SUCCESS_TIP;
//	}

	/**
	 * @description 跳转异常确认页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/abnormalView/{matReturnBackId}")
	public String abnormalView(@PathVariable String matReturnBackId, Model model) {
		model.addAttribute("matReturnBackId", matReturnBackId);
		return PREFIX + "abnormalView.html";
	}

	/**
	 * @description 异常确认
	 */
	@RequestMapping("/abnormalConfirmSubmit/{matReturnBackId}")
	@BussinessLog(businessName = "异常确认")
	@ResponseBody
	public Tip abnormalConfirmSubmit(@Valid MatReturnBack verify, @PathVariable String matReturnBackId) {
		String accountId = super.getShiroAccount().getId();
		MatReturnBack matReturnBack = this.matReturnBackRepository.findByBackId(matReturnBackId);
		if (matReturnBack != null) {
			matReturnBack.setConfirmUser(this.userRepository.findUserByAccountId(accountId).getUserName());
			matReturnBack.setConfirmDate(new Date());
			matReturnBack.setConfirmMode(VerifyMode.ABNORCONF);
			matReturnBack.setNum(verify.getNum());
			matReturnBack.setRemark(verify.getRemark());
			matReturnBack.setAuditStatus(verify.getAuditStatus());
			matReturnBack.setBarCode("");
			this.matReturnBackService.update(matReturnBack);
		}
		return SUCCESS_TIP;
	}

	/**
	 * @description 归还查询列表
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
		String userName = super.getPara("userName");
		String status = super.getPara("status");
		String backType = super.getPara("backType");
		String equSettingName = super.getPara("equSettingName");
		Page<MatReturnBack> page = this.matReturnBackService.getPage(pageNo, pageSize, beginTime, endTime, userName,
				status, backType, equSettingName);
		return page;
	}

	/**
	 * 归还清单导出
	 */
	@RequestMapping("/infoExport/{params}")
	@ResponseBody
	public String infoExport(HttpServletRequest request, HttpServletResponse response, @PathVariable String[] params)
			throws IOException {
		// params[0]:beginTime, params[1]:endTime, params[2]:userName, params[3]:status,
		// params[4]:backType, params[5]:equSettingName
		String matReturnBack = this.matReturnBackService.infoExport(request, response, params[0], params[1], params[2],
				params[3], params[4], params[5]);
		return matReturnBack;
	}
}