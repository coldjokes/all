package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.CabinetCriteria;
import com.cnbaosi.cabinet.entity.modal.Cabinet;
import com.cnbaosi.cabinet.serivce.CabinetService;

/**
 *  柜体
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/api/cabinets")
public class CabinetController extends BaseController {

	@Autowired
	private CabinetService cabinetSvc;
	
	@LogRecord("新增柜体")
	@PostMapping
	public RestFulResponse<String> addCabinet(@RequestBody Cabinet cabinet) {
		return actionResult(cabinetSvc.addCabinet(cabinet));
	}
	
	@LogRecord("删除柜体")
	@DeleteMapping("/{id}")
	public RestFulResponse<String> deleteComputer(@PathVariable("id") String id) {
		
		String msg = cabinetSvc.deleteCabinetById(id);
		if(msg == null) {
			return success("删除成功！");
		} else {
			return error("删除失败！" + msg);
		}
	}
	
	@LogRecord("修改柜体")
	@PutMapping
	public RestFulResponse<String> updateUser(@RequestBody Cabinet cabinet) {
		return actionResult(cabinetSvc.updateCabinet(cabinet));
	}

	@LogRecord("获取柜体列表")
	@GetMapping
	public RestFulResponse<Cabinet> getCabinets(CabinetCriteria cabinetCriteria) {
		List<Cabinet> results = cabinetSvc.getCabinets(cabinetCriteria);
		return success(results.size(), results);
	}
	
}

