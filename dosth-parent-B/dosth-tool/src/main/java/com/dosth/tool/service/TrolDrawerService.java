package com.dosth.tool.service;

import com.dosth.util.OpTip;

/**
 * @description 可控抽屉柜接口
 * @author Zhidong.Guo
 *
 */
public interface TrolDrawerService {

	/**
	 * @description 编辑可控抽屉柜参数
	 * @param equSettingId 柜体Id
	 * @param cabinetType  柜体类型
	 * @param trolCom      可控柜串口
	 * @param trolBaud     可控柜波特率
	 * @param data         编辑参数
	 * @return
	 */
	public OpTip editTroSetup(String equSettingId, String cabinetType, String trolCom, String trolBaud, String data);
}