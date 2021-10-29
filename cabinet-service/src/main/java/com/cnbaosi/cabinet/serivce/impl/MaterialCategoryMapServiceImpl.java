package com.cnbaosi.cabinet.serivce.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryMapCriteria;
import com.cnbaosi.cabinet.entity.modal.Material;
import com.cnbaosi.cabinet.entity.modal.MaterialCategory;
import com.cnbaosi.cabinet.entity.modal.MaterialCategoryMap;
import com.cnbaosi.cabinet.mapper.MaterialCategoryMapMapper;
import com.cnbaosi.cabinet.serivce.MaterialCategoryMapService;
import com.cnbaosi.cabinet.serivce.MaterialCategoryService;
import com.cnbaosi.cabinet.serivce.MaterialService;
import com.google.common.collect.Lists;

/**
 * 物料类别映射方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class MaterialCategoryMapServiceImpl extends ServiceImpl<MaterialCategoryMapMapper, MaterialCategoryMap> implements MaterialCategoryMapService{

	private static final Logger log = LoggerFactory.getLogger(MaterialCategoryServiceImpl.class);
	
	@Autowired
	private MaterialService materialSvc;

	@Autowired
	private MaterialCategoryService materialCategorySvc;
	
	@Override
	public String udpateCategoryMap(MaterialCategoryMapCriteria categoryMapCriteria) {

		String msg = null;
		
		String categoryId = categoryMapCriteria.getCategoryId();
		List<String> materialIdList = categoryMapCriteria.getMaterialIdList();
		
		if(StringUtils.isNotBlank(categoryId)) {
			
			boolean isSonReleated = false;
			List<String> sonReleatedList = Lists.newArrayList();
			
			//解绑前，先确定旗下子类别有没有绑定相同的物料，如果有，则不能解绑。
			//1.获取所有子分类，包括子子分类
			List<MaterialCategory> sonCategoryList = materialCategorySvc.getAllSonCategory(categoryId);
			if(CollectionUtils.isNotEmpty(sonCategoryList)) {
				
				//2.通过所有子分类id查找出子分类关联的物料信息
				List<String> sonMaterialCategoryList = sonCategoryList.stream().map(MaterialCategory :: getId).collect(Collectors.toList());
				MaterialCategoryMapCriteria sonCategoryMapCriteria = new MaterialCategoryMapCriteria();
				sonCategoryMapCriteria.setCategoryIdList(sonMaterialCategoryList);
				List<MaterialCategoryMap> sonMaterialCategoryMapList = this.getMapList(sonCategoryMapCriteria);
				
				
				if(CollectionUtils.isNotEmpty(sonMaterialCategoryMapList)) {
					Set<String> sonMaterialIdList = sonMaterialCategoryMapList.stream().map(MaterialCategoryMap :: getMaterialId).collect(Collectors.toSet());
					
					//3.如果子分类下的物料信息不为空，则就需要开始比较了。获取选中分类下原来的物料信息
					MaterialCategoryMapCriteria oldCategoryMapCriteria = new MaterialCategoryMapCriteria();
					oldCategoryMapCriteria.setCategoryId(categoryId);
					List<MaterialCategoryMap> oldCategoryMapList = this.getMapList(oldCategoryMapCriteria);
					List<String> oldMaterialIdList = oldCategoryMapList.stream().map(MaterialCategoryMap :: getMaterialId).collect(Collectors.toList());
					
					//4.比较本次页面上保存的物料id和原来该分类下的物料id，获取本次保存时去除的物料id
					List<String> removedIdList = oldMaterialIdList.stream().filter(oldId -> {
						String i = materialIdList.stream().filter(newId -> newId.equals(oldId)).findFirst().orElse(null);
						return i == null;
					}).collect(Collectors.toList());

					
					//5.查找子分类下，有没有此次去除的物料id，如果有，则说明子分类下也绑定了此物料。因此不能解绑
					sonReleatedList = sonMaterialIdList.stream().filter(sonMaterialId -> {
						String i = removedIdList.stream().filter(removedId -> removedId.equals(sonMaterialId)).findFirst().orElse(null);
						return i != null;
					}).collect(Collectors.toList());

					//6. 如果子分类下有关联，则直接提示信息解绑失败。若无关联，则继续更新操作
					isSonReleated = CollectionUtils.isNotEmpty(sonReleatedList);
				}
			}
			if(!isSonReleated) {
				//删除当前类别所绑定的所有物料
				EntityWrapper<MaterialCategoryMap> wrapper = new EntityWrapper<>();
				wrapper.eq("category_id", categoryId);
				super.delete(wrapper);
				
				//插入物料类别关系
				if(CollectionUtils.isNotEmpty(materialIdList)) {
					List<MaterialCategoryMap> mapList = Lists.newArrayList();
					for(String materialId : materialIdList) {
						MaterialCategoryMap map = new MaterialCategoryMap();
						map.setCategoryId(categoryId);
						map.setMaterialId(materialId);
						mapList.add(map);
					}
					 this.addMaterialCategoryMaps(mapList);
				}
			} else {
				String materialId = sonReleatedList.get(0);
				Material releatedMa = materialSvc.selectById(materialId);
				msg = "物料【" + releatedMa.getName() + "】在子分类中也被关联了，请先解绑子分类中的物料信息后再操作";
			}
		}
		return msg;
	}
	
	@Override
	public List<MaterialCategoryMap> getMapList(MaterialCategoryMapCriteria categoryMapCriteria) {
		
		String categoryId = categoryMapCriteria.getCategoryId();
		List<String> categoryIdList = categoryMapCriteria.getCategoryIdList();
		
		EntityWrapper<MaterialCategoryMap> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(categoryId)) {
			wrapper.eq("category_id", categoryId);
		}
		if(CollectionUtils.isNotEmpty(categoryIdList)) {
			wrapper.in("category_id", categoryIdList);
		}
		wrapper.orderBy("create_time", true);
		return super.selectList(wrapper);
	}

	@Override
	public boolean addMaterialCategoryMaps(List<MaterialCategoryMap> maps) {
		return super.insertBatch(maps);
	}

	

}

