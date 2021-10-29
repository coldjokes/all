package com.dosth.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.Menu;
import com.dosth.common.db.repository.BaseRepository;

public interface MenuRepository extends BaseRepository<Menu, String> {

	/**
	 * 根据code获取菜单列表
	 * 
	 * @param code
	 * @return
	 */
	@Query("select m from Menu m where m.code = ?1")
	public List<Menu> findMenuListByCode(String code);

	/**
	 * 创建菜单树
	 * 
	 * @return
	 */
	@Query("from Menu m where m.pcode is null or m.pcode = m.code or m.code = ''")
	public List<Menu> createMenuTree();
}