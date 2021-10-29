package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.toolcabinet.enums.EnumBorrowType;

public interface MatCategoryTreeRepository extends BaseRepository<MatCategoryTree, String> {

	/**
	 * @description 获取树信息
	 * @param status
	 * @return
	 */
	@Query("from MatCategoryTree m where m.status = :status")
	public List<MatCategoryTree> findAll(UsingStatus status);
	
	/**
	 * @description 获取分类信息
	 * @param equType
	 * @param status
	 * @return
	 */
	@Query("from MatCategoryTree m where m.equType = :equType and m.status = :status")
	public List<MatCategoryTree> findByType(EnumBorrowType equType, UsingStatus status);
	
	/**
	 * @description 根据类型名获取类型分类
	 * @param receiveInfo
	 * @param status
	 * @return
	 */
	@Query("from MatCategoryTree m where m.name = :receiveName and m.status = :status")
	public List<MatCategoryTree> findByName(String receiveName, UsingStatus status);

	
	/**
	 * @description 获取节点信息
	 * @param equType
	 * @param status
	 * @return
	 */
	@Query("from MatCategoryTree m where m.id = :nodeId and m.status = :status")
	public MatCategoryTree findByNode(String nodeId, UsingStatus status);
	
	
	/**
	 * @description 获取子节点信息
	 * @param equType
	 * @param status
	 * @return
	 */
	@Query("from MatCategoryTree m where m.pId = :nodeId and m.id <> m.pId and m.status = :status")
	public List<MatCategoryTree> findChildrenNode(String nodeId, UsingStatus status);
	
    /**
     * @description 获取路径包含该节点的子节点
     * @param pathId
     * @param status
     * @return
     */
    @Query("from MatCategoryTree m where m.fId like CONCAT('%', :pathId, '%') and m.status = :status")
    public List<MatCategoryTree> findPathNode(String pathId, UsingStatus status);

    /**
     * @description 初始化领取类型
     */
    @Modifying
    @Transactional
    @Query("delete from MatCategoryTree where id not in ('1', '2', '3', '4', '5')")
	public void resetInit();
}