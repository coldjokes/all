package com.dosth.tool.service;

import java.util.List;
import java.util.Map;

import com.dosth.common.servcie.BaseService;
import com.dosth.tool.entity.CabinetSetup;

/**
 * 刀具柜配置
 * 
 * @author WeiFeng.Li
 *
 */
public interface CabinetSetupService extends BaseService<CabinetSetup> {

	/**
	 * 查询
	 * 
	 * @param criteria
	 * @return
	 */
	public Map<String, CabinetSetup> getCabinetSetupByEquSettingId(String equSettingId);

	/**
	 * 根据序列号获取配置信息
	 * 
	 * @param serialNo 序列号
	 * @return
	 */
	public List<CabinetSetup> getCabinetSetupBySerialNo(String serialNo);

	/**
	 * @description 根据柜子Id和键值获取设置值
	 * @param cabinetId 柜体Id
	 * @param key 键值
	 * @return
	 */
	public String getValueByCabinetIdAndKey(String cabinetId, String key);

	/**
	 * @description 根据主柜Id获取附属储物柜参数列表（PLC类型柜子添加储物柜专用）
	 * @param mainCabinetId 主柜Id
	 * @return
	 */
	public List<CabinetSetup> getStoreCabinetSetupByEquSettingId(String mainCabinetId);
	
	/**
	 * @description 根据id查询暂存柜信息
	 * @param id 主柜Id
	 * @return
	 */
	public CabinetSetup  getShareSwitch(String id);
	
	
}