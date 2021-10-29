package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.common.state.DataSyncType;
import com.dosth.tool.entity.DataSyncState;

/**
 * 数据同步配置
 * 
 * @author chenlei
 *
 */
public interface DataSyncStateRepository extends BaseRepository<DataSyncState, String> {
	
	/**
	 * @description 根据别名获取刀具柜同步信息
	 * @param cabinetName 刀具柜别名
	 * @return
	 */
	@Query("from DataSyncState d where d.equSetting.wareHouseAlias = :cabinetName and d.dataSyncType =:type")
	public List<DataSyncState> getSyncInfo(String cabinetName, DataSyncType type);
	
}