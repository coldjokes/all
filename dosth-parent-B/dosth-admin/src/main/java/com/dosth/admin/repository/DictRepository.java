package com.dosth.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.admin.entity.Dict;
import com.dosth.common.db.repository.BaseRepository;

public interface DictRepository extends BaseRepository<Dict, String> {

	/**
	 * 根据名称获取字典列表
	 * 
	 * @param name
	 * @return
	 */
	@Query("select d from Dict d where d.name = ?1")
	public List<Dict> findDictListByName(String name);

	/**
	 * 根据Id获取子级字典
	 * 
	 * @param id
	 * @return
	 */
	@Query("select d from Dict d where d.pId = ?1")
	public List<Dict> findListByPid(String id);
}
