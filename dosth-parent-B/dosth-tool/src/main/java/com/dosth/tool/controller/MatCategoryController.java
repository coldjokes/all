package com.dosth.tool.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cnbaosi.dto.OpTip;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.base.tips.Tip;
import com.dosth.common.constant.TableSelectType;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.controller.BaseController;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.StringUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.dto.MatAssociationInfo;
import com.dosth.tool.common.warpper.MatCategoryPageWarpper;
import com.dosth.tool.entity.MatCategory;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.service.MatCategoryService;

/**
 * 物料关联设置Service
 * 
 * @author chenlei
 *
 */
@Controller
@RequestMapping("/matCategory")
public class MatCategoryController extends BaseController{
	private static String PREFIX = "/tool/matCategory/";
	
	@Autowired
	private MatCategoryService matCategoryService;
	
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	
	@Autowired
	private MatCategoryRepository matCategoryRepository;
	
	/**
	 * 跳转到业务主页面
	 */
	@RequestMapping("")
	public String index(Model model) {
		model.addAttribute("columns", new MatCategoryPageWarpper(null).createColumns(TableSelectType.BOX));
		return PREFIX + "index.html";
	}
	
	/**
	 * @Desc 创建tree
	 */
	@RequestMapping("/categoryTree")
	@ResponseBody
	public List<ZTreeNode> categoryTree() {
		List<ZTreeNode> tree = this.matCategoryService.categoryTree();
		return tree;
	}
	
	/**
	 * @Desc 增加节点
	 */
	@RequestMapping("/nodeAdd/{pId}/{level}/{name}/{path}")
	@ResponseBody
	public MatCategoryTree nodeAdd(@PathVariable String pId, @PathVariable String level, 
			@PathVariable String name, @PathVariable String[] path) {
		return this.matCategoryService.nodeAdd(pId, level, name, StringUtils.join(path,","));
	}
	
	/**
	 * @Desc 更改节点名
	 */
	@RequestMapping("/editName/{nodeId}/{name}")
	@ResponseBody
	public void editName(@PathVariable String nodeId, @PathVariable String name) {
		this.matCategoryService.editName(nodeId, name);
	}
	
	/**
	 * @Desc 删除节点
	 */
	@RequestMapping("/nodeDel/{nodeId}")
	@ResponseBody
	public void nodeDel(@PathVariable String nodeId) {
		this.matCategoryService.nodeDel(nodeId);
	}
	
	/**
	 * 获取物料信息
	 * @param 
	 */
	@RequestMapping("/getEquInfos/{ids}")
	public String getEquInfos(@PathVariable String[] ids, Model model) {//treeId,parentTreeId
		Criteria<MatEquInfo> criteria = new Criteria<MatEquInfo>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<MatEquInfo> infos = this.matEquInfoRepository.findAll(criteria);
		model.addAttribute("matEquInfo",resultInfo(infos, ids[0], ids[1]));
		model.addAttribute("netPath", ToolProperties.PREFIX);
		return PREFIX + "infos.html";
	}
	
	/**
	 * 获取物料信息
	 * @param 
	 */
	@RequestMapping("/search/{infos}")
	public String search(@PathVariable String[] infos, Model model) {//searchVal,treeId,parentTreeId
		
		List<MatAssociationInfo> list = new ArrayList<>();
		Criteria<MatEquInfo> c = new Criteria<>();
		c.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		if (infos[0] != null && !"".equals(infos[0])) {
			c.add(Restrictions.or(new Criterion[] { 
					Restrictions.like("matEquName", infos[0].trim(), true),
					Restrictions.like("barCode", infos[0].trim(), true), 
					Restrictions.like("spec", infos[0].trim(), true) }));
		}
		List<MatEquInfo> equInfos = this.matEquInfoRepository.findAll(c);
		
		List<String> treeIds = this.matCategoryRepository.findMatInfoIds(infos[1]);
		if(treeIds != null && treeIds.size() != 0) {
			for(MatEquInfo equInfo : equInfos) {
				if(treeIds != null && treeIds.size() > 0 && !treeIds.contains(equInfo.getId())) {
					continue;
				}
				MatAssociationInfo matAssociationInfo = new MatAssociationInfo();
				matAssociationInfo.setIcon(equInfo.getIcon());
				matAssociationInfo.setMatEquName(equInfo.getMatEquName());
				matAssociationInfo.setBarCode(equInfo.getBarCode());
				matAssociationInfo.setSpec(equInfo.getSpec());
				matAssociationInfo.setId(equInfo.getId());
				matAssociationInfo.setFlag(1);
				list.add(matAssociationInfo);
			}
		}
		
		String flag = infos[3];
		if(StringUtils.isNotBlank(flag) && "1".equals(flag)) {
			model.addAttribute("matEquInfo",list);
		} else {
			model.addAttribute("matEquInfo",resultInfo(equInfos, infos[1], infos[2]));
		}
		
		model.addAttribute("netPath", ToolProperties.PREFIX);
		return PREFIX + "infos.html";
	}
	
	/**
	 * 获取选中信息
	 * @param 
	 */
	@RequestMapping("/dipCheck/{typeTreeId}")
	public String dipCheck(@PathVariable String typeTreeId, Model model) {
		List<MatAssociationInfo> info = this.matCategoryService.getDipCheck(typeTreeId);
		model.addAttribute("matEquInfo",info);
		model.addAttribute("netPath", ToolProperties.PREFIX);
		return PREFIX + "infos.html";
	}
	
	/**
	 * 保存信息
	 * @param 
	 */
	@RequestMapping("/submmit/{typeTreeId}/{checkVar}/{unCheckVar}")
	@BussinessLog(businessName = "添加物料关联")
	@ResponseBody
	public Tip submmit(@PathVariable String typeTreeId, @PathVariable String[] checkVar, 
			@PathVariable String[] unCheckVar, Model model) {
		List<String> matIdList = new ArrayList<>();
		List<String> List = new ArrayList<>(checkVar.length);
		List<String> unCheckList = new ArrayList<>(unCheckVar.length);
		
		List<MatCategory> categoryList = this.matCategoryRepository.findByNode(typeTreeId);
		Collections.addAll(List,checkVar);
		Collections.addAll(unCheckList,unCheckVar);
		for(MatCategory category : categoryList) {
			matIdList.add(category.getMatInfoId());
		}
		//添加新加入的物料
		for(String id : List) {
			MatCategory matCategory = new MatCategory();
			if(matIdList.contains(id) || id.equals("empty")) {
				continue;
			}
			matCategory.setMatInfoId(id);
			matCategory.setStatus(UsingStatus.ENABLE);
			matCategory.setCategoryTreeId(typeTreeId);
			this.matCategoryRepository.save(matCategory);
		}
		
		//删除未选中的物料
		if(!unCheckList.get(0).equals("empty") ) {
			for(MatCategory category : categoryList) {
				if(!unCheckList.contains(category.getMatInfoId())) {
					continue;
				}
				this.matCategoryRepository.delete(category);
			}
		}
		return SUCCESS_TIP;
	}
	
	/**
	 * 返回数据封装
	 * @param 
	 */
	private List<MatAssociationInfo> resultInfo(List<MatEquInfo> infos, String typeTreeId, String parentTreeId) {
		List<String> matIdList = new ArrayList<>();
		List<String> infoIds = null;
		List<MatAssociationInfo> list=new ArrayList<>();
		List<MatCategory> categoryList = this.matCategoryRepository.findByNode(typeTreeId);
		if(StringUtil.isNotBlank(parentTreeId)) {// 获取父类绑定的物料Id
			infoIds = this.matCategoryRepository.findMatInfoIds(parentTreeId);
		}
		for(MatCategory category : categoryList) {// 获取当前节点绑定的物料Id
			matIdList.add(category.getMatInfoId());
		}
		for (MatEquInfo info : infos) {
			if (typeTreeId != null && parentTreeId != null && !typeTreeId.equals(parentTreeId)) {
				if (infoIds != null && infoIds.size() > 0 && !infoIds.contains(info.getId())) {
					// 父类绑定物料的情况，子类的最大选择范围为父类的物料集合
					continue;
				} else if ((infoIds == null || infoIds.size() == 0) && !parentTreeId.equals("")) {
					// 存在父类，并且父类没有绑定物料的情况，子类不表示物料列表
					continue;
				}
			}
			MatAssociationInfo matAssociationInfo = new MatAssociationInfo();
			matAssociationInfo.setFlag(0);
			matAssociationInfo.setMatEquName(info.getMatEquName());
			matAssociationInfo.setBarCode(info.getBarCode());
			matAssociationInfo.setSpec(info.getSpec());
			matAssociationInfo.setId(info.getId());
			matAssociationInfo.setIcon(info.getIcon() == null ? "" : info.getIcon());
			if(matIdList.contains(info.getId())) {
				matAssociationInfo.setFlag(1);
			}
			list.add(matAssociationInfo);
		}
		return list;
	}
	
	/**
	 * @description 校验名称是否重复
	 * @param categoryTreePId 上级Id
	 * @param categoryTreeId 本级Id
	 * @param newName 新名称
	 * @return
	 */
	@RequestMapping("/checkName")
	@ResponseBody
	public OpTip checkName(@RequestParam("categoryTreePId") String categoryTreePId, @RequestParam("categoryTreeId") String categoryTreeId, @RequestParam("newName") String newName) {
		return this.matCategoryService.checkName(categoryTreePId, categoryTreeId, newName);
	}
}