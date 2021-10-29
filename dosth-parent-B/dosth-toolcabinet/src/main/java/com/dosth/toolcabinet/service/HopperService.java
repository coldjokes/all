package com.dosth.toolcabinet.service;

import com.cnbaosi.exception.BscException;
import com.dosth.toolcabinet.enums.PlcOpType;

/**
 * @description 料斗Service
 * @author guozhidong
 *
 */
public interface HopperService {
	/**
	 * @description 料斗操作
	 * @param cabinetId 柜体Id
	 * @param type        料斗动作
	 * @throws BscException
	 */
	public void op(String cabinetId, PlcOpType type) throws BscException;
}