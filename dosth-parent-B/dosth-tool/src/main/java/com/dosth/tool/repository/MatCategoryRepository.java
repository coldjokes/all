package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatCategory;

public interface MatCategoryRepository extends BaseRepository<MatCategory, String> {

	/**
	 * @description 获取绑定信息
	 * @return
	 */
	@Query("from MatCategory m where m.categoryTreeId = :id")
	public List<MatCategory> findByNode(String id);

	/**
	 * @description 获取绑定物料信息
	 * @return
	 */
	@Query("select m.matInfoId from MatCategory m where m.categoryTreeId = :id")
	public List<String> findMatInfoIds(String id);

	/**
	 * @description 根据treeId获取绑定物料信息
	 * @return
	 */
	@Query("select m.matInfoId from MatCategory m where m.categoryTreeId in( :id)")
	public List<String> getMatIds(List<String> id);

	/**
	 * @description 获取所有绑定物料信息
	 * @return
	 */
	@Query("from MatCategory m")
	public List<MatCategory> findAllNodes();

	/**
	 * @description 获取所有绑定物料信息
	 * @return
	 */
	@Query("from MatCategory m where m.matInfoId = :id")
	public List<MatCategory> findByMatId(String id);

	/**
	 * 根据物料ID查询物料关联信息
	 * 
	 * @param id 物料ID
	 * @return
	 */
	@Query("from MatCategory m where m.matInfoId in( :id)")
	public List<MatCategory> findBymatIds(List<String> id);

	/**
	 * 根据物料ID删除物料关联信息
	 * 
	 * @param id 物料ID
	 * @return
	 */
	@Modifying
	@Query("delete MatCategory m where m.matInfoId = :id")
	public void deleteBymatId(String id);
}