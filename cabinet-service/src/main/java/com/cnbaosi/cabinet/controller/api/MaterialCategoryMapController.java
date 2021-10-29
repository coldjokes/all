package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryMapCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialCategoryMap;
import com.cnbaosi.cabinet.serivce.MaterialCategoryMapService;

/**
 * 物料类别映射
 * 
 * @author Yifeng Wang  
 */
@RestController
@RequestMapping("/api/materials/category/map")
public class MaterialCategoryMapController extends BaseController {

	@Autowired
	private MaterialCategoryMapService materialCateoryMapSvc;

	@LogRecord("更新物料类别关系")
	@PostMapping
	public RestFulResponse<String> udpateCategoryMap(@RequestBody MaterialCategoryMapCriteria categoryMapCriteria) {
		
		String msg = materialCateoryMapSvc.udpateCategoryMap(categoryMapCriteria);
		if(msg == null) {
			return success("保存成功！");
		} else {
			return error("保存失败！" + msg);
		}
	}

	@LogRecord("查询物料类别关系")
	@GetMapping
	public RestFulResponse<MaterialCategoryMap> categoryMaterial(MaterialCategoryMapCriteria categoryMapCriteria) {
		List<MaterialCategoryMap> results = materialCateoryMapSvc.getMapList(categoryMapCriteria);
		return success(results.size(), results);
	}
	
}
