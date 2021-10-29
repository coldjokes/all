package com.dosth.tool.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import com.dosth.common.db.entity.BasePojo;
import com.dosth.toolcabinet.enums.EnumBorrowType;

/**
 * @description 部门关联权限
 * @author guozhidong
 *
 */
@Entity
@SuppressWarnings("serial")
public class DeptBorrowPopedom extends BasePojo {

	@Column(columnDefinition = "varchar(36) COMMENT '部门ID'")
	private String deptId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '物料借出权限'")
	private EnumBorrowType borrowPopedom; // 物料借出权限

	@Lob
	@Column(columnDefinition = "longtext COMMENT '权限'")
	private String popedoms; // 权限

	public DeptBorrowPopedom() {
	}

	public DeptBorrowPopedom(String deptId, EnumBorrowType borrowPopedom, String popedoms) {
		this.deptId = deptId;
		this.borrowPopedom = borrowPopedom;
		this.popedoms = popedoms;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public EnumBorrowType getBorrowPopedom() {
		return this.borrowPopedom;
	}

	public void setBorrowPopedom(EnumBorrowType borrowPopedom) {
		this.borrowPopedom = borrowPopedom;
	}

	public String getPopedoms() {
		return this.popedoms;
	}

	public void setPopedoms(String popedoms) {
		this.popedoms = popedoms;
	}
}