package com.dosth.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * 部门表
 * 
 * @author liweifeng
 *
 */
@SuppressWarnings("serial")
@Entity
public class Dept extends BasePojo {

	@Column(columnDefinition = "varchar(50) COMMENT '部门名称'")
	@PageTableTitle(value = "部门名称")
	private String deptName;

	@Column(columnDefinition = "varchar(36) COMMENT '部门编号'")
	@PageTableTitle(value = "部门编号")
	private String deptNo;

	@Column(name = "p_id", columnDefinition = "varchar(36) COMMENT '上级部门ID'")
	@PageTableTitle(value = "上级部门ID", isVisible = false)
	private String pId;
//	@OneToMany
//	@JoinColumn(name = "p_id", referencedColumnName = "id", insertable = false, updatable = false)
//	private List<Dept> deptList;

	@Column(columnDefinition = "varchar(255) COMMENT '上级部门ids'")
	@PageTableTitle(value = "上级部门ids", isVisible = false)
	private String fullIds;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(10) COMMENT '部门状态：1.启用 ，2.冻结， 3.删除'")
	@PageTableTitle(value = "部门状态", isEnum = true, isVisible = false)
	private ManagerStatus status = ManagerStatus.OK;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

//	public List<Dept> getDeptList() {
//		return deptList;
//	}
//
//	public void setDeptList(List<Dept> deptList) {
//		this.deptList = deptList;
//	}

	public String getFullIds() {
		return fullIds;
	}

	public void setFullIds(String fullIds) {
		this.fullIds = fullIds;
	}

	public ManagerStatus getStatus() {
		return status;
	}

	public void setStatus(ManagerStatus status) {
		this.status = status;
	}

}