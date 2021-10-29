package com.cnbaosi.cabinet.serivce.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.entity.criteria.MaterialRemindCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialStockCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialRemind;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.entity.modal.dto.StockDetailDto;
import com.cnbaosi.cabinet.mapper.MaterialRemindMapper;
import com.cnbaosi.cabinet.mapper.MaterialStockMapper;
import com.cnbaosi.cabinet.serivce.MaterialRemindService;
import com.cnbaosi.cabinet.serivce.ShiroService;
import com.cnbaosi.cabinet.util.StringUtil;

/**
 * 物料提醒方法实现类
 * 
 * @author Yifeng Wang  
 */
@Service
public class MaterialRemindServiceImpl extends ServiceImpl<MaterialRemindMapper, MaterialRemind> implements MaterialRemindService{
	
	@Autowired
	private ShiroService shiroSvc;
	@Autowired
	private MaterialStockMapper materialStockMapper;
	
	@Override
	public Page<MaterialRemind> getMaterialRemindPageList(MaterialRemindCriteria matRemindCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria) {
		
		String text = matRemindCriteria.getText();
		
		Integer curPage = pageCriteria.getCurPage();
		Integer pageSize = pageCriteria.getPageSize();
		
		String sortField = sortCriteria.getSortField();
		String sortOrder = sortCriteria.getSortOrder();

		EntityWrapper<MaterialRemind> wrapper = new EntityWrapper<>();
		
		if(StringUtils.isNotBlank(text)) { //多字段匹配查找
			text = text.trim();
			wrapper.like("name", text).or().like("no", text).or().like("spec", text);
		}
		
		if(sortField != null) {
			wrapper.orderBy(StringUtil.camelToUnderline(sortField), sortOrder.equals(SortCriteria.AES));
		} else {
			wrapper.orderBy("no", true);
		}
		
		Page<MaterialRemind> page = new Page<>(curPage, pageSize);
		return super.selectPage(page, wrapper);
	}

	@Override
	public List<MaterialRemind> getMaterialRemindList(MaterialRemindCriteria materialRemindCriteria) {
		
		String materialId = materialRemindCriteria.getMaterialId();
		
		EntityWrapper<MaterialRemind> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(materialId)) {
			wrapper.like("material_id", materialId);
		}
		return super.selectList(wrapper);
	}
	
	@Override
	public Boolean addRemind(MaterialRemind materialRedmind) {
		
		//先判断提醒表中是否有相同物料
		String materialId = materialRedmind.getMaterialId();
		MaterialRemindCriteria materialRemindCriteria = new MaterialRemindCriteria();
		materialRemindCriteria.setMaterialId(materialId);
		List<MaterialRemind> list = this.getMaterialRemindList(materialRemindCriteria);
		if(CollectionUtils.isNotEmpty(list)) {
			return false;
		}
		
		//添加
		User user = shiroSvc.getUser();
		materialRedmind.setCreatedUserFullname(user.getFullname());
		materialRedmind.setCreateTime(new Date());
		return super.insert(materialRedmind);
	}

	@Override
	public boolean removeFromRemindList(String materialId) {
		MaterialRemindCriteria materialRemindCriteria = new MaterialRemindCriteria();
		materialRemindCriteria.setMaterialId(materialId);
		List<MaterialRemind> result = getMaterialRemindList(materialRemindCriteria);
		if(CollectionUtils.isNotEmpty(result)) {
			for(MaterialRemind mr : result) {
				super.deleteById(mr.getId());
			}
		}
		return false;
	}

	@Override
	public boolean checkIfExisted(String materialId) {
		MaterialStockCriteria criteria = new MaterialStockCriteria();
		criteria.setMaterialId(materialId);
		List<StockDetailDto> detailDtoList = materialStockMapper.getStockDetailDto(criteria);
		boolean result = false; //true:已经存在库存 false:暂无此物料库存
		if(CollectionUtils.isNotEmpty(detailDtoList)){
			for(StockDetailDto dto : detailDtoList) {
				if(dto.getAmount() != null && dto.getAmount() > 0) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public void removeIfExist(String materialId) {
		boolean isExisted = this.checkIfExisted(materialId);
		if(isExisted) {
			this.removeFromRemindList(materialId);
		}
	}


}

