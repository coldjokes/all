package com.dosth.admin.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.dosth.common.annotion.PageTableTitle;
import com.dosth.common.db.entity.BasePojo;

/**
 * @description 角色表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class Roles extends BasePojo {
	
	@PageTableTitle(value = "排序号")
	@Column(columnDefinition = "int(11) COMMENT '序号'")
	private Integer num; 
	
	@Column(name = "pId", columnDefinition = "varchar(36) COMMENT '父角色ID'")
	@PageTableTitle(value = "父角色")
	private String pId;
	@ManyToOne
	@JoinColumn(name = "pId", insertable = false, updatable = false)
	private Roles parentRole;
	
	@OneToMany(mappedBy = "parentRole")
	private List<Roles> roleList;
	
	@Transient
	private String pRoleName;

	@Column(columnDefinition = "varchar(50) COMMENT '角色名称'")
	@PageTableTitle(value = "角色名称")
	private String name;

	@Column(columnDefinition = "varchar(36) COMMENT '部门ID'")
	private String deptId;
	@ManyToOne
	@JoinColumn(name = "deptId", insertable = false, updatable = false)
	@PageTableTitle(value = "所属部门", isForeign = true)
	private Dept dept;

	@Column(columnDefinition = "varchar(50) COMMENT '提示'")
	@PageTableTitle(value = "别名")
	private String tips;
	
	@Column(columnDefinition = "bigint(20) COMMENT '保留字段'")
	private Long version;
	

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getpId() {
		return this.pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public Roles getParentRole() {
		return this.parentRole;
	}

	public void setParentRole(Roles parentRole) {
		this.parentRole = parentRole;
	}

	public List<Roles> getRoleList() {
		if (this.roleList == null) {
			this.roleList = new ArrayList<>();
		}
		return this.roleList;
	}

	public void setRoleList(List<Roles> roleList) {
		this.roleList = roleList;
	}

	public String getpRoleName() {
		if (this.pRoleName == null || "".equals(this.pRoleName)) {
			if (this.parentRole != null) {
				this.pRoleName = this.parentRole.getName();
			}
		}
		return this.pRoleName;
	}

	public void setpRoleName(String pRoleName) {
		this.pRoleName = pRoleName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Dept getDept() {
		return this.dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public String getTips() {
		return this.tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "Role{" + "id=" + id + ", num=" + num + ", pId=" + pId + ", name=" + name + ", deptId=" + deptId
				+ ", tips=" + tips + ", version=" + version + "}";
	}
}