package com.dosth.tool.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import com.dosth.common.db.repository.BaseRepository;
import com.dosth.tool.entity.CabinetPlcSetting;

/**
 * @description 刀具柜plc参数设置
 * @Author guozhidong
 */
public interface CabinetPlcSettingRepository extends BaseRepository<CabinetPlcSetting, String> {

	/**
	 * @description 根据柜子Id和plc设置Id获取设置详情
	 * @param cabinetId 柜子Id
	 * @param settingId plc设置Id
	 * @return
	 */
	@Query("from CabinetPlcSetting s where s.cabinetId = :cabinetId and s.plcSettingId = :settingId")
	public List<CabinetPlcSetting> getCabinetPlcSettingListByCabinetId(@RequestParam("cabinetId") String cabinetId,
			@RequestParam("settingId") String settingId);
}