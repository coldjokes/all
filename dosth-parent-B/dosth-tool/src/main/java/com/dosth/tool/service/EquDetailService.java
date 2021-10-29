package com.dosth.tool.service;

import java.util.List;

import com.dosth.common.servcie.BaseService;
import com.dosth.constant.EnumDoor;
import com.dosth.tool.entity.EquDetail;

/**
 * @description 设备详情Service
 * 
 * @author guozhidong
 *
 */
public interface EquDetailService extends BaseService<EquDetail> {
	/**
	 * @description 新增
	 * @return
	 */
	public EquDetail add(EquDetail equDetail);

	/**
	 * @description 根据柜子Id获取设备详情列表
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public List<EquDetail> getEquDetailListBySettingId(String cabinetId);

	/**
	 * @description APP获取左右门
	 * @param ip
	 * @return
	 */
	public EnumDoor getDoor(String ip);
}