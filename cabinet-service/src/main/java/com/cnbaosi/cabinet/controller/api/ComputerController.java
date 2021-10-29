package com.cnbaosi.cabinet.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.ComputerCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.modal.Computer;
import com.cnbaosi.cabinet.serivce.ComputerService;
import com.google.common.collect.Lists;

/**
 * 主机
 * 
 * @author Yifeng Wang  
 */
@RestController
@RequestMapping("/api/computers")
public class ComputerController extends BaseController {

	@Autowired
	private ComputerService computerSvc;
	
	@LogRecord("新增主机")
	@PostMapping
	public RestFulResponse<String> addComputer(@RequestBody Computer computer) {
		return actionResult(computerSvc.addComputer(computer));
	}
	
	@LogRecord("删除主机")
	@DeleteMapping("/{id}")
	public RestFulResponse<String> deleteComputer(@PathVariable("id") String id) {
		String msg = computerSvc.deleteComputer(id);
		if(msg == null) {
			return success("删除成功！");
		} else {
			return error("删除失败！" + msg);
		}
	}
	
	@LogRecord("修改主机")
	@PutMapping
	public RestFulResponse<String> updateUser(@RequestBody Computer computer) {
		return actionResult(computerSvc.updateComputer(computer));
	}
	
	@LogRecord("获取所有主机列表") 
	@GetMapping
	public RestFulResponse<Computer> getComputers(ComputerCriteria computerCriteria, PageCriteria pageCriteria) {
		Page<Computer> pagedComputer = computerSvc.getComputers(computerCriteria, pageCriteria);
		return success(pagedComputer.getTotal(), pagedComputer.getRecords());
	}
	
	@LogRecord("通过唯一识别码获取主机信息")
	@GetMapping("/getByIdentifyCode")
	public RestFulResponse<Computer> getComputerId(String identifyCode) {
		Computer computer = computerSvc.getComputerByIdentifyCode(identifyCode);
		return success(1, Lists.newArrayList(computer));
	}
	
	@LogRecord("用户名校验")
	@GetMapping("/nameCheck")
	public boolean nameCheck(@RequestParam String name) {
		Computer computer = computerSvc.getComputerByName(name);
		return computer == null;
	}
}

