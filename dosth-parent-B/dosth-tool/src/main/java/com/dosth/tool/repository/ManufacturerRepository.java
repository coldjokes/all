package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.Manufacturer;
/**
 * 
 * @description 供货商持久化层
 * @author guozhidong
 *
 */
public interface ManufacturerRepository extends BaseRepository<Manufacturer, String> {

	/**
	 * 根据名称查询供应商信息
	 * @param manufacturerName
	 * @return
	 */
	@Query("from Manufacturer m where m.manufacturerName = :manufacturerName and m.status = 'ENABLE'")
	public Manufacturer findByName(String manufacturerName);
	
	/**
	 * @description 根据名称查询供应商信息
	 * @param manufacturerName
	 * @return
	 */
	@Query("from Manufacturer m where m.manufacturerName = :manufacturerName and m.status = 'ENABLE'")
	public List<Manufacturer> getManufacturerByName(String manufacturerName);	
	
	/**
	 * @description 根据编号查询供应商信息
	 * @param manufacturerNo
	 * @return
	 */
	@Query("from Manufacturer m where m.manufacturerNo = :manufacturerNo and m.status = 'ENABLE'")
	public List<Manufacturer> getManufacturerByNo(String manufacturerNo);

	/**
	 * @description 根据物料清除供应商(保留出厂设置)
	 */
	@Transactional
	@Modifying
	@Query("delete from Manufacturer m where m.id <> '001'")
	public void deleteByMat();
}