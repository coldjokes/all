package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.CabinetPlcSetting;

/**
 * @Description 刀具柜plc参数设置Service
 * @Author guozhidong
 */
public interface CabinetPlcSettingService extends BaseService<CabinetPlcSetting> {
	/**
	 * @description 根据刀具柜Id获取Plc参数设置信息列表
	 * @param cabinetId 刀具柜Id
	 * @return
	 */
	public List<CabinetPlcSetting> getCabinetPlcSettingListByCabinetId(String cabinetId);
}