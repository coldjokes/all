package com.cnbaosi.cabinet.controller.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.cnbaosi.cabinet.aop.log.LogRecord;
import com.cnbaosi.cabinet.config.CabinetServiceConfig;
import com.cnbaosi.cabinet.controller.BaseController;
import com.cnbaosi.cabinet.entity.RestFulResponse;
import com.cnbaosi.cabinet.entity.criteria.MaterialCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.modal.Material;
import com.cnbaosi.cabinet.serivce.MaterialService;
import com.google.common.collect.Lists;

/**
  * 物料
 * 
 * @author Yifeng Wang  
 */

@RestController
@RequestMapping("/api/materials")
public class MaterialController extends BaseController {

	@Autowired
	private MaterialService matSvc;
	@Autowired
	private CabinetServiceConfig config;
	
	@LogRecord("增加物料")
	@PostMapping
	public RestFulResponse<Material> addMat(@RequestBody Material mat) {
		return actionResult(matSvc.addMaterial(mat));
	}
	
	@LogRecord("删除物料")
	@DeleteMapping("/{id}")
	public RestFulResponse<String> deleteMat(@PathVariable("id") String id) {
		return actionResult(matSvc.deleteMaterial(id));
	}
	
	@LogRecord("修改物料")
	@PutMapping
	public RestFulResponse<Material> updateMat(@RequestBody Material mat) {
		return actionResult(matSvc.updateMaterial(mat));
	}
	
	@LogRecord("物料编号规格校验")
	@GetMapping("/noSpecCheck")
	public RestFulResponse<Material> noAndSpecCheck(@RequestParam String no, @RequestParam String spec) {
		Material ma = matSvc.getMaterialByNoAndSpec(no, spec);
		return success(ma != null ? 1: 0, Lists.newArrayList(ma));
	}
	
	@LogRecord("查询物料列表")
	@GetMapping
	public RestFulResponse<Material> getMaterials(MaterialCriteria matCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria) {
		Page<Material> pagedMaterials = matSvc.getMaterials(matCriteria, pageCriteria, sortCriteria);
		return success(pagedMaterials.getTotal(), pagedMaterials.getRecords());
	}
	
	@LogRecord("根据类别查询物料列表")
	@GetMapping("/byCategory")
	public RestFulResponse<Material> getMaterialsByCategory(String categoryId) {
		List<Material> results = matSvc.getMaterialByCategoryId(categoryId);
		return success(results.size(), results);
	}
	
	@LogRecord("增加物料图片")
	@PostMapping("/picture")
	public RestFulResponse<String> addMatPicUrl(@RequestParam("prePicture") MultipartFile prePicture) {
		String path = matSvc.saveMatImage(prePicture, config.getPicturePath());
		return success(1, Lists.newArrayList(path));
	}
	
	@LogRecord("增加物料图纸")
	@PostMapping("/blueprint")
	public RestFulResponse<String> addBlueprint(@RequestParam("preBlueprint") MultipartFile blueprintFile) {
		String path = matSvc.saveMatImage(blueprintFile, config.getBlueprintPath());
		return success(1, Lists.newArrayList(path));
	}
	
//	@LogRecord("删除物料图纸")
	@PostMapping("/blueprint/delete")
	public RestFulResponse<String> deleteBlueprint() {
		return success(1, Lists.newArrayList());
	}
	
	@LogRecord("导出物料列表")
	@GetMapping("export")
	public void exportMaterials(MaterialCriteria matCriteria, HttpServletRequest request, HttpServletResponse response) {
		matSvc.exportMateria(matCriteria, request, response);
	}
	
	@LogRecord("物料导入")
	@PostMapping("/import")
	public RestFulResponse<String> userUpload(@RequestParam("file") MultipartFile file) {
		String msg = matSvc.importMats(file);
		if(msg == null) {
			return success("导入成功！");
		} else {
			return error("导入失败！" + msg);
		}
	}
}

