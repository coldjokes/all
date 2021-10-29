package com.cnbaosi.cabinet.serivce;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.MaterialCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.modal.Material;

/**
 * 物料相关方法
 * 
 * @author Yifeng Wang  
 */
public interface MaterialService extends IService<Material> {

	/**
	 * 新增物料
	 * @param mat 物料实体
	 */
	boolean addMaterial(Material mat);
	
	/**
	 * 批量新增物料
	 * @param mat 物料实体
	 */
	boolean addMaterials(List<Material> mats);
	
	/**
	 * 删除物料
	 * @param id 物料id
	 * @return
	 */
	boolean deleteMaterial(String id);

	/**
	 * 导入物料
	 * @param file
	 */
	String importMats(MultipartFile file);
	
	/**
	 * 更新物料
	 * @param 物料 物料实体
	 * @return
	 */
	boolean updateMaterial(Material mat);
	
	/**
	 * 查找物料
	 * @param userCriteria 物料相关条件过滤
	 * @param pageCriteria 分页过滤
	 * @param sortCriteria 排序过滤
	 */
	Page<Material> getMaterials(MaterialCriteria matCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria);

	/**
	 * 增加物料图片及图纸
	 * @param file
	 * @param 图片前缀
	 * @return
	 */
	String saveMatImage(MultipartFile file, String picPath);

	/**
	 * 导出物料列表
	 * @param matCriteria 物料过滤条件
	 * @param response
	 */
	void exportMateria(MaterialCriteria matCriteria, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 通过物料编号和规格去获取物料
	 * @param no
	 * @param spec
	 * @return
	 */
	Material getMaterialByNoAndSpec(String no, String spec);

	
	/**
	 * 通过物料类别获取物料
	 * @param id
	 * @return
	 */
	List<Material> getMaterialByCategoryId(String id);

	/**
	 * 通过扫描码获取物料
	 * @param code 扫描码
	 * @return
	 */
	List<Material> getMaterialByScanCode(String code);
}

