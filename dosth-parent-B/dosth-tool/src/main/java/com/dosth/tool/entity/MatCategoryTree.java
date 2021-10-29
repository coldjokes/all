package com.dosth.tool.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.entity.BasePojo;
import com.dosth.toolcabinet.enums.EnumBorrowType;

/**
 * @description 物料分类
 * @author chenlei
 *
 */
@Entity
@SuppressWarnings("serial")
public class MatCategoryTree extends BasePojo {

	@PageTableTitle(value = "父节点ID")
	@Column(columnDefinition = "varchar(255) COMMENT '父节点ID'")
	private String pId;

	@PageTableTitle(value = "节点名")
	@Column(columnDefinition = "varchar(255) COMMENT '节点名'")
	private String name;

	@PageTableTitle(value = "层级")
	@Column(columnDefinition = "varchar(255) COMMENT '层级'")
	private String level;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '状态'")
	@PageTableTitle(value = "状态", isEnum = true)
	private UsingStatus status;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '物料类型'")
	private EnumBorrowType equType;

	@Column(columnDefinition = "varchar(255) COMMENT '节点路径'")
	private String fId;

	@Column(columnDefinition = "varchar(100) COMMENT '父节点名'")
	private String fName;

	@PageTableTitle(value = "创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(columnDefinition = "datetime COMMENT '创建时间'")
	private Date CreateDate;

	public MatCategoryTree() {
		setCreateDate(new Date());
	}

	public MatCategoryTree(String pId, String name, String level, EnumBorrowType equType, String fId) {
		setCreateDate(new Date());
		setStatus(UsingStatus.ENABLE);
		this.pId = pId;
		this.name = name;
		this.level = level;
		this.equType = equType;
		this.fId = fId;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public EnumBorrowType getEquType() {
		return equType;
	}

	public void setEquType(EnumBorrowType equType) {
		this.equType = equType;
	}

	public UsingStatus getStatus() {
		if (this.status == null) {
			this.status = UsingStatus.ENABLE;
		}
		return this.status;
	}

	public void setStatus(UsingStatus status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}

	public String getfId() {
		return this.fId;
	}

	public void setfId(String fId) {
		this.fId = fId;
	}

	public String getfName() {
		return this.fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}
}