package com.dosth.toolcabinet.service;

import java.util.Map;

import com.cnbaosi.exception.BscException;
import com.cnbaosi.workspace.spring.Door;
import com.dosth.toolcabinet.enums.PlcOpType;

/**
 * @description 门Service
 * @author guozhidong
 *
 */
public interface DoorService {
	/**
	 * @description 检测门状态,门开启状态,延迟2s关门操作
	 */
	public void checkDoorStatus();
	
	/**
	 * @description 门操作
	 * @param cabinetId 柜体Id
	 * @param type        料斗动作
	 * @throws BscException
	 */
	public void op(String cabinetId, PlcOpType type) throws BscException;
	
	/**
	 * @description 批量门操作
	 * @param opFlag  开关门操作标识 true 开门 false 关门
	 * @throws BscException
	 */
	public void batchDoorOp(Boolean opFlag) throws BscException;
	
	/**
	 * @description 根据柜子Id获取门列表
	 * @param cabinetId 柜子Id
	 * @return
	 */
	public Map<Byte, Door> getDoorMap(String cabinetId);
}