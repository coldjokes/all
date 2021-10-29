package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.modal.CabinetCell;
import com.cnbaosi.cabinet.serivce.CabinetCellService;

/**
 *  格口
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/api/cabinetCells")
public class CabinetCellController extends BaseController {

	@Autowired
	private CabinetCellService cellSvc;
	
	@LogRecord("修改格口")
	@PutMapping
	public RestFulResponse<String> updateUser(@RequestBody List<CabinetCell> cellList) {
		return actionResult(cellSvc.updateCabinetCells(cellList));
	}
}

