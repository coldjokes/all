package com.dosth.comm.plc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.constant.EnumDoor;
import com.dosth.toolcabinet.enums.PlcOpType;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;

public class PLCUtil {

	private static final Logger logger = LoggerFactory.getLogger(PLCUtil.class);

	public static String HOST = "192.168.1.101";
	public static int PORT = 502;
	public static int SLAVE_ID = 1;
	public static int TIME_OUT = 120;
	public static final int OPEN_DOOR_ACTION = 1;
	public static final int CLOSE_DOOR_ACTION = 2;
	public static final int OPEN_DOOR2_ACTION = 3;
	public static final int CLOSE_DOOR2_ACTION = 4;

	public static final int OFFSET = 1; // PLC and modbus address offset

	public static final int F1_ADDRESS = 6097 - OFFSET;
	public static final int F2_ADDRESS = 6099 - OFFSET;
	public static final int F3_ADDRESS = 6101 - OFFSET;
	public static final int F4_ADDRESS = 6103 - OFFSET;
	public static final int F5_ADDRESS = 6105 - OFFSET;
	public static final int F6_ADDRESS = 6107 - OFFSET;
	public static final int F7_ADDRESS = 6109 - OFFSET;
	public static final int F8_ADDRESS = 6111 - OFFSET;
	public static final int F9_ADDRESS = 6113 - OFFSET;
	public static final int F10_ADDRESS = 6115 - OFFSET;

	public static final int[] F_ADDRESS_ARRAY = { 0, F1_ADDRESS, F2_ADDRESS, F3_ADDRESS, F4_ADDRESS, F5_ADDRESS,
			F6_ADDRESS, F7_ADDRESS, F8_ADDRESS, F9_ADDRESS, F10_ADDRESS };

	public static final int ORIGIN_POSI_ADDRESS = 6117 - OFFSET;
	public static final int SPEED_ADDRESS = 6119 - OFFSET;
	public static final int CURRENT_POSI_ADDRESS = 6121 - OFFSET;

	public static final int IP_SEGMENT1_ADDRESS = 6125 - OFFSET;
	public static final int IP_SEGMENT2_ADDRESS = 6126 - OFFSET;
	public static final int IP_SEGMENT3_ADDRESS = 6127 - OFFSET;
	public static final int IP_SEGMENT4_ADDRESS = 6128 - OFFSET;

	public static final int TARGET_POSI_ADDRESS = 4187 - OFFSET;
	public static final int SCAN_RES_ADDRESS = 4188 - OFFSET;
	public static final int TEMPORARY_INDICATOR_ADDRESS = 4189 - OFFSET;

	public static final int ON_UP_COIL = 2349 - OFFSET;
	public static final int ON_DOWN_COIL = 2350 - OFFSET;

	/**
	 * 左侧取料口开门
	 */
	public static final int ON_OPEN_DOOR_COIL = 2351 - OFFSET;
	/**
	 * 左侧取料口关门
	 */
	public static final int ON_CLOSE_DOOR_COIL = 2352 - OFFSET;
	/**
	 * 右侧取料口开门
	 */
	public static final int ON_OPEN2_DOOR_COIL = 2371 - OFFSET;
	/**
	 * 右侧取料口关门
	 */
	public static final int ON_CLOSE2_DOOR_COIL = 2372 - OFFSET;

	public static final int ON_RESET_LIFT_COIL = 2353 - OFFSET;
	public static final int ON_ARRIVE_COIL = 2354 - OFFSET;
	/**
	 * 左侧取料口门状态标识 1 为已开门;0 为已关门
	 */
	public static final int ON_CLOSE_AND_CHECK_COIL = 2355 - OFFSET;
	/**
	 * 右侧取料口门状态标识 1 为已开门;0 为已关门
	 */
	public static final int ON_CLOSE_AND_CHECK2_COIL = 2373 - OFFSET;
	public static final int ON_FAULT_COIL = 2356 - OFFSET;

	public static final int MAX_LOCATION_WARNING_COIL = 2357 - OFFSET;
	public static final int MIN_LOCATION_WARNING_COIL = 2358 - OFFSET;
	public static final int MOTOR_DRIVER_WARNING_COIL = 2359 - OFFSET;
	public static final int ON_OPEN_DOOR_FAIL_COIL = 2360 - OFFSET;
	public static final int ON_CLOSE_DOOR_FAIL_COIL = 2361 - OFFSET;
	public static final int ON_OPEN_RETRI_DOOR_FAIL_COIL = 2362 - OFFSET;
	public static final int ON_SCAN_COIL = 2363 - OFFSET;
	public static final int MODIFY_IP_COIL = 2379 - OFFSET;

	public static TCPMasterConnection connection;
	private static ModbusTCPTransaction transaction;

	/**
	 * @description 读取Modbus
	 * @param address Modbus线地址
	 * @return 寄存器中的值
	 */
	public static int readAddress(String host, int address) throws Exception {
		TCPMasterConnection connection = null;
		InetAddress addr = InetAddress.getByName(host);
		connection = new TCPMasterConnection(addr);
		connection.setPort(502);
		connection.connect();
		ModbusRequest request;
		ModbusTCPTransaction trans;

		request = new ReadMultipleRegistersRequest(address, 1);
		trans = new ModbusTCPTransaction(connection);
		request.setUnitID(1);
		trans.setRequest(request);
		trans.execute();
		return (((ReadMultipleRegistersResponse) trans.getResponse()).getRegisterValue(0));
	}

	/**
	 * @description 读从Modbus寄存器中的值
	 * @param address Modbus地址
	 * @return 寄存器中的值
	 */
	public static int readModbusValue(int address) throws Exception {
		int result = -1;
		try {
			ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(address, 2);
			request.setUnitID(SLAVE_ID);
			ModbusResponse response = sendRequest(request);

			int low = ((ReadMultipleRegistersResponse) response).getRegisterValue(0);
			int high = ((ReadMultipleRegistersResponse) response).getRegisterValue(1);
			result = geneInt(low, high);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Roc readModbusValue() exception,address: " + address);
		}
		return result;
	}

	/**
	 * @description 读值从Modbus线圈
	 * @param address Modbus线圈地址
	 * @return 线圈中的值
	 */
	public static boolean readCoilValue(int address) throws Exception {
		boolean result = false;
		try {
			ReadCoilsRequest request = new ReadCoilsRequest(address, 1);
			request.setUnitID(SLAVE_ID);
			ModbusResponse response = sendRequest(request);
			result = ((ReadCoilsResponse) response).getCoilStatus(0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Roc readCoilValue() exception,address: " + address);
		}
		return result;
	}

	/**
	 * @description 写入Modbus寄存器中的值
	 * @param address Modbus地址（双字节）
	 * @return 写入是否成功
	 */
	public static boolean writeModbusValue(int address, int value) throws Exception {
		boolean flag = false;
		try {
			ModbusRequest request = null;
			int[] valSep = int4ToInt2(value);
			SimpleInputRegister[] values = { new SimpleInputRegister(valSep[0]), new SimpleInputRegister(valSep[1]) };
			request = new WriteMultipleRegistersRequest(address, values);
			request.setUnitID(SLAVE_ID);
			sendRequest(request);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Roc writeModbusValue() exception,address: " + address);
		}
		return flag;
	}

	/**
	 * @description 写入Modbus寄存器中的值
	 * @param address Modbus地址（单字节）
	 * @return 写入是否成功
	 */
	public static boolean writeModbusValue1(int address, int value) throws Exception {
		boolean flag = false;
		try {
			ModbusRequest request = null;
			request = new WriteSingleRegisterRequest(address, new SimpleInputRegister(value));
			request.setUnitID(SLAVE_ID);
			sendRequest(request);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Roc writeModbusValue1() exception,address: " + address);
		}
		return flag;
	}

	/**
	 * @description 写值到Modbus线圈
	 * @param address Modbus线圈地址
	 * @return 写入是否成功
	 */
	public static boolean writeCoilValue(int address, boolean value) throws Exception {
		boolean flag = false;
		try {
			ModbusRequest request = new WriteCoilRequest(address, value);
			request.setUnitID(SLAVE_ID);
			sendRequest(request);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("Roc writeCoilValue() exception,address: " + address);
		}
		return flag;
	}

	/**
	 * @description 发送modbus请求
	 * @param request ModbusRequest
	 * @return ModbusResponse
	 */
	private static ModbusResponse sendRequest(ModbusRequest request) throws ModbusException, UnknownHostException {

		transaction.setRetries(5);
		transaction.setRequest(request);
		try {
			transaction.execute();
		} catch (ModbusException e) {
			logger.warn("Roc PLCUtil.sendRequest() exception message: " + e.getMessage());
			throw new ModbusException(e.getMessage());
		}
		return transaction.getResponse();
	}

	/**
	 * @description 设置Modbus，并连接
	 * @param host    主机地址
	 * @param port    端口号
	 * @param slaveId modbus server编号
	 * @return 连接是否成功
	 */
	public static boolean setModbusConfig(String host, int port, int slaveId) {

		HOST = host;
		PORT = port;
		SLAVE_ID = slaveId;

		logger.warn("Roc PLCUtil.setModbusConfig(): IP:" + HOST + " Port:" + PORT + " Slave ID:" + SLAVE_ID);

		connection = null;

		boolean isHostKnown = true;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			isHostKnown = false;
			logger.warn("Roc UnknownHostException:" + host);
		}

		boolean isReachAble = true;
		if (isHostKnown) {
			try {
				isReachAble = addr.isReachable(10000);
			} catch (IOException e1) {
				e1.printStackTrace();
				isReachAble = false;
				logger.warn("Roc addr.isReachable() throw IOException");
			}
		}

		boolean isSucc = true;
		if (isReachAble) {
			connection = new TCPMasterConnection(addr);
			connection.setPort(port);
			try {
				connection.connect();
			} catch (Exception e) {
				e.printStackTrace();
				isSucc = false;
				logger.error(
						"Roc connection.connect()() throw Exception, addr is:" + addr + " Exception:" + e.getMessage());
			}
			if (isSucc) {
				transaction = new ModbusTCPTransaction(connection);
			}
		} else {
			isSucc = false;
		}
		return isSucc;
	}

	/**
	 * @description Modbus是否处于连接成功状态
	 * @return 连接是否成功
	 */
	public static boolean isConnected() {
		if (connection == null) {
			logger.warn("Roc isConnected(), connection==null");
			return false;
		}
		boolean isConn = connection.isConnected();
		return isConn;
	}

	/**
	 * @description 关闭Modbus连接
	 */
	public static void close() {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * @description 4 字节的int类型，取低2位字节，放入int[]
	 * @param val 待转换int值
	 * @return
	 */
	public static int[] int4ToInt2(int val) {
		int lowMark = 0x0000FFFF;
		int highMark = 0xFFFF0000;
		int low = lowMark & val;
		int high = highMark & val;
		high = high >> 16;
		int[] valSep = { low, high };
		return valSep;
	}

	/**
	 * @description 低2位字节值合并成整数
	 * @param low  字节
	 * @param high 字节
	 * @return int
	 */
	public static int geneInt(int low, int high) {
		int combind = (high << 16) | low;
		return combind;
	}

	/**
	 * @description 移动料斗到指定位置（层序号）
	 * @param positon 层序号
	 * @return 移动是否成功
	 */
	public static boolean moveLiftToTargetPosi(int position) {
		boolean doorOpen = isDoorOpen();
		if (doorOpen) {
			logger.info("Now Door opened,then command door closed");
			onCloseDoor(EnumDoor.ALL);
			while (isDoorOpen()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		logger.info("Move to target floor " + position);
		boolean flag = true;
		try {
			writeModbusValue1(TARGET_POSI_ADDRESS, position);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * @description 移动料斗到取料口
	 * @return 移动是否成功
	 */
	public static boolean moveLiftToDoorPosi() {
		boolean doorOpen = isDoorOpen();
		if (doorOpen) {
			logger.info("Now Door opened,then command door closed");
			onCloseDoor(EnumDoor.ALL);
			while (isDoorOpen()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		boolean flag = true;
		int position = 100;
		try {
			writeModbusValue1(TARGET_POSI_ADDRESS, position);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 移动料斗到最低层
	 * @return 移动是否成功
	 */
	public static boolean moveLiftToGroundPosi() {
		boolean doorOpen = isDoorOpen();
		if (doorOpen) {
			logger.info("门处于开启状态,执行关闭操作");
			onCloseDoor(EnumDoor.ALL);
			while (isDoorOpen()) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		boolean flag = true;
		int position = 200;
		try {
			writeModbusValue1(TARGET_POSI_ADDRESS, position);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 获得料斗的当前位置（层序号）
	 * @return 当前层序号
	 */
	public static int getLiftCurrPosi() {
		int posi = -1;
		try {
			posi = readModbusValue(CURRENT_POSI_ADDRESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return posi;
	}

	// SPEED_ADDRESS
	public static int getSpeed() {
		int speed = -1;
		try {
			speed = readModbusValue(SPEED_ADDRESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return speed;
	}

	public static boolean setSpeed(int speed) {
		boolean flag = true;
		try {
			writeModbusValue(SPEED_ADDRESS, speed);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 获得料斗到起始位置（层序号）
	 * @return 起始位置
	 */
	public static int getOriginPosi() {
		int originPos = -1;
		try {
			originPos = readModbusValue(ORIGIN_POSI_ADDRESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originPos;
	}

	/**
	 * @description 设定料斗层数对应的值
	 * @param fIndex 层序号
	 * @param value  位置参数
	 */
	public static boolean invokeFFunc(int fIndex, int value) {
		boolean flag = true;
		try {
			writeModbusValue(F_ADDRESS_ARRAY[fIndex], value);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 打开暂存柜指示灯
	 */
	public static boolean turnOnTempoCabinetLight() {
		setModbusConfig(HOST, PORT, SLAVE_ID);
		int res = 1; // light on
		boolean flag = true;
		try {
			writeModbusValue1(TEMPORARY_INDICATOR_ADDRESS, res);
			logger.info("打开暂存柜的灯状态: " + flag);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;

	}

	/**
	 * @description 关闭暂存柜指示灯
	 */
	public static boolean turnOffTempoCabinetLight() {
		setModbusConfig(HOST, PORT, SLAVE_ID);
		int res = 0; // light off
		boolean flag = true;
		try {
			writeModbusValue1(TEMPORARY_INDICATOR_ADDRESS, res);
			logger.info("Roc PLCUtil.turnOffTempoCabinetLight(),isSucc: " + flag);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 暂存柜指示灯闪烁
	 */
	public static boolean slinkTempoCabinetLight() {
		setModbusConfig(HOST, PORT, SLAVE_ID);
		int res = 2; // slink light
		boolean flag = true;
		try {
			writeModbusValue1(TEMPORARY_INDICATOR_ADDRESS, res);
			logger.warn("Roc PLCUtil.slinkTempoCabinetLight(),isSucc: " + flag);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 料斗点动上升
	 * @return 移动是否成功
	 */
	public static boolean onLiftUp() {
		boolean flag = true;
		try {
			writeCoilValue(ON_UP_COIL, true);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 料斗点动下降
	 * @return 移动是否成功
	 */
	public static boolean onLiftDown() {
		boolean flag = true;
		try {
			writeCoilValue(ON_DOWN_COIL, true);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 料斗停止移动
	 * @return 是否成功
	 */
	public static boolean stopLiftUp() {
		PLCUtil.setModbusConfig(HOST, PORT, SLAVE_ID);
		boolean flag = true;
		try {
			writeCoilValue(ON_UP_COIL, false);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 料斗停止下降
	 * @return 是否成功
	 */
	public static boolean stopLiftDown() {
		PLCUtil.setModbusConfig(HOST, PORT, SLAVE_ID);
		boolean flag = true;
		try {
			writeCoilValue(ON_DOWN_COIL, false);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 打开取料口的门
	 * @return 是否成功
	 */
	public static boolean onOpenDoor(EnumDoor door) {
		if (isDoorOpen()) {
			logger.info("门已被打开");
			return true;
		}
		boolean flag = true;
		try {
			// 开启左侧门
			if (door.equals(EnumDoor.LEFT) || door.equals(EnumDoor.ALL)) {
				writeCoilValue(ON_OPEN_DOOR_COIL, true);
			}
			Thread.sleep(200);
			// 开启右侧门
			if (door.equals(EnumDoor.RIGHT) || door.equals(EnumDoor.ALL)) {
				writeCoilValue(ON_OPEN2_DOOR_COIL, true);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 关闭取料口的门
	 * @return 是否成功
	 */
	public static boolean onCloseDoor(EnumDoor door) {
		boolean flag = true;
		try {
			// 关闭左侧门
			if (door.equals(EnumDoor.LEFT) || door.equals(EnumDoor.ALL)) {
				writeCoilValue(ON_CLOSE_DOOR_COIL, true);
			}
			Thread.sleep(200);
			// 关闭右侧门
			if (door.equals(EnumDoor.RIGHT) || door.equals(EnumDoor.ALL)) {
				writeCoilValue(ON_CLOSE2_DOOR_COIL, true);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 料斗复位
	 * @return 是否成功
	 */
	public static boolean resetLift() {
		boolean flag = true;
		try {
			writeCoilValue(ON_RESET_LIFT_COIL, true);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 移动料斗后，料斗是否到达
	 * @return 是否成功
	 */
	public static boolean isArrive() {
		boolean arrive = false;
		try {
			arrive = readCoilValue(ON_ARRIVE_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrive;
	}

	/**
	 * @description 取料口门是否打开(区分1个门，2个门，分开判断)
	 * @return 打开状态返回true
	 */
	public static boolean isOpenDoor(boolean isCTypeCabinet) {
		boolean open = false;
		try {
			if(isCTypeCabinet) {
				open = readCoilValue(ON_CLOSE_AND_CHECK_COIL);
				Thread.sleep(200);
				open = readCoilValue(ON_CLOSE_AND_CHECK2_COIL);
			}else {
				open = readCoilValue(ON_CLOSE_AND_CHECK_COIL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return open;
	}
	
	/**
	 * @description 取料口门是否打开
	 * @return 打开状态返回true
	 */
	public static boolean isDoorOpen() {
		boolean open = false;
		try {
			open = readCoilValue(ON_CLOSE_AND_CHECK_COIL);
			Thread.sleep(200);
			open = readCoilValue(ON_CLOSE_AND_CHECK2_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return open;
	}

	/**
	 * @description 读取PLC故障标志
	 * @return 是否故障
	 */
	public static boolean isNonFault() {
		boolean nonFault = false;
		try {
			nonFault = readCoilValue(ON_FAULT_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nonFault;
	}

	/**
	 * @description 料斗到达标志清除
	 * @return 是否成功
	 */
	public static boolean clearArriveFlag() {
		boolean isSucc = false;
		try {
			isSucc = writeCoilValue(ON_ARRIVE_COIL, false);
		} catch (Exception e) {
			isSucc = false;
			e.printStackTrace();
		}
		return isSucc;
	}

	/**
	 * @description 清除归还扫描仪标志位
	 * @return 是否成功
	 */
	public static boolean clearScanFlag() {
		boolean isSucc = false;
		try {
			isSucc = writeCoilValue(ON_SCAN_COIL, false);
		} catch (Exception e) {
			isSucc = false;
			e.printStackTrace();
		}
		return isSucc;
	}

	/**
	 * @description 移动料斗到最高位置警告
	 * @return 是否成功
	 */
	public static boolean isMaxLocationWarning() {
		boolean flag = false;
		try {
			flag = readCoilValue(MAX_LOCATION_WARNING_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 料斗最低位置警告
	 * @return 是否成功
	 */
	public static boolean isMinLocationWarning() {
		boolean flag = false;
		try {
			flag = readCoilValue(MIN_LOCATION_WARNING_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 是否步进电机故障
	 * @return 是否故障
	 */
	public static boolean isMotorDriverWarning() {
		boolean flag = false;
		try {
			flag = readCoilValue(MOTOR_DRIVER_WARNING_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 取料口开门失败警告
	 * @return 是否成功
	 */
	public static boolean isOpenDoorFailWarning() {
		boolean flag = false;
		try {
			flag = readCoilValue(ON_OPEN_DOOR_FAIL_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 取料口关门失败警告
	 * @return 是否成功
	 */
	public static boolean isCloseDoorFailWarning() {
		boolean flag = false;
		try {
			flag = readCoilValue(ON_CLOSE_DOOR_FAIL_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 回收门是否开门故障
	 * @return 是否故障
	 */
	public static boolean isOpenRetriDoorFailWarning() {
		boolean flag = false;
		try {
			flag = readCoilValue(ON_OPEN_RETRI_DOOR_FAIL_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 回收是否置位（指示回收扫描仪要开始启动了）
	 * @return 是否成功
	 */
	public static boolean isScan() {
		boolean flag = false;
		try {
			flag = readCoilValue(ON_SCAN_COIL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 设置回收的类型，如研磨，报废等
	 * @return 是否成功
	 */
	public static boolean setScanRes(int res) {
		setModbusConfig(HOST, PORT, SLAVE_ID);
		logger.info("Set scan result " + res);
		boolean flag = true;
		try {
			writeModbusValue1(SCAN_RES_ADDRESS, res);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description set PLC ip adderess
	 * @param ip like this 192.168.1.89
	 */
	public static boolean setIP(String ip) {
		setModbusConfig(HOST, PORT, SLAVE_ID);
		String[] tokens = ip.split("\\.");
		if (tokens.length != 4) {
			return false;
		}
		int ip1 = Integer.parseInt(tokens[0]);
		int ip2 = Integer.parseInt(tokens[1]);
		int ip3 = Integer.parseInt(tokens[2]);
		int ip4 = Integer.parseInt(tokens[3]);
		boolean flag = true;
		try {
			writeModbusValue1(IP_SEGMENT1_ADDRESS, ip1);
			writeModbusValue1(IP_SEGMENT2_ADDRESS, ip2);
			writeModbusValue1(IP_SEGMENT3_ADDRESS, ip3);
			writeModbusValue1(IP_SEGMENT4_ADDRESS, ip4);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		if (flag) {
			try {
				flag = writeCoilValue(MODIFY_IP_COIL, true);
			} catch (Exception e) {
				flag = false;
				e.printStackTrace();
			}
		}
		return flag;
	}

	/**
	 * @description 操作PLC
	 * @param plcOpType plc操作类型
	 * @throws Exception
	 */
	public static void opPlc(PlcOpType plcOpType) throws Exception {
		Boolean flag = null;
		PLCUtil.setModbusConfig(HOST, PORT, SLAVE_ID);
		Boolean value = true;
		// 点动上升、点动下降为暂停
		switch (plcOpType) {
		case TO_DOWN_ZERO: // 一键补料
			flag = writeModbusValue(plcOpType.getCode(), 200);
			break;
//		case RETURN_BACK_DOOR_REST: // 回收口复位
//			flag = writeCoilValue(plcOpType.getCode(), plcOpType.getResVal() == 1);
//			break;
//		case ON_UP_COIL:
//			writeCoilValue(PlcOpType.ON_DOWN_COIL.getCode(), false);
//		case ON_DOWN_COIL:
//			writeCoilValue(PlcOpType.ON_UP_COIL.getCode(), false);
		default:
			value = !readCoilValue(plcOpType.getCode());
			flag = writeCoilValue(plcOpType.getCode(), value);
			break;
		}
		if (flag == null || !flag) {
			throw new Exception(plcOpType.getDesc() + "失败");
		}
	}

	/**
	 * @description 取料口开门，同时开始状态检测
	 * @return 是否成功
	 */
	public static boolean openDoorWithStatusChecking(String cabinetId, EnumDoor door) {
		PLCUtil.setModbusConfig(HOST, PORT, SLAVE_ID);
		boolean isRunCmdWell = onOpenDoor(door);

		String messageToWeb = "取料口门正在打开...";
		WebSocketUtil.sendMsgSingle(cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));

		if (door.equals(EnumDoor.LEFT) || door.equals(EnumDoor.ALL)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					logger.info("查询左侧开门状态线程启动");
					queryDoorStatsTillActionDone(10, OPEN_DOOR_ACTION, cabinetId);
				}
			}).start();
		}

		if (door.equals(EnumDoor.RIGHT) || door.equals(EnumDoor.ALL)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					logger.info("查询右侧开门状态线程启动");
					queryDoorStatsTillActionDone(10, OPEN_DOOR2_ACTION, cabinetId);
				}
			}).start();
		}

		return isRunCmdWell;
	}

	/**
	 * @description 取料口关门，同时开始状态检测
	 * @return 是否成功
	 */
	public static boolean closeDoorWithStatusChecking(String cabinetId) {
		PLCUtil.setModbusConfig(HOST, PORT, SLAVE_ID);
		boolean isRunCmdWell = onCloseDoor(EnumDoor.ALL);
		String messageToWeb = "取料口门正在关闭...";
		WebSocketUtil.sendMsgSingle(cabinetId, new WsMsg(WsMsgType.TEXT, messageToWeb));
		new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("查询左侧关门状态线程启动");
				queryDoorStatsTillActionDone(10, CLOSE_DOOR_ACTION, cabinetId);
			}
		}).start();

		new Thread(new Runnable() {
			@Override
			public void run() {
				logger.info("查询右侧关门状态线程启动");
				queryDoorStatsTillActionDone(10, CLOSE_DOOR2_ACTION, cabinetId);
			}
		}).start();
		return isRunCmdWell;
	}

	/**
	 * @description 查询PLC取料口门状态，直到关闭/打开或者超时
	 * @param timeout       超时 单位秒
	 * @param action        关门或者开门
	 * @param cabinetIdPara 柜子Id
	 */
	private static boolean queryDoorStatsTillActionDone(int timeout, int action, String cabinetIdPara) {
		boolean isOpen = false;
		if (action == CLOSE_DOOR_ACTION || action == CLOSE_DOOR2_ACTION) {
			isOpen = true;
		}
		long dur = timeout * 1000;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		boolean isTimeout = true;
		while ((now - start) < dur) {
			try {
				Thread.sleep(500);
				if (action == CLOSE_DOOR_ACTION) {
					isOpen = readCoilValue(PLCUtil.ON_CLOSE_AND_CHECK_COIL);
				} else {
					isOpen = readCoilValue(PLCUtil.ON_CLOSE_AND_CHECK2_COIL);
				}
			} catch (Exception e) {
				e.printStackTrace();
				isOpen = false; // exception
			}
			if (action == OPEN_DOOR_ACTION) {
				if (isOpen) {
					String messageToWeb = "取料口" + (action == CLOSE_DOOR_ACTION ? "左侧" : "右侧") + "门已经打开";
					logger.info(messageToWeb);
					WebSocketUtil.sendMsgSingle(cabinetIdPara, new WsMsg(WsMsgType.DOOROPENED, messageToWeb));
					isTimeout = false;
					break; // opened
				}
			} else if (action == CLOSE_DOOR_ACTION) {
				if (!isOpen) {
					String messageToWeb = "取料口" + (action == CLOSE_DOOR_ACTION ? "左侧" : "右侧") + "门已经关闭";
					logger.info(messageToWeb);
					WebSocketUtil.sendMsgSingle(cabinetIdPara, new WsMsg(WsMsgType.DOORCLOSED, messageToWeb));
					isTimeout = false;
					break; // closed
				}
			}
			now = System.currentTimeMillis();
		}
		if (isTimeout) {
			logger.error("Query Door Stats Till Action Done Timeout");
		}
		return isOpen;
	}
}