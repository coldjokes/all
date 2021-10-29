package com.dosth.tool.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.naming.NoPermissionException;
import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.warpper.ExtraBoxNumSettingPageWarpper;
import com.dosth.tool.entity.ExtraBoxNumSetting;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.service.ExtraBoxNumSettingService;
import com.dosth.tool.service.SubBoxService;
import com.dosth.tool.service.SubCabinetDetailService;
import com.dosth.util.OpTip;

@Controller
@RequestMapping("/extraBoxNumSetting")
public class ExtraBoxNumSettingController extends BaseController {

	private static String PREFIX = "/tool/extraBoxNumSetting/";

	@Autowired
	private ExtraBoxNumSettingService extraBoxNumSettingService;
	@Autowired
	private SubBoxService subBoxService;
	@Autowired
	private SubCabinetDetailService subCabinetDetailService;

	/**
	 * 跳转到主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new ExtraBoxNumSettingPageWarpper(null).createColumns(TableSelectType.RADIO));
		return PREFIX + "index.html";
	}

	/**
	 * 添加页面
	 */
	@RequestMapping("/extraBoxNumSetting_add")
	public String addView(Model model) {
		// 当前暂存柜已设置数量
		Integer sumExtraBoxNum = 0;
		List<ExtraBoxNumSetting> extraBoxNumList = this.extraBoxNumSettingService.findAll();
		for (ExtraBoxNumSetting extraBoxNum : extraBoxNumList) {
			if (extraBoxNum.getStatus().equals(UsingStatus.ENABLE)) {
				sumExtraBoxNum += Integer.parseInt(extraBoxNum.getExtraBoxNum());
			}
		}
		// 暂存柜总数量
		Integer subBoxNum = this.subBoxService.getSubBoxNum();
		// 可设置暂存柜数量
		Integer num = subBoxNum - sumExtraBoxNum;
		model.addAttribute("num", num);
		model.addAttribute(new ExtraBoxNumSetting());
		return PREFIX + "add.html";
	}

	/**
	 * 添加
	 */
	@RequestMapping("/add")
	@BussinessLog(businessName = "添加定义")
	@ResponseBody
	public OpTip add(@Valid ExtraBoxNumSetting extraBoxNumSetting, BindingResult result) {
		OpTip tip = new OpTip(200, "添加成功！");
		List<ExtraBoxNumSetting> list = this.extraBoxNumSettingService.getNumListByAccountId(extraBoxNumSetting.getAccountId());
		if (list != null && list.size() > 0) {
			tip = new OpTip(201, "当前人员已分配暂存柜,不可重复新增！");
		} else {
			// 当前暂存柜已设置数量
			Integer sumExtraBoxNum = 0;
			List<ExtraBoxNumSetting> extraBoxNumList = this.extraBoxNumSettingService.findAll();
			for (ExtraBoxNumSetting extraBoxNum : extraBoxNumList) {
				if (extraBoxNum.getStatus().equals(UsingStatus.ENABLE)) {
					sumExtraBoxNum += Integer.parseInt(extraBoxNum.getExtraBoxNum());
				}
			}
			// 暂存柜总数量
			Integer subBoxNum = this.subBoxService.getSubBoxNum();
			// 可设置暂存柜数量
			Integer num = subBoxNum - sumExtraBoxNum;
			// 当前设置的暂存柜数量
			Integer cunNum = Integer.parseInt(extraBoxNumSetting.getExtraBoxNum());
			if (cunNum <= num) {
				String id = this.extraBoxNumSettingService.getAccount(extraBoxNumSetting.getAccountId());
				// 用户已添加的场合，作为Update处理
				if (id != null) {
					if (extraBoxNumSetting.getStatus().equals(UsingStatus.ENABLE)) {
						extraBoxNumSetting.setStatus(UsingStatus.ENABLE);
					} else {
						extraBoxNumSetting.setStatus(UsingStatus.ENABLE);
					}
					extraBoxNumSetting.setId(id);
					this.extraBoxNumSettingService.update(extraBoxNumSetting);
					// 用户未添加的场合，作为Insert处理
				} else {
					extraBoxNumSetting.setStatus(UsingStatus.ENABLE);
					this.extraBoxNumSettingService.save(extraBoxNumSetting);
				}
			} else {
				tip = new OpTip(201, "超出可分配数量！");
				return tip;
			}
		}
		return tip;
	}

	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("/extraBoxNumSetting_edit/{id}")
	public String editView(@PathVariable String id, Model model) {
		ExtraBoxNumSetting extraBoxNumSetting = this.extraBoxNumSettingService.get(id);
		Integer curNum = Integer.parseInt(extraBoxNumSetting.getExtraBoxNum());
		model.addAttribute(extraBoxNumSetting);
		// 当前暂存柜已设置数量
		Integer sumExtraBoxNum = 0;
		List<ExtraBoxNumSetting> extraBoxNumList = this.extraBoxNumSettingService.findAll();
		for (ExtraBoxNumSetting extraBoxNum : extraBoxNumList) {
			if (extraBoxNum.getStatus().equals(UsingStatus.ENABLE)) {
				sumExtraBoxNum += Integer.parseInt(extraBoxNum.getExtraBoxNum());
			}
		}
		// 暂存柜总数量
		Integer subBoxNum = this.subBoxService.getSubBoxNum();
		// 可设置暂存柜数量
		Integer num = subBoxNum - sumExtraBoxNum + curNum;
		model.addAttribute("num", num);
		return PREFIX + "edit.html";
	}

	/**
	 * 修改
	 */
	@RequestMapping("/edit")
	@BussinessLog(businessName = "修改")
	@ResponseBody
	public OpTip edit(@Valid ExtraBoxNumSetting extraBoxNumSetting, BindingResult result) throws NoPermissionException {
		OpTip tip = new OpTip(200, "修改成功！");
		// 获取当前使用中数量
		Integer i = 0;
		List<SubCabinetDetail> detailList = this.subCabinetDetailService.findByAccountId(extraBoxNumSetting.getAccountId());
		if (detailList != null && detailList.size() > 0) {
			i = detailList.size();
		}
		// 获取当前分配数量
		ExtraBoxNumSetting tmpExtraBoxNumSetting = this.extraBoxNumSettingService.get(extraBoxNumSetting.getId());
		Integer tmpNum = Integer.parseInt(tmpExtraBoxNumSetting.getExtraBoxNum());
		// 当前暂存柜已分配总数
		Integer sumExtraBoxNum = 0;
		List<ExtraBoxNumSetting> extraBoxNumList = this.extraBoxNumSettingService.findAll();
		for (ExtraBoxNumSetting extraBoxNum : extraBoxNumList) {
			if (extraBoxNum.getStatus().equals(UsingStatus.ENABLE)) {
				sumExtraBoxNum += Integer.parseInt(extraBoxNum.getExtraBoxNum());
			}
		}
		// 暂存柜总数
		Integer subBoxNum = this.subBoxService.getSubBoxNum();
		// 可分配暂存柜数量
		Integer num = subBoxNum - sumExtraBoxNum + tmpNum;
		// 当前设置的暂存柜数量
		Integer cunNum = Integer.parseInt(extraBoxNumSetting.getExtraBoxNum());
		
		//分配数量不能小于当前使用中数量
		if (cunNum < i) {
			tip = new OpTip(201, "该员工有" + i + "个正在使用的暂存柜，分配数量不能小于" + i + "！");
			return tip;
		}

		if (cunNum <= num) {
			ExtraBoxNumSetting tempExtraBoxNumSetting = this.extraBoxNumSettingService.get(extraBoxNumSetting.getId());
			if (tempExtraBoxNumSetting.getStatus().equals(UsingStatus.ENABLE)) {
				extraBoxNumSetting.setStatus(UsingStatus.ENABLE);
			} else {
				extraBoxNumSetting.setStatus(UsingStatus.DISABLE);
			}
			try {
				BeanUtils.copyProperties(tempExtraBoxNumSetting, extraBoxNumSetting);
				this.extraBoxNumSettingService.update(extraBoxNumSetting);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			tip = new OpTip(201, "超出可分配数量！");
			return tip;
		}
		return tip;
	}

	/**
	 * 删除
	 */
	@RequestMapping("/update/{id}")
	@BussinessLog(businessName = "删除")
	@ResponseBody
	public OpTip update(@PathVariable String id) {
		OpTip tip = new OpTip(200, "删除成功！");
		ExtraBoxNumSetting extraBoxNumSetting = this.extraBoxNumSettingService.get(id);
		Integer i = 0;
		List<SubCabinetDetail> detailList = this.subCabinetDetailService.findByAccountId(extraBoxNumSetting.getAccountId());
		if (detailList != null && detailList.size() > 0) {
			i= detailList.size();
		}
		if(i > 0 ) {
			tip = new OpTip(201, "该员工有" +i + "个正在使用的暂存柜，请先取完！");
			return tip;
		}
		extraBoxNumSetting.setStatus(UsingStatus.DISABLE);
		this.extraBoxNumSettingService.update(extraBoxNumSetting);
		return tip;
	}

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
		} catch (Exception e) {
		}
		try {
			pageSize = Integer.valueOf(super.getPara("limit"));
		} catch (Exception e) {
		}
		String name = super.getPara("userName");
		Page<ExtraBoxNumSetting> page = this.extraBoxNumSettingService.getPage(pageNo, pageSize, name);
		return new ExtraBoxNumSettingPageWarpper(page).invokeObjToMap();
	}
}
