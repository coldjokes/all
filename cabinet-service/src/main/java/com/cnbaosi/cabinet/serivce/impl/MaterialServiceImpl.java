package com.cnbaosi.cabinet.serivce.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cnbaosi.cabinet.config.CabinetServiceConfig;
import com.cnbaosi.cabinet.entity.AppConsts;
import com.cnbaosi.cabinet.entity.criteria.MaterialCategoryMapCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialCriteria;
import com.cnbaosi.cabinet.entity.criteria.MaterialRemindCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.SortCriteria;
import com.cnbaosi.cabinet.entity.enums.SourceEnum;
import com.cnbaosi.cabinet.entity.modal.Material;
import com.cnbaosi.cabinet.entity.modal.MaterialCategoryMap;
import com.cnbaosi.cabinet.entity.modal.MaterialRemind;
import com.cnbaosi.cabinet.mapper.MaterialMapper;
import com.cnbaosi.cabinet.serivce.MaterialCategoryMapService;
import com.cnbaosi.cabinet.serivce.MaterialRemindService;
import com.cnbaosi.cabinet.serivce.MaterialService;
import com.cnbaosi.cabinet.serivce.MesService;
import com.cnbaosi.cabinet.util.DateTimeUtil;
import com.cnbaosi.cabinet.util.ExcelUtil;
import com.cnbaosi.cabinet.util.FileUnZipRarUtil;
import com.cnbaosi.cabinet.util.FileUtil;
import com.cnbaosi.cabinet.util.StringUtil;
import com.google.common.collect.Lists;

/**
 * ?????????????????????
 * 
 * @author Yifeng Wang  
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService{

	private static final Logger log = LoggerFactory.getLogger(MaterialServiceImpl.class);
	
	@Autowired
	private CabinetServiceConfig config;
	@Autowired
	private MaterialCategoryMapService materialCategoryMapSvc;
	@Autowired
	private MesService mesSvc;
	@Autowired
	private MaterialRemindService remindSvc;
	
	@Override
	public boolean addMaterial(Material mat) {
		mat.setSource(SourceEnum.SYSTEM.getCode());
		return super.insert(mat);
	}

	@Override
	public boolean addMaterials(List<Material> mats) {
		return super.insertBatch(mats, mats.size());
	}

	@Override
	public String importMats(MultipartFile file) {
		
		String fileFullName = file.getOriginalFilename();
		
		int pos = fileFullName.lastIndexOf(".");
        String fileName = fileFullName.substring(0, pos).toLowerCase();
        
		//????????????????????????
		String uploadPath = config.getProjectLocation() + config.getTempPath();
		File updatePathFolder = new File(uploadPath);
		if(!updatePathFolder.exists()) {
			updatePathFolder.mkdir();
		}
		
		try {
			file.transferTo(new File(uploadPath + fileFullName));
		} catch (IllegalStateException | IOException e2) {
			e2.printStackTrace();
		} 
		
		//??????
		try {
			FileUnZipRarUtil.zipRarToFile(fileFullName, uploadPath + fileFullName, uploadPath);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//?????????????????????????????????
		String sourceFileFolder = uploadPath + File.separator + fileName + File.separator;
		
		String msg = null;
		List<Material> importMatList = Lists.newArrayList();
		
		//??????excel??????
		File sourceExcelFile = new File(sourceFileFolder + fileName + ".xls");
		if(sourceExcelFile.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(sourceExcelFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			List<List<Object>> results = ExcelUtil.executeExcel(fis);
			
			//?????????????????????
			Integer source = SourceEnum.FILE.getCode();
			
			String targetPath =  config.getProjectLocation() + config.getPicturePath();
			File targetPathFile = new File(targetPath);
			if(!targetPathFile.exists()) {
				targetPathFile.mkdirs();
			}
			
			//?????????????????????????????????????????????
			if(CollectionUtils.isNotEmpty(results)) {
				
				
				List<Material> compareMaterialList = Lists.newArrayList();
				
				for(List<Object> cellList : results) {
					
					//excel??????
					String name = String.valueOf(cellList.get(0));
					String no = String.valueOf(cellList.get(1));
					String spec = String.valueOf(cellList.get(2));
					String warnValStr = String.valueOf(cellList.get(3));
					String pictureName = String.valueOf(cellList.get(4));
					String remark = String.valueOf(cellList.get(5));

					//????????????
					name = AppConsts.NULL.equals(name) ? null : name;
					no = AppConsts.NULL.equals(no) ? null : no;
					spec = AppConsts.NULL.equals(spec) ? null : spec;
					warnValStr = AppConsts.NULL.equals(warnValStr) ? null : warnValStr;
					pictureName = AppConsts.NULL.equals(pictureName) ? null : pictureName;
					remark = AppConsts.NULL.equals(remark) ? null : remark;
					
					if(StringUtils.isNotBlank(name) && StringUtils.isNotBlank(no) && StringUtils.isNotBlank(spec) ) {
						Material mat = new Material();
						
						mat.setName(name);
						mat.setNo(no);
						mat.setSpec(spec);
						mat.setRemark(remark);
						
						boolean selfHasSameMaterial = false; //excel?????????????????????????????????????????????
						if(CollectionUtils.isNotEmpty(compareMaterialList)) {
							for(Material m : compareMaterialList) {
								if(m.equals(mat)) {
									selfHasSameMaterial = true;
									break;
								}
							}
						}
						
						if(selfHasSameMaterial) {
							msg = "?????????????????????????????????????????????" + no + "????????????" + spec + "???????????????????????????";
							break;
						} else {
							compareMaterialList.add(mat);
						}
						
						
						//??????????????????????????????????????????????????????????????????
						Material ma = this.getMaterialByNoAndSpec(no, spec);
						if(ma != null) {
							msg = "???????????????????????????????????????" + no + "????????????" + spec + "???????????????????????????";
							break;
						}
						
						if(name.length() > 100) {
							msg = "???????????????" + name + "????????????????????????????????????????????????100?????????";
							break;
						}
						
						if(no.length() > 100) {
							msg = "???????????????" + no + "????????????????????????????????????????????????100?????????";
							break;
						}
						
						if(spec.length() > 100) {
							msg = "???????????????" + spec + "????????????????????????????????????????????????100?????????";
							break;
						}
						
						if(StringUtils.isNotBlank(warnValStr)) {
							if(StringUtil.isInteger(warnValStr)) {
								Integer warnVal = Integer.valueOf(warnValStr);
								if(warnVal >= 100) {
									msg = "?????????" + name + "???????????????????????????????????????????????????????????????99??????";
									break;
								} else {
									mat.setWarnVal(warnVal);
									
								}
							} else {
								msg = "?????????" + name + "????????????????????????????????????????????????";
								break;
							}
						} else {
							mat.setWarnVal(null);
						}
						
						
						//????????????
						if(StringUtils.isNotBlank(pictureName)) {
							String finalPicName = null;
							try {
								finalPicName = FileUtil.getNewFileName(pictureName);
							} catch (Exception e) {
								e.printStackTrace();
								msg = "??????????????????????????????" + pictureName + "???";
								break;
							}
							
							File sourceFile = new File(sourceFileFolder + pictureName);
							
							//??????????????????
							if(!sourceFile.exists()) {
								msg = "??????" + sourceFileFolder + pictureName + "????????????";
								break;
							}
							
							File targetFile = new File(targetPath + finalPicName);
							try {
								FileUtil.copyFileUsingFileChannels(sourceFile, targetFile);
								mat.setPicture(config.getPicturePath() + finalPicName);
							} catch (IOException e) {
								e.printStackTrace();
								msg = "??????" + pictureName + "???????????????";
								break;
							}
						}
						
						mat.setSource(source);
						
						importMatList.add(mat);
					} else {
						msg = "???????????????????????????";
						break;
					}
				}
			} else {
				msg = "???????????????????????????";
			}
		} else {
			msg = "?????????EXCEL??????";
		}
		
		//?????????????????????????????????????????????zip??????????????????
		FileUtil.deleteFolder(uploadPath + fileName);
		FileUtil.deleteFile(uploadPath, fileFullName);
		
		//??????
		if(msg == null) {
			this.addMaterials(importMatList);
		}
		return msg;
	}
	
	@Override
	public boolean deleteMaterial(String id) {
		Material mat = new Material(id);
		mat.setDeleteTime(DateTimeUtil.now());
		
		//??????????????????
		MaterialRemindCriteria materialRemindCriteria = new MaterialRemindCriteria();
		materialRemindCriteria.setMaterialId(id);
		List<MaterialRemind> rmList = remindSvc.getMaterialRemindList(materialRemindCriteria);
		if(CollectionUtils.isNotEmpty(rmList)) {
			MaterialRemind rm = rmList.get(0);
			remindSvc.deleteById(rm.getId());
		}
		
		return super.updateById(mat);
	}

	@Override
	public boolean updateMaterial(Material mat) {
		mat.setUpdateTime(DateTimeUtil.now());
		return super.updateById(mat);
	}
	
	@Override
	public Page<Material> getMaterials(MaterialCriteria matCriteria, PageCriteria pageCriteria, SortCriteria sortCriteria) {
		String text = matCriteria.getText();
		Integer source = matCriteria.getSource();
		List<String> idList = matCriteria.getIdList();
		List<String> noList = matCriteria.getNoList();
		List<String> nameList = matCriteria.getNameList();
		
		Integer curPage = pageCriteria.getCurPage();
		Integer pageSize = pageCriteria.getPageSize();
		
		String sortField = sortCriteria.getSortField();
		String sortOrder = sortCriteria.getSortOrder();

		EntityWrapper<Material> wrapper = new EntityWrapper<>();
		
		if(CollectionUtils.isNotEmpty(idList)) {
			wrapper.in("id", idList);
		}
		if(CollectionUtils.isNotEmpty(noList)) {
			wrapper.in("no", noList);
		}
		if(CollectionUtils.isNotEmpty(nameList)) {
			wrapper.in("name", nameList);
		}
		
		if(StringUtils.isNotBlank(text)) { //?????????????????????
			text = text.trim();
			wrapper.like("name", text).or().like("no", text).or().like("spec", text);
		}
		wrapper.andNew();
		if(source != null && source != -1) {
			wrapper.eq("source", source);
		}
		if(sortField != null) {
			wrapper.orderBy(StringUtil.camelToUnderline(sortField), sortOrder.equals(SortCriteria.AES));
		} else {
			wrapper.orderBy("no", true);
		}
		
		wrapper.isNull("delete_time");
		
		Page<Material> page = new Page<>(curPage, pageSize);
		return super.selectPage(page, wrapper);
	}
	
	@Override
	public String saveMatImage(MultipartFile file, String picPath) {
		String projectLocation = config.getProjectLocation();
		String fileName = FileUtil.saveFile(projectLocation + picPath, file);
		return picPath + fileName;
	}
	
	@Override
	public void exportMateria(MaterialCriteria matCriteria, HttpServletRequest request, HttpServletResponse response) {
		String text = matCriteria.getText();
		Integer source = matCriteria.getSource();
		EntityWrapper<Material> wrapper = new EntityWrapper<>();
		if(StringUtils.isNotBlank(text)) {
			text = text.trim();
			wrapper.like("name", text).or().like("no", text).like("spec", text);
		}
		if(source != null && source != -1) {
			wrapper.eq("source", source);
		}
		wrapper.orderBy("no", true);
		wrapper.isNull("delete_time");
		List<Material> results = super.selectList(wrapper);
		
		String sheetName = "????????????";
		
		String[] header = {"??????", "????????????", "????????????", "????????????", "?????????", "??????"};
		
		List<List<String>> fieldDataList = new ArrayList<>();
		
        for (int i = 0; i < results.size(); i ++) {
        	
        	List<String> rowValue = Lists.newArrayList();
        	Material material = results.get(i);
        	
        	rowValue.add(i + 1 + "");
        	rowValue.add(material.getName());
        	rowValue.add(material.getNo());
        	rowValue.add(material.getSpec());
        	rowValue.add(material.getWarnVal() != null ? material.getWarnVal() + "" : "");
        	rowValue.add(material.getRemark());
        	
        	fieldDataList.add(rowValue);
        }
        
        List<String> sheetNames = Lists.newArrayList(sheetName);
        Map<String, List<String>> fieldNames = new HashMap<>();
        Map<String, List<List<String>>> fieldDataMap = new HashMap<>();
        
        fieldNames.put(sheetName, Arrays.asList(header));
        fieldDataMap.put(sheetName, fieldDataList);
        
        ExcelUtil exportUtil = new ExcelUtil(fieldNames, fieldDataMap);
        exportUtil.createWorkbook(sheetNames);
        String filename = ExcelUtil.processFileName(request, "????????????.xls");
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        try {
			exportUtil.expordExcel(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Material getMaterialByNoAndSpec(String no, String spec) {
		EntityWrapper<Material> wrapper = new EntityWrapper<>();
		wrapper.eq("no", no);
		wrapper.eq("spec", spec);
		wrapper.isNull("delete_time");
		return super.selectOne(wrapper);
	}

	@Override
	public List<Material> getMaterialByCategoryId(String categoryId) {
		MaterialCriteria matCriteria = new MaterialCriteria();
		PageCriteria pageCriteria = new PageCriteria();
		SortCriteria sortCriteria = new SortCriteria();
		
		if(categoryId != null && !"#".equals(categoryId) && !"-1".equals(categoryId)) {
			MaterialCategoryMapCriteria categoryMapCriteria = new MaterialCategoryMapCriteria();
			categoryMapCriteria.setCategoryId(categoryId);
			List<MaterialCategoryMap> mapList = materialCategoryMapSvc.getMapList(categoryMapCriteria);
			if(CollectionUtils.isNotEmpty(mapList)) {
				List<String> materialIdList = mapList.stream().map(x -> x.getMaterialId()).collect(Collectors.toList());
				matCriteria.setIdList(materialIdList);
			} else {
				return Lists.newArrayList();
			}
		}
		Page<Material> materialList = this.getMaterials(matCriteria, pageCriteria, sortCriteria);
		return materialList.getRecords();
	}

	@Override
	public List<Material> getMaterialByScanCode(String code) {
		
		List<Material> resutls = Lists.newArrayList();
		
		//?????????????????????????????????
		String materialNames = mesSvc.getMaterialNamesByCode(code);
		
		if(StringUtils.isNoneBlank(materialNames)) {
			
			//????????????????????? | ??????
			String[] names = materialNames.split("???");
			
			if(names != null && names.length > 0) {
				List<String> nameList = Lists.newArrayList(names);
				MaterialCriteria matCriteria = new MaterialCriteria();
				matCriteria.setNameList(nameList);
				Page<Material> materialList = this.getMaterials(matCriteria, new PageCriteria(), new SortCriteria());
				resutls = materialList.getRecords();
			}
		}
		return resutls;
	}
}

