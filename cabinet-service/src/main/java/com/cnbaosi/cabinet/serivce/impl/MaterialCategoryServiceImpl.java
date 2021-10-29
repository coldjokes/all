package com.cnbaosi.cabinet.serivce.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryMapCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialCategory;
import com.cnbaosi.cabinet.entity.modal.MaterialCategoryMap;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;
import com.cnbaosi.cabinet.mapper.MaterialCategoryMapper;
import com.cnbaosi.cabinet.serivce.MaterialCategoryMapService;
import com.cnbaosi.cabinet.serivce.MaterialCategoryService;
import com.cnbaosi.cabinet.util.DateTimeUtil;
import com.google.common.collect.Lists;

/**
 * 物料方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class MaterialCategoryServiceImpl extends ServiceImpl<MaterialCategoryMapper, MaterialCategory> implements MaterialCategoryService{

	private static final Logger log = LoggerFactory.getLogger(MaterialCategoryServiceImpl.class);

	@Autowired
	private MaterialCategoryMapService materialCategoryMapSvc;
	
	@Override
	public Boolean addCategory(MaterialCategory category) {
		return super.insert(category);
	}

	@Override
	public String deleteCategory(String id) {
		String msg = null;
		if(id.equals("-1")) {
			msg = "根节点不能删除！";
		} else {
			
			MaterialCategoryMapCriteria categoryMapCriteria = new MaterialCategoryMapCriteria();
			categoryMapCriteria.setCategoryId(id);
			List<MaterialCategoryMap> mapList = materialCategoryMapSvc.getMapList(categoryMapCriteria);
			
			if(CollectionUtils.isNotEmpty(mapList)) {
				msg = "该类别下还有绑定物料，请解绑后再删除此类别！";
			} else {
				//查询是否有子类别
				MaterialCategoryCriteria categoryCriteria = new MaterialCategoryCriteria();
				categoryCriteria.setpId(id);
				List<MaterialCategory> categoryList = this.getMaterialCategory(categoryCriteria);
				if(CollectionUtils.isNotEmpty(categoryList)) {
					msg = "该类别下还有子类别，请删除后再删除此类别！";
				} else {
					MaterialCategory category = new MaterialCategory(id);
					category.setDeleteTime(DateTimeUtil.now());
					super.updateById(category);
				}
			}
		}
		return msg;
	}

	@Override
	public Boolean updateCategory(MaterialCategory category) {
		category.setUpdateTime(DateTimeUtil.now());
		return super.updateById(category);
	}

	@Override
	public List<MaterialCategory> getMaterialCategory(MaterialCategoryCriteria categoryCriteria) {
		String id = categoryCriteria.getId();
		String pId = categoryCriteria.getpId();
		String text = categoryCriteria.getText();
		
		EntityWrapper<MaterialCategory> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(id)) {
			wrapper.like("id", id);
		}
		if(StringUtils.isNotBlank(pId)) {
			wrapper.like("p_id", pId);
		}
		if(StringUtils.isNotBlank(text)) {
			wrapper.eq("text", text);
		}
		wrapper.isNull("delete_time");
		wrapper.orderBy("create_time", true);
		
		return super.selectList(wrapper);
	}

	//-----------------------类别列表-------------------//
	@Override
	public List<TreeNodes> getMaterialCategoryTree() {
		MaterialCategoryCriteria categoryCriteria = new MaterialCategoryCriteria();
		List<MaterialCategory> source = this.getMaterialCategory(categoryCriteria);
		
		List<TreeNodes> result = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(source)) {
			
			for(MaterialCategory category : source) {
				String id = category.getId();
				if(id.equals("-1")) {
					TreeNodes tn = new TreeNodes(id, category.getpId(), category.getText());
					
					tn.setChildren(this.getChildrenNode(id, source));
					result.add(tn);
				}
			}
		}
		return result;
	}

	@Override
	public List<TreeNodes> getChildrenNode(String id, List<MaterialCategory> source) {
		List<TreeNodes> newTrees = Lists.newArrayList();
        for (MaterialCategory category : source) {
            if (category.getpId() != null && category.getpId().equals(id) && !category.getId().equals(id)) {
                TreeNodes tn = new TreeNodes(category.getId(), category.getpId(), category.getText());
                //递归获取子节点下的子节点，即设置树控件中的children
                tn.setChildren(getChildrenNode(category.getId(), source));
                newTrees.add(tn);
            }
        }
		return newTrees;
	}

	@Override
	public List<MaterialCategory> getAllSonCategory(String id) {
		MaterialCategoryCriteria categoryCriteria = new MaterialCategoryCriteria();
		List<MaterialCategory> sourceList = this.getMaterialCategory(categoryCriteria);
		List<MaterialCategory> resultList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(sourceList)) {
			List<MaterialCategory> sonCategoryList = sourceList.stream().filter(category -> {
				return id.equals(category.getpId());
			}).collect(Collectors.toList());
			
			resultList.addAll(sonCategoryList);
			getSons(resultList, sourceList, sonCategoryList);
		}
		return resultList;
	}

	private void getSons(List<MaterialCategory> resultList, List<MaterialCategory> sourceList, List<MaterialCategory> matCategoryList) {
		if(CollectionUtils.isNotEmpty(matCategoryList)) {
			for(MaterialCategory matCategory : matCategoryList) {
				String sonId = matCategory.getId();
				List<MaterialCategory> sonSonCategoryList = sourceList.stream().filter(category -> {
					return sonId.equals(category.getpId());
				}).collect(Collectors.toList());
				resultList.addAll(sonSonCategoryList);
				getSons(resultList, sourceList, sonSonCategoryList);
			}
		}
	}
	
}

