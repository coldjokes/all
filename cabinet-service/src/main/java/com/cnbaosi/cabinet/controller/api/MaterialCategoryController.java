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
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialCategory;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;
import com.cnbaosi.cabinet.serivce.MaterialCategoryService;

/**
 * 物料类别
 * 
 * @author Yifeng Wang  
 */
@RestController
@RequestMapping("/api/materials/category")
public class MaterialCategoryController extends BaseController {

	@Autowired
	private MaterialCategoryService materialCateorySvc;

	@LogRecord("增加物料类别")
	@PostMapping
	public RestFulResponse<String> addCategory(@RequestBody MaterialCategory category) {
		return actionResult(materialCateorySvc.addCategory(category));
	}
	
	@LogRecord("删除物料类别")
	@DeleteMapping("/{id}")
	public RestFulResponse<String> deleteCategory(@PathVariable("id") String id) {
		String msg = materialCateorySvc.deleteCategory(id);
		if(msg == null) {
			return success("删除成功！");
		} else {
			return error("删除失败！" + msg);
		}
	}
	
	@LogRecord("修改物料类别")
	@PutMapping
	public RestFulResponse<String> updateCategory(@RequestBody MaterialCategory category) {
		return actionResult(materialCateorySvc.updateCategory(category));
	}

	@LogRecord("查询物料类别列表")
	@GetMapping
	public RestFulResponse<MaterialCategory> categoryMaterial(MaterialCategoryCriteria categoryCriteria) {
		List<MaterialCategory> results = materialCateorySvc.getMaterialCategory(categoryCriteria);
		return success(results.size(), results);
	}
	
	@LogRecord("查询物料类别树")
	@GetMapping("tree")
	public RestFulResponse<TreeNodes> categoryTree() {
		List<TreeNodes> results = materialCateorySvc.getMaterialCategoryTree();
		return success(results.size(), results);
	}
	
}
