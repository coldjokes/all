package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.MatEquInfo;

public interface MatEquInfoRepository extends BaseRepository<MatEquInfo, String> {

	/**
	 * 根据编号查询物料信息
	 * 
	 * @param barCode
	 * @return
	 */
	@Query("from MatEquInfo m where m.barCode = :barCode")
	public List<MatEquInfo> selectByBarCode(String barCode);

	/**
	 * @description 获取物料剩余数量
	 * @param cabinetId 当前柜子Id
	 * @param matId     物料Id
	 * @return
	 */
	@Query("from MatEquInfo m where m.id = :matId")
	public List<MatEquInfo> findByMatId(String matId);
}