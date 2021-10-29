package com.cnbaosi.modbus.monitor;

import com.cnbaosi.modbus.dto.MonitorData;

/**
 * @description modbus监控
 * @author guozhidong
 *
 */
public interface ModbusMonitor {
	/**
	 * @description 接收信息监控数据
	 */
	public void receiveMonitorData(MonitorData data);
}
