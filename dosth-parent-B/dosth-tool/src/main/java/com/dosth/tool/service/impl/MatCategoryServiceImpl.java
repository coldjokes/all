
package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.common.dto.MatAssociationInfo;
import com.dosth.tool.entity.MatCategory;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatCategoryTreeRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.service.MatCategoryService;
import com.dosth.util.OpTip;

/**
 * 物料关联设置Service实现
 * 
 * @author chenlei
 *
 */
@Service
@Transactional
public class MatCategoryServiceImpl implements MatCategoryService {

	@Autowired
	private MatCategoryTreeRepository matCategoryTreeRepository;
	@Autowired
	private MatCategoryRepository matCategoryRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> categoryTree() throws DoSthException {
		List<ZTreeNode> treeList = new ArrayList<>();
		ZTreeNode node;
		List<MatCategoryTree> categoryList = this.matCategoryTreeRepository.findAll(UsingStatus.ENABLE);
		for (MatCategoryTree category : categoryList) {
			node=new ZTreeNode(category.getId(), category.getpId(), category.getName(), 
					category.getEquType().name(), category.getfId());
			switch (category.getfId().split(",").length) {
			case 1:
				node.setIcon("/static/img/two.png");
				break;
			case 2:
				node.setIcon("/static/img/three.png");
				break;
			case 3:
				node.setIcon("/static/img/four.png");
				break;
			case 4:
				node.setIcon("/static/img/five.png");
				break;
			default:
				break;
			}
			treeList.add(node);
		}
	
		return treeList;
	}

	@Override
	public MatCategoryTree nodeAdd(String pId, String level, String name, String path) throws DoSthException {
		MatCategoryTree tree = this.matCategoryTreeRepository.getOne(pId);
		MatCategoryTree category = new MatCategoryTree(pId, name, level, tree.getEquType(), path);
		category = this.matCategoryTreeRepository.save(category);
		// 重新构建Path
		if (!category.getId().equals(category.getpId())) {
			path = (tree.getfId() == null ? "" : (tree.getfId() + ",")) + category.getId();
			category.setfId(path);
			String fName = (tree.getfName() == null ? "" : (tree.getfName() + ",")) + category.getName();
			category.setfName(fName);
			category = this.matCategoryTreeRepository.saveAndFlush(category);
		}
		return category;
	}

	@Override
	public void editName(String nodeId, String name) throws DoSthException {
		MatCategoryTree category = this.matCategoryTreeRepository.getOne(nodeId);
		category.setName(name);
		if (!category.getId().equals(category.getpId())) {
			MatCategoryTree p = this.matCategoryTreeRepository.getOne(category.getpId());
			String fName = (p.getfName() == null ? "" : (p.getfName() + ",")) + category.getName();
			category.setfName(fName);
		}
		this.matCategoryTreeRepository.saveAndFlush(category);
	}

	@Override
	public void nodeDel(String nodeId) throws DoSthException {
		MatCategoryTree categoryTree = this.matCategoryTreeRepository.getOne(nodeId);
		List<MatCategory> categoryList = this.matCategoryRepository.findByNode(nodeId);
		
		// 分支删除，则绑定在该分支上的物料信息也删除
		if(categoryList != null && categoryList.size() != 0) {
			this.matCategoryRepository.deleteInBatch(categoryList);
		}
		// 分支逻辑删除
		categoryTree.setStatus(UsingStatus.DISABLE);
		this.matCategoryTreeRepository.saveAndFlush(categoryTree);
	}
	
	@Override
	public void save(MatCategoryTree entity) throws DoSthException {
		this.matCategoryTreeRepository.save(entity);
	}

	@Override
	public MatCategoryTree update(MatCategoryTree entity) throws DoSthException {
		return this.matCategoryTreeRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(MatCategoryTree entity) throws DoSthException {
		this.matCategoryTreeRepository.delete(entity);
	}

	@Override
	public MatCategoryTree get(Serializable id) throws DoSthException {
		return this.matCategoryTreeRepository.getOne(id);
	}

	@Override
	public List<MatCategory> dataFilter(String accountId, String typeId) throws DoSthException {
		List<MatCategory> categoryList = new ArrayList<>();
		// 获取该节点信息
		List<MatCategoryTree> childrenTree = this.matCategoryTreeRepository.findChildrenNode(typeId, UsingStatus.ENABLE);
		if(childrenTree == null || childrenTree.size() == 0) {// 如果选中节点是终节点，则直接获取绑定物料
			List<MatCategory> equList = this.matCategoryRepository.findByNode(typeId);
			categoryList.addAll(equList);
		}
		return categoryList;
	}

	@Override
	public List<MatAssociationInfo> getDipCheck(String typeTreeId) throws DoSthException {
		List<MatAssociationInfo> list = new ArrayList<>();
		Criteria<MatEquInfo> c = new Criteria<>();
		c.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<MatEquInfo> equInfos = this.matEquInfoRepository.findAll(c);
		
		List<String> treeIds = this.matCategoryRepository.findMatInfoIds(typeTreeId);
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
		return list;
	}

	@Override
	public OpTip matCheck(String matIds) {
		String message = "";
		String matName = "";
		String categoryName = "";
		OpTip tip = new OpTip(200, "");
		String[] matIdArr = matIds.split(";");
		List<String> matIdList = Arrays.asList(matIdArr);
		List<MatCategory> matCategoryList = this.matCategoryRepository.findBymatIds(matIdList);
		if (CollectionUtils.isNotEmpty(matCategoryList)) {
			message = "物料已关联提示" + "<br/>";
			for (String matId : matIdList) {
				for (MatCategory matCategory : matCategoryList) {
					if (matId.equals(matCategory.getMatInfoId())) {
						matName = matCategory.getMatEquInfo().getMatEquName() + "：	";
						categoryName += matCategory.getCategoryTree().getName() + ",";
					}
				}
				message += matName + categoryName + "<br/>";
				matName = "";
				categoryName = "";
			}
			tip.setCode(204);
			tip.setMessage(message);
		}
		return tip;
	}

	@Override
	public void deleteBymatId(String matId) {
		this.matCategoryRepository.deleteBymatId(matId);
	}

	@Override
	public com.cnbaosi.dto.OpTip checkName(String categoryTreePId, String categoryTreeId, String newName) {
		com.cnbaosi.dto.OpTip tip = new com.cnbaosi.dto.OpTip();
		List<MatCategoryTree> treeList = this.matCategoryTreeRepository.findAll();
		treeList = treeList.stream().filter(tree -> UsingStatus.ENABLE.equals(tree.getStatus()) && categoryTreePId.equals(tree.getpId())
				&& !categoryTreeId.equals(tree.getId()) && newName.equals(tree.getName())).collect(Collectors.toList());
		if (treeList != null && treeList.size() > 0) {
			tip.setCode(201);
			tip.setMessage("同级已存在同名");
		}
		return tip;
	}
}