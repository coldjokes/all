package com.cnbaosi.common;

import com.cnbaosi.modbus.ModbusServerConfig;
import com.cnbaosi.modbus.ModbusUtil;
import com.cnbaosi.modbus.enums.AddrType;

import net.wimpi.modbus.ModbusException;

/**
 * modbus测试类
 * @author guozhidong
 *
 */
public class ModbusTest {
	public static void main(String[] args) {
		ModbusUtil.getInstance(new ModbusServerConfig("192.168.6.67", 502));
		try {
			System.err.println(ModbusUtil.readModbusValue(1, AddrType.DoorSignal));
		} catch (ModbusException e) {
			e.printStackTrace();
		}
	}
}