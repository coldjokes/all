package com.dosth.tool.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.EquSetting;

/**
 * @description 柜体管理
 * @author guozhidong
 *
 */
public interface EquSettingRepository extends BaseRepository<EquSetting, String> {
	/**
	 * @description 根据柜子Id获取柜体与副柜信息列表
	 * @param cabinetId 柜体Id
	 * @return
	 */
	@Query("from EquSetting e where e.id = :cabinetId or e.equSettingParentId = :cabinetId or (e.cabinetType = 'VIRTUAL_WAREHOUSE' and e.equSettingParentId is null)")
	public List<EquSetting> getEquSettingList(String cabinetId);

	/**
	 * @description 根据设备Id获取柜体设置列表
	 * @param equInfoId 设备Id
	 * @return
	 */
	@Query("from EquSetting e where e.id = :cabinetId")
	public List<EquSetting> getEquSettingListByEquInfoId(String cabinetId);

	/**
	 * @description 根据序列号查询刀具柜信息
	 * @param serialNo
	 * @return
	 */
	@Query("from EquSetting e where e.serialNo = :serialNo")
	public EquSetting getEquSettingBySerialNo(String serialNo);

	/**
	 * @description 根据主柜Id获取柜体树形集合
	 * @param cabinetId 主柜Id
	 * @return
	 */
	@Query("from EquSetting e where e.id = :cabinetId or e.equSettingParentId = :cabinetId")
	public List<EquSetting> getCabinetTreeList(String cabinetId);

	/**
	 * @description 获取主柜列表
	 */
	@Query("from EquSetting e where e.equSettingParentId is null or e.equSettingParentId is ''")
	public List<EquSetting> getMainCabinetList();

	/**
	 * @description 根据仓库别名获取柜体列表
	 * @param wareHouseAlias 仓库别名
	 * @return
	 */
	@Query("from EquSetting e where e.wareHouseAlias = :wareHouseAlias")
	public List<EquSetting> getCabinetListByWareHouseAlias(String wareHouseAlias);

	/**
	 * @description 修改柜子管理员为admin
	 */
	@Modifying
	@Transactional
	@Query("update from EquSetting set accountId = '1'")
	public void deleteByUser();

	/**
	 * @description 根据主柜Id获取附属柜体列表
	 * @param equSettingParentId 主柜Id
	 * @return
	 */
	@Query("from EquSetting e where e.equSettingParentId = :equSettingParentId")
	public List<EquSetting> getEquSettingChildList(String equSettingParentId);
}