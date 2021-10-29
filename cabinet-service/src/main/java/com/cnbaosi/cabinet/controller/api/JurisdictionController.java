package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.MaterialCriteria;
import com.cnbaosi.cabinet.entity.criteria.UserCriteria;
import com.cnbaosi.cabinet.entity.modal.Jurisdiction;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;
import com.cnbaosi.cabinet.serivce.JurisdictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 领取权限Controller
 */
@RestController
@RequestMapping("/api/jurisdiction")
public class JurisdictionController extends BaseController {

	@Autowired
	private JurisdictionService juriService;

	/**
	 * @description 初始化帐户树
	 * @return
	 */
	@LogRecord("查询用户树")
	@RequestMapping(value = "/initUserTree", method = RequestMethod.GET)
	@ResponseBody
	public RestFulResponse<TreeNodes> initAccountTree() {
		List<TreeNodes> results = juriService.getUserDeptTree();
		return success(results.size(),results);
	}

	@LogRecord("查询取料权限树")
	@RequestMapping(value = "/initReceiveTree", method = RequestMethod.GET)
	@ResponseBody
	public RestFulResponse<TreeNodes> initReceiveTree() {
		List<TreeNodes> results = juriService.getMaterialCategory();
		return success(results.size(),results);
	}

	/**
	 * 点击人员树获取取料权限树
	 */
	@LogRecord("查询人员取料权限")
	@PostMapping("/getDepts")
	public RestFulResponse userMaterial(@RequestBody MaterialCriteria materialCriteria){
		RestFulResponse restFulResponse = new RestFulResponse();
			List<String> results = juriService.getUserMaterials(materialCriteria);
			restFulResponse.setResults(results);
			restFulResponse.setCode(200);
		return restFulResponse;
	}

	/**
	 * 保存人员取料权限
	 */
	@LogRecord("保存人员取料权限")
	@PostMapping
	@ResponseBody
	public RestFulResponse<String> saveAccess(@RequestBody UserCriteria userCriteria){
		String msg = juriService.saveAccess(userCriteria);
		if(msg == null) {
			return success("保存成功！");
		} else {
			return error("保存失败！" + msg);
		}
	}

	/**
	 * 设备端领取--判断人员是否有取料权限
	 */
	@LogRecord("设备端判断人员取料权限")
	@PostMapping("/receiveRole")
	@ResponseBody
	public RestFulResponse receiveRoles(@RequestBody Jurisdiction jurisdiction){
		RestFulResponse restFulResponse = new RestFulResponse();
		Boolean result = juriService.receiveRoles(jurisdiction);
		restFulResponse.setMessage(result.toString());
		return restFulResponse;
	}

}
