package com.dosth.tool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnbaosi.dto.OpTip;
import com.dosth.tool.service.DataDeleteService;

/**
 * @description 清理数据
 * @author Zhidong.Guo
 *
 */
@Controller
@RequestMapping("/clearData")
public class ClearDataController {

	private static final String PREFIX = "/tool/cleardata/";
	
	@Autowired
	private DataDeleteService dataDeleteService;

	@RequestMapping("")
	public String index() {
		return PREFIX + "index.html";
	}
	
	/**
	 * @description 清理物料
	 */
	@RequestMapping("/cleatMat")
	@ResponseBody
	public OpTip clearMat() {
		OpTip tip = new OpTip("清理物料信息成功");
		try {
			this.dataDeleteService.clearMat();
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("清理物料信息失败");
		}
		return tip;
	}

	/**
	 * @description 清理设备
	 */
	@RequestMapping("/clearEqu")
	@ResponseBody
	public OpTip clearEqu() {
		OpTip tip = new OpTip("清理设备信息成功");
		try {
			this.dataDeleteService.clearEqu();
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("清理设备信息失败");
		}
		return tip;
	}

	/**
	 * @description 清理人员
	 */
	@RequestMapping("/clearUser")
	@ResponseBody
	public OpTip clearUser() {
		OpTip tip = new OpTip("清理人员信息成功");
		try {
			this.dataDeleteService.clearUser();
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("清理人员信息失败");
		}
		return tip;
	}
	
	/**
	 * @description 一键恢复出厂设置
	 */
	@RequestMapping("/resetInit")
	@ResponseBody
	public OpTip resetInit() {
		OpTip tip = new OpTip("一键恢复成功");
		try {
			this.dataDeleteService.resetInit();
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("一键恢复失败");
		}
		return tip;
	}
}