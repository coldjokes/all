package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.ManufacturerCustom;

/**
 * 
 * @description 供货商客服持久化层
 * @author guozhidong
 *
 */
public interface ManufacturerCustomRepository extends BaseRepository<ManufacturerCustom, String> {
	
	/**
	 * @description 根据供应商Id获取客服列表
	 * @param manufacturerId 供应商Id
	 * @param status 启用状态
	 * @return
	 */
	@Query("from ManufacturerCustom m where m.manufacturerId = :manufacturerId and m.status = :status")
	public List<ManufacturerCustom> getCustomListByManufacturerId(String manufacturerId, UsingStatus status);

	/**
	 * @description 根据供应商匹配联系人名称
	 * @param manufacturerId 供应商Id
	 * @param contactName 联系人名称
	 * @return
	 */
	@Query("from ManufacturerCustom m where m.status = 'ENABLE' and m.manufacturerId = :manufacturerId and m.contactName =:contactName")
	public List<ManufacturerCustom> getCustomListByCustomName(String manufacturerId, String contactName);

	/**
	 * @description 根据物料清除供货商客服
	 */
	@Transactional
	@Modifying
	@Query("delete from ManufacturerCustom m where m.manufacturerId <> '001'")
	public void deleteByMat();
}