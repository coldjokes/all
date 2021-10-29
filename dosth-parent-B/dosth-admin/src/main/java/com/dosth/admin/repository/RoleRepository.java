package com.dosth.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.Roles;
import com.dosth.common.db.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<Roles, String> {

	/**
	 * 获取角色列表
	 */
	@Query("from Roles r where r.pId = r.id or r.pId is null")
	public List<Roles> getRoleList();
}