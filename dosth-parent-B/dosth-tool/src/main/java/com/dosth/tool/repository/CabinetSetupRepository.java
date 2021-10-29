package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.CabinetSetup;

/**
 * 刀具柜配置
 * 
 * @author WeiFeng.Li
 *
 */
public interface CabinetSetupRepository extends BaseRepository<CabinetSetup, String> {

	/**
	 * 根据序列号获取配置信息
	 * 
	 * @param serialNo 序列号
	 * @return
	 */
	@Query("from CabinetSetup setup where setup.equSettingId in (select ee.id from EquSetting ee where ee.equSettingParentId = (select s.id from EquSetting s where s.serialNo =:serialNo)) or setup.equSettingId in (select ss.id from EquSetting ss where ss.serialNo =:serialNo)")
	List<CabinetSetup> getCabinetSetupBySerialNo(String serialNo);

	/**
	 * @description 根据柜子Id和键值获取设置值
	 * @param cabinetId 柜体Id
	 * @param key 键值
	 * @return
	 */
	@Query("select s.setupValue from CabinetSetup s where s.equSettingId = :cabinetId and s.setupKey = :key")
	public List<String> getValueByCabinetIdAndKey(String cabinetId, String key);

	/**
	 * @description 根据主柜Id获取附属柜子参数列表
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	@Query("from CabinetSetup s where s.equSetting.equSettingParentId = :mainCabinetId")
	public List<CabinetSetup> getCabinetSetupListByMainCabinetId(String mainCabinetId);

	/**
	 * @description 根据柜体Id删除柜体设置
	 * @param equSettingId 柜体Id
	 */
	@Modifying
	@Transactional
	@Query("delete from CabinetSetup c where c.equSettingId = :equSettingId")
	public void deleteCabinetSetupsByCabinetId(String equSettingId);
}