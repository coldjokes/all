package com.dosth.toolcabinet.service;

import com.cnbaosi.exception.BscException;
import com.dosth.toolcabinet.enums.PlcOpType;

/**
 * @description 回收口操作接口
 * @author guozhidong
 *
 */
public interface ReturnBackDoorService {
	/**
	 * @description 回收口操作
	 * @param cabinetId 柜体Id
	 * @param type        回收口动作
	 * @throws BscException
	 */
	public void op(String cabinetId, PlcOpType type) throws BscException;
}