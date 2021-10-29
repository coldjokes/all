package com.dosth.comm.motorboard;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;;

/**
 * Modbus工具类 for Motor Board with JAMOD Read synchronous
 * blocking(下达马达步进命令后读工作状态采取同步阻塞的的方式） int flag =
 * ModbusUtil.isMotorMoveDone(motorIndex,30); 马达工作未完成之前此处一致等待，直到timeout 30s
 * if(flag==0) 马达旋转完成 read sttatus; else if(flag==1) Modbus Read Exception; else
 * if(flag==2) Motro Movement Timeout;
 */
public class ModbusUtil {

	// to be cosistent with GUI,we put 0 in the array,but never use it
	public static final Logger logger = LoggerFactory.getLogger(ModbusUtil.class);

	public static String HOST = "192.168.2.46";
	public static int PORT = 502;
	public static int SLAVE_ID = 1;

	public static int MAX_MOTOR_COUNT = 10;
	public static int MAX_ROTATED_COUNT = 99;

//	public static int[] MotorRegister = {0,10,11,12,13,14,15,16,17,18,19};
	public static int[] MotorRegister = { 0, 19, 18, 17, 16, 15, 14, 13, 12, 11, 10 };

	public static final int RESET_ADDRESS = 1;
	public static final int OVERLOAD_FAULT_ADDRESS = 2;
	public static final int OFF_CIRCUIT_ADDRESS = 3;
	public static final int SHORT_CIRCUIT_ADDRESS = 4;
	public static final int FAULT_LIGHT_ADDRESS = 5;
	public static final int BACK_TO_ORIGHIN_ADDRESS = 6;
	public static final int LOCATION_LED_ADDRESS = 7;
	public static final int MICRO_MOVE_SWITHCH_ADDRESS4 = 8;
	public static final int[] StatusRegister = { 0, RESET_ADDRESS, OVERLOAD_FAULT_ADDRESS, OFF_CIRCUIT_ADDRESS,
			SHORT_CIRCUIT_ADDRESS, FAULT_LIGHT_ADDRESS, BACK_TO_ORIGHIN_ADDRESS, LOCATION_LED_ADDRESS,
			MICRO_MOVE_SWITHCH_ADDRESS4 };

	public static final int OVERLOAD_FAULT = 1;
	public static final int OFF_CIRCUIT_FAULT = 2;
	public static final int SHORT_CIRCUIT_FAULT = 4;
	public static final int MICRO_MOVE_SWITHCH_FAULT = 8;
	public static final int[] Fault = { OVERLOAD_FAULT, OFF_CIRCUIT_FAULT, SHORT_CIRCUIT_FAULT,
			MICRO_MOVE_SWITHCH_FAULT };

	public static final int MOTOR1_MARK = 1;
	public static final int MOTOR2_MARK = 2;
	public static final int MOTOR3_MARK = 4;
	public static final int MOTOR4_MARK = 8;
	public static final int MOTOR5_MARK = 16;
	public static final int MOTOR6_MARK = 32;
	public static final int MOTOR7_MARK = 64;
	public static final int MOTOR8_MARK = 128;
	public static final int MOTOR9_MARK = 256;
	public static final int MOTOR10_MARK = 512;
	public static int[] MARK = { 0, MOTOR1_MARK, MOTOR2_MARK, MOTOR3_MARK, MOTOR4_MARK, MOTOR5_MARK, MOTOR6_MARK,
			MOTOR7_MARK, MOTOR8_MARK, MOTOR9_MARK, MOTOR10_MARK };

	public static TCPMasterConnection connection;
	private static ModbusTCPTransaction transaction;
	private static int MotorMoveDoneTimeout = 120;

	/**
	 * 给指定地址类型寄存器读取数据
	 * 
	 * @param host    modbus服务器地址
	 * @param port    modbus服务器端口
	 * @param slaveId 客户端Id
	 * @param address 寄存器地址类型
	 * @throws Exception
	 */
	public static int readModbusValue(int address) throws Exception {
		int result = -1;
		try {
			ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(address, 1);
			request.setUnitID(SLAVE_ID);
			ModbusResponse response = sendRequest(request);
			result = ((ReadMultipleRegistersResponse) response).getRegisterValue(0);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("ModbusUtil.readModbusValue() exception,address: " + address);
		}
		return result;
	}

	/**
	 * 给指定地址类型寄存器写入数据
	 * 
	 * @param address 寄存器地址
	 * @param value   输入值
	 * @throws Exception
	 */
	public static boolean writeModbusValue(int address, int value) throws Exception {
		boolean flag = false;
		try {
			ModbusRequest request = null;
			request = new WriteSingleRegisterRequest(address, new SimpleInputRegister(value));
			request.setUnitID(SLAVE_ID);
			sendRequest(request);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("ModbusUtil.writeModbusValue() exception,address: " + address);
		}
		return flag;
	}

	/**
	 * 发送ModbusRequest
	 * 
	 * @param request
	 * @return
	 * @throws ModbusException
	 * @throws UnknownHostException
	 */
	private static ModbusResponse sendRequest(ModbusRequest request) throws ModbusException, UnknownHostException {

		transaction.setRetries(5);
		transaction.setRequest(request);
		try {
			transaction.execute();
		} catch (ModbusException e) {
			logger.warn("ModbusUtil.sendRequest() exception message: " + e.getMessage());
			throw new ModbusException(e.getMessage());

		}
		return transaction.getResponse();
	}

	// init(clear) status register
	public static void initRegister(int motorIndex) {
		for (int i = 1; i < ModbusUtil.StatusRegister.length; i++) {
			int address = ModbusUtil.StatusRegister[i];
			int init_mark = ~ModbusUtil.MARK[motorIndex];
			try {
				int read_res = readModbusValue(address);
				int init_val = init_mark & read_res;
				ModbusUtil.writeModbusValue(address, init_val);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//
	public static boolean setModbusConfig(String host, int port, int slaveId) {

		HOST = host;
		PORT = port;
		SLAVE_ID = slaveId;

		connection = null;
		logger.warn("ModbusUtil.setModbusConfig(): IP:" + HOST + " Port:" + PORT + " Slave ID:" + SLAVE_ID);

		boolean isHostKnown = true;
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(host);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			isHostKnown = false;
			logger.warn("ModbusUtil.setModbusConfig() UnknownHostException message: " + e1.getMessage());
		}

		boolean isReachAble = true;
		if (isHostKnown) {
			try {
				isReachAble = addr.isReachable(10000);
			} catch (IOException e1) {
				e1.printStackTrace();
				isReachAble = false;
				logger.warn("ModbusUtil.isReachable() IOException message: " + e1.getMessage());
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
				logger.warn(" ModbusUtil.connection() Exception message: " + e.getMessage());
			}
			if (isSucc) {
				transaction = new ModbusTCPTransaction(connection);
			}
		} else {
			isSucc = false;
			logger.warn(" ModbusUtil.isReachable() : false");
		}
		return isSucc;
	}

	// motor move
	public static boolean motorMove(int motorIndex, int rotatedCount) {
		// check the parameter,they should be within the limits
		if (motorIndex == 0 || rotatedCount == 0) {
			return false;
		}

		if (motorIndex > ModbusUtil.MAX_MOTOR_COUNT || rotatedCount > ModbusUtil.MAX_ROTATED_COUNT) {
			return false;
		}
		boolean flag = true;

		int address = ModbusUtil.MotorRegister[motorIndex];
		try {
			ModbusUtil.writeModbusValue(address, rotatedCount);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	// PCB reset faulst
	public static void resetMotor(int motorIndex) {
		int address = ModbusUtil.RESET_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int write_val = (~markVal) & read_res;
			ModbusUtil.writeModbusValue(address, write_val);
			logger.warn("ModbusUtil.resetMotor(), Read " + address + " Response " + read_res);
			logger.warn("ModbusUtil.resetMotor(), Write " + address + " Value " + write_val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// reset short circuit
	public static void resetShortCircuitFault(int motorIndex) {
		int address = ModbusUtil.SHORT_CIRCUIT_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int write_val = (~markVal) & read_res;
			ModbusUtil.writeModbusValue(address, write_val);
			logger.warn("ModbusUtil.resetShortCircuitFault(), Read " + address + " Response " + read_res);
			logger.warn("ModbusUtil.resetShortCircuitFault(), Write " + address + " Value " + write_val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// reset short circuit
	public static void resetOverLoadFault(int motorIndex) {
		int address = ModbusUtil.OVERLOAD_FAULT_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int write_val = (~markVal) & read_res;
			ModbusUtil.writeModbusValue(address, write_val);
			logger.warn("ModbusUtil.resetOverLoadFault(), Read " + address + " Response " + read_res);
			logger.warn("ModbusUtil.resetOverLoadFault(), Write " + address + " Value " + write_val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// make light on
	public static boolean onLight(int motorIndex) {
		boolean flag = true;
		int address = ModbusUtil.BACK_TO_ORIGHIN_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int write_val = markVal | read_res;
			ModbusUtil.writeModbusValue(address, write_val);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public static boolean offLight(int motorIndex) {
		boolean flag = true;
		int address = ModbusUtil.BACK_TO_ORIGHIN_ADDRESS;
		int markVal = ~ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int write_val = markVal & read_res;
			ModbusUtil.writeModbusValue(address, write_val);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public static boolean glitterLight(int motorIndex) {
		boolean flag = true;
		int address = ModbusUtil.LOCATION_LED_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int write_val = markVal | read_res;
			ModbusUtil.writeModbusValue(address, write_val);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	//
	public static boolean isFaultLightOn(int motorIndex) {
		boolean flag = false;
		int address = ModbusUtil.FAULT_LIGHT_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = ModbusUtil.readModbusValue(address);
			int val = markVal & read_res;
			if (val != 0) {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;

	}

	public static int readMotorStatusData(int motorIndex) {
		int status = 0;
		int mark = MARK[motorIndex];
		try {
			int response = readModbusValue(OVERLOAD_FAULT_ADDRESS);
			int selfVal = mark & response;
			if (selfVal != 0) {
				status += OVERLOAD_FAULT;
			}
			response = readModbusValue(OFF_CIRCUIT_ADDRESS);
			selfVal = mark & response;
			if (selfVal != 0) {
				status += OFF_CIRCUIT_FAULT;
			}
			response = readModbusValue(SHORT_CIRCUIT_ADDRESS);
			selfVal = mark & response;
			if (selfVal != 0) {
				status += SHORT_CIRCUIT_FAULT;
			}
			response = readModbusValue(MICRO_MOVE_SWITHCH_ADDRESS4);
			selfVal = mark & response;
			if (selfVal != 0) {
				status += MICRO_MOVE_SWITHCH_FAULT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}

	public static String parseStatusData(int status) {
		if (status == 0) {
			return "正常 ";
		}
		if ((status & OVERLOAD_FAULT) != 0) {
			return "过载故障 ";
		}
		if ((status & OFF_CIRCUIT_FAULT) != 0) {
			return "断路故障 ";
		}
		if ((status & SHORT_CIRCUIT_FAULT) != 0) {
			return "短路故障 ";
		}
		if ((status & MICRO_MOVE_SWITHCH_FAULT) != 0) {
			return "微动开关故障 ";
		}
		return "";
	}

	// timeout in second,default value is 60 second
	// 0-done 1-exception 2-timeout
	public static int isMotorMoveDone(int motorIndex, int timeout) {

		int flag = 2;
		int address = MotorRegister[motorIndex];
		long dur = timeout * 1000; // millsecond
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while ((now - start) < dur) {
			try {
				int response = readModbusValue(address);
				if (response == 0) {
					flag = 0; // done
				}
			} catch (Exception e) {
				e.printStackTrace();
				flag = 1; // exception
			}

			if (flag == 0 || flag == 1) {
				break;
			}
			now = System.currentTimeMillis();
		}
		return flag;
	}

	public static String getStatusStr(int motorIndex) {
		return parseStatusData(readMotorStatusData(motorIndex));
	}

	public static boolean isConnected() {
		return connection != null;
	}

	// in second
	public static void setMotorMoveDoneTimeout(int timeout) {
		MotorMoveDoneTimeout = timeout;
	}

	// in second
	public static int getMotorMoveDoneTimeout() {
		return MotorMoveDoneTimeout;
	}

	public static void close() {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * 故障恢复
	 */
	public static void reset(String ip) {
		logger.info("故障恢复");
		try {
			setModbusConfig(ip, 502, 1);
			ModbusUtil.writeModbusValue(19, 0); // 物理1号电机取数置零
			ModbusUtil.writeModbusValue(18, 0); // 物理2号电机取数置零
			ModbusUtil.writeModbusValue(17, 0); // 物理3号电机取数置零
			ModbusUtil.writeModbusValue(16, 0); // 物理4号电机取数置零
			ModbusUtil.writeModbusValue(15, 0); // 物理5号电机取数置零
			ModbusUtil.writeModbusValue(14, 0); // 物理6号电机取数置零
			ModbusUtil.writeModbusValue(13, 0); // 物理7号电机取数置零
			ModbusUtil.writeModbusValue(12, 0); // 物理8号电机取数置零
			ModbusUtil.writeModbusValue(11, 0); // 物理9号电机取数置零
			ModbusUtil.writeModbusValue(10, 0); // 物理10号电机取数置零
			
			ModbusUtil.writeModbusValue(1, 0); // 过载
			ModbusUtil.writeModbusValue(2, 0); // 开路
			ModbusUtil.writeModbusValue(3, 0); // 短路
			ModbusUtil.writeModbusValue(7, 0); // 微动开关
			

			ModbusUtil.writeModbusValue(4, 0); // 故障指示灯
			ModbusUtil.writeModbusValue(5, 0); // 回原点
			ModbusUtil.writeModbusValue(6, 0); // 定位指示灯
			ModbusUtil.writeModbusValue(8, 0); // 其他
		} catch (Exception e) {
			logger.error("故障恢复错误");
			e.printStackTrace();
		} finally {
			ModbusUtil.close();
		}
	}
	
	/**
     * @description 读取电机状态
     * @return 0 无故障, 大于0 有故障
     */
    public static int check(String ip) {
        logger.info("读取电机状态");
        try {
            int result = 0;
        	setModbusConfig(ip, 502, 1);
//            int[] results = ModbusUtil.readBatch(1, 0, 10);
//            result += results[1];
//            result += results[2];
//            result += results[3];
//            result += results[7];
            result += ModbusUtil.readModbusValue(1);
            result += ModbusUtil.readModbusValue(2);
            result += ModbusUtil.readModbusValue(3);
            result += ModbusUtil.readModbusValue(7);
            return result;
        } catch (Exception e) {
            logger.error("读取电机状态异常");
            e.printStackTrace();
        } finally {
            ModbusUtil.close();
        }
        return 0;
    }
    
    
    
    /**
	 * 故障恢复
	 */
	public static void wrong(String ip) {
		logger.info("故障恢复");
		try {
			setModbusConfig(ip, 502, 1);
			ModbusUtil.writeModbusValue(1, 1); // 过载
			ModbusUtil.writeModbusValue(2, 2); // 开路
			ModbusUtil.writeModbusValue(3, 4); // 短路
			ModbusUtil.writeModbusValue(7, 8); // 微动开关
		} catch (Exception e) {
			logger.error("故障恢复错误");
			e.printStackTrace();
		} finally {
			ModbusUtil.close();
		}
	}
}