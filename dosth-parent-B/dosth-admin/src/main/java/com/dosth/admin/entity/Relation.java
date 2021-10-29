package com.dosth.admin.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.dosth.common.db.entity.BasePojo;

/**
 * 角色和菜单关联表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class Relation extends BasePojo {

	public Relation() {
	}

	public Relation(String roleId, String menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
	}

	@Column(name = "menuId", columnDefinition = "varchar(36) COMMENT '菜单ID'")
	private String menuId;
	@ManyToOne
	@JoinColumn(name = "menuId", referencedColumnName = "id", insertable = false, updatable = false)
	private Menu menu;
	
	@Column(name = "roleId", columnDefinition = "varchar(36) COMMENT '角色ID'")
	private String roleId;
	@ManyToOne
	@JoinColumn(name = "roleId", referencedColumnName = "id", insertable = false, updatable = false)
	private Roles role;

	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Roles getRole() {
		return this.role;
	}

	public void setRole(Roles role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Relation{" + "id=" + id + ", menuId=" + menuId + ", roleId=" + roleId + "}";
	}
}