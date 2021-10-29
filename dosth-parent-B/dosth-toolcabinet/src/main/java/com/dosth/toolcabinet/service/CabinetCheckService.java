package com.dosth.toolcabinet.service;

import com.cnbaosi.exception.BscException;

/**
 * @description 柜子检测
 * @author guozhidong
 *
 */
public interface CabinetCheckService {
	
	/**
	 * @description 验证操作前置条件
	 * @param cabinetId 柜子Id
	 * @param params 验证状态 params[0] 是否验证串口;params[1] 是否验证撞机标识
	 * @throws BscException
	 */
	public void check(String cabinetId, Boolean... params) throws BscException;
	
	/**
	 * @description 检测连接
	 * @param cabinetId 柜子Id
	 * @throws BscException
	 */
	public void checkConnect(String cabinetId) throws BscException;
}