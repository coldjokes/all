package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatReturnDetail;

/**
 * 
 * @description 物料归还详情持久化层
 * @author chenlei
 *
 */
public interface MatReturnDetailRepository extends BaseRepository<MatReturnDetail, String> {
	
	/**
	 * 根据归还ID删除数据
	 * 
	 * @param code
	 */
	@Modifying
	@Transactional
	@Query("delete from MatReturnDetail y where y.matReturnBackId = :code")
	public void delByReturnBackId(String code);
	
	/**
	 * 根据类型ID获取归还ID集合
	 * 
	 * @param code
	 */
	@Query("from MatReturnDetail y where y.restitutionTypeId = :code")
	public List<MatReturnDetail> getReturnIds(String code);
}