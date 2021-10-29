package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 物料分类
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatCategory extends BasePojo {

	@Column(name = "MAT_INFO_ID", columnDefinition = "varchar(36) COMMENT '物料ID'")
	private String matInfoId;
	@ManyToOne
	@JoinColumn(name = "MAT_INFO_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "物料名称", isForeign = true)
	private MatEquInfo matEquInfo;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Column(name = "CATEGORY_TREE_ID", columnDefinition = "varchar(36) COMMENT '物料分类树ID'")
	private String categoryTreeId;
	@ManyToOne
	@JoinColumn(name = "CATEGORY_TREE_ID", insertable = false, updatable = false)
	@PageTableTitle(value = "关联类别", isForeign = true, isVisible = false)
	private MatCategoryTree categoryTree;

	public String getMatInfoId() {
		return matInfoId;
	}

	public void setMatInfoId(String matInfoId) {
		this.matInfoId = matInfoId;
	}

	public MatEquInfo getMatEquInfo() {
		return matEquInfo;
	}

	public void setMatEquInfo(MatEquInfo matEquInfo) {
		this.matEquInfo = matEquInfo;
	}

	public UsingStatus getStatus() {
		return status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public String getCategoryTreeId() {
		return categoryTreeId;
	}

	public void setCategoryTreeId(String categoryTreeId) {
		this.categoryTreeId = categoryTreeId;
	}

	public MatCategoryTree getCategoryTree() {
		return categoryTree;
	}

	public void setCategoryTree(MatCategoryTree categoryTree) {
		this.categoryTree = categoryTree;
	}

}