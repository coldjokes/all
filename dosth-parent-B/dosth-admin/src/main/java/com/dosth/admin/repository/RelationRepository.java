package com.dosth.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.Menu;
import com.dosth.admin.entity.Relation;
import com.dosth.common.db.repository.BaseRepository;

public interface RelationRepository extends BaseRepository<Relation, String> {

	/**
	 * 根据角色Id获取菜单列表
	 * 
	 * @param roleId
	 *            角色Id
	 * @return
	 */
	@Query("select r.menu from Relation r where r.roleId = ?1")
	public List<Menu> getMenuListByRoleId(String roleId);

	/**
	 * 根据角色Id获取菜单Id集合
	 * 
	 * @param roleId
	 *            角色Id
	 * @return
	 */
	@Query("select r.menuId from Relation r where r.roleId = ?1")
	public List<String> getMenuIdListByRoleId(String roleId);

	/**
	 * 根据角色Id获取角色菜单关系数据
	 * 
	 * @param roleId
	 *            角色Id
	 * @return
	 */
	@Query("from Relation r where r.roleId = ?1")
	public List<Relation> getListByRoleId(String roleId);
}