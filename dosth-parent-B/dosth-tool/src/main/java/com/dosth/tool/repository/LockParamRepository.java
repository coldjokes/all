package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.LockParam;

/**
 * 锁控板参数
 * 
 * @author Weifeng.Li
 *
 */
public interface LockParamRepository extends BaseRepository<LockParam, String> {
	
	/**
	 * @description 根据柜子Id获取栈号列表
	 * @param equSettingId 柜体Id
	 */
	@Query("select p.boardNo from LockParam p where p.equSettingId = :equSettingId")
	public List<Integer> getBoardNoListByEquSettingId(String equSettingId);

	/**
	 * @description 根据主柜Id获取附属锁控板柜体栈号集合
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	@Query("from LockParam p where p.equSetting.equSettingParentId = :mainCabinetId")
	public List<LockParam> getBoardNoListByMainCabinetId(String mainCabinetId);
	
	/**
	 * @description 根据柜体Id删除对应参数
	 * @param cabinetId
	 */
	@Modifying
	@Transactional
	@Query("delete from LockParam l where l.equSettingId = :cabinetId")
	public void deleteLockParamsByCabinetId(String cabinetId);
}