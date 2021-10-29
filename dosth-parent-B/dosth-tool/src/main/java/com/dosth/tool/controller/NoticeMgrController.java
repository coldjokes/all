package com.dosth.tool.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.controller.BaseController;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.NoticeMgr;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.service.NoticeMgrService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.util.OpTip;

/**
 * 通知管理Controller
 * 
 * @author Weifeng Li
 *
 */
@Controller
@RequestMapping("/noticeMgr")
public class NoticeMgrController extends BaseController {

	private static String PREFIX = "/tool/noticeMgr/";

	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private NoticeMgrService noticeMgrService;
	@Autowired
	private UserService userService;

	/**
	 * 跳转到主页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(Model model) {
		List<EquSetting> equSettingList = this.equSettingRepository.getMainCabinetList();
		model.addAttribute("equSettingList", equSettingList);
		return PREFIX + "index.html";
	}

	/**
	 * 列表
	 * 
	 * @return
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
		String name = super.getPara("name");
		String noticeType = super.getPara("noticeType");
		Page<NoticeMgr> page = this.noticeMgrService.getPage(pageNo, pageSize, name, noticeType);
		return page;
	}

	/**
	 * 跳转到添加页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/addView")
	public String addView(Model model) {
		List<EquSetting> equSettingList = this.equSettingRepository.getMainCabinetList();
		model.addAttribute("equSettingList", equSettingList);
		model.addAttribute(new NoticeMgr());
		return PREFIX + "edit.html";
	}

	/**
	 * 添加
	 * 
	 * @param noticeMgr
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public OpTip add(@Valid NoticeMgr noticeMgr) {
		OpTip tip = new OpTip(200, "添加成功");

		List<NoticeMgr> noticeMgrList = this.noticeMgrService.getNoticeMgrByCabinetId(noticeMgr.getEquSettingId())
				.stream().filter(mgr -> mgr.getNoticeType().equals(noticeMgr.getNoticeType()))
				.collect(Collectors.toList());

		if (noticeMgrList != null && noticeMgrList.size() > 0) {
			return new OpTip(201, "同一刀具柜通知类型不能重复添加");
		}

		if (noticeMgr.getWarnValue() > noticeMgr.getNum()) {
			return new OpTip(201, "预警值必须小于可用数量");
		}

		EquSetting equSetting = this.equSettingRepository.findOne(noticeMgr.getEquSettingId());
		if (equSetting != null) {
			noticeMgr.setEquSettingName(equSetting.getEquSettingName());
		}

		String userIds[] = noticeMgr.getAccountId().split(",");
		ViewUser user;
		String userName = "";
		for (int i = 0; i < userIds.length; i++) {
			user = this.userService.getViewUser(userIds[i]);
			if (user != null) {
				userName += user.getUserName() + ",";
				noticeMgr.setUserName(userName);
			}
		}

		if (noticeMgr.getNoticeType().equals(NoticeType.PRINT)) {
			noticeMgr.setCount(noticeMgr.getNum());
		} else if (noticeMgr.getNoticeType().equals(NoticeType.RECOVERY)) {
			noticeMgr.setCount(0);
		} else {
			noticeMgr.setCount(0);
		}

		this.noticeMgrService.save(noticeMgr);
		return tip;
	}

	/**
	 * 跳转到修改页面
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/editView/{id}")
	public String editView(@PathVariable String id, Model model) {
		NoticeMgr noticeMgr = this.noticeMgrService.get(id);
		List<EquSetting> equSettingList = this.equSettingRepository.getMainCabinetList();
		model.addAttribute("equSettingList", equSettingList);

		ViewUser viewUser;
		String userName = "";
		String array[] = noticeMgr.getAccountId().split(",");
		for (int i = 0; i < array.length; i++) {
			viewUser = this.userService.getViewUser(array[i]);
			if (viewUser != null) {
				userName += viewUser.getUserName() + ",";
			}
		}
		model.addAttribute("noticeType", noticeMgr.getNoticeType().toString());
		model.addAttribute("userName", userName);
		model.addAttribute("accountId", noticeMgr.getAccountId());
		model.addAttribute(noticeMgr);
		LogObjectHolder.me().set(noticeMgr);
		return PREFIX + "edit.html";
	}

	@RequestMapping("/edit")
	@ResponseBody
	public OpTip edit(@Valid NoticeMgr noticeMgr) {
		OpTip tip = new OpTip(200, "修改成功");

		List<NoticeMgr> noticeMgrList = this.noticeMgrService.getNoticeMgrByCabinetId(noticeMgr.getEquSettingId())
				.stream().filter(mgr -> mgr.getNoticeType().equals(noticeMgr.getNoticeType())
						&& !mgr.getId().equals(noticeMgr.getId()))
				.collect(Collectors.toList());

		if (noticeMgrList != null && noticeMgrList.size() > 0) {
			return new OpTip(201, "同一刀具柜通知类型不能重复添加");
		}

		if (noticeMgr.getWarnValue() != null && noticeMgr.getNum() != null
				&& noticeMgr.getWarnValue() > noticeMgr.getNum()) {
			return new OpTip(201, "预警值必须小于可用数量");
		}

		NoticeMgr temHardware = this.noticeMgrService.get(noticeMgr.getId());
		OnOrOff status = temHardware.getStatus() == null ? OnOrOff.OFF : temHardware.getStatus();

		EquSetting equSetting = this.equSettingRepository.findOne(noticeMgr.getEquSettingId());
		if (equSetting != null) {
			noticeMgr.setEquSettingName(equSetting.getEquSettingName());
		}

		String userIds[] = noticeMgr.getAccountId().split(",");
		ViewUser user;
		String userName = "";
		for (int i = 0; i < userIds.length; i++) {
			user = this.userService.getViewUser(userIds[i]);
			if (user != null) {
				userName += user.getUserName() + ",";
				noticeMgr.setUserName(userName);
			}
		}

		noticeMgr.setCount(temHardware.getCount());
		try {
			BeanUtils.copyProperties(temHardware, noticeMgr);
			temHardware.setStatus(status);
			this.noticeMgrService.update(temHardware);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return tip;
	}

	@RequestMapping("/delete/{id}")
	@ResponseBody
	public OpTip delete(@PathVariable String id) {
		OpTip tip = new OpTip(200, "删除成功");
		NoticeMgr noticeMgr = this.noticeMgrService.get(id);
		if (noticeMgr != null) {
			this.noticeMgrService.delete(noticeMgr);
		}
		return tip;
	}
	
	@RequestMapping("/statusSet/{id}/{status}")
	@ResponseBody
	public OpTip statusSet(@PathVariable("id") String id, @PathVariable("status") String status) {
		OpTip tip = new OpTip(200, "设置成功");
		NoticeMgr noticeMgr = this.noticeMgrService.get(id);
		noticeMgr.setStatus(status.equals("0") ? OnOrOff.ON : OnOrOff.OFF);
		this.noticeMgrService.update(noticeMgr);
		return tip;
	}
}
