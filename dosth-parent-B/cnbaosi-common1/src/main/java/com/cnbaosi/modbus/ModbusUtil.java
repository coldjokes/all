package com.cnbaosi.modbus;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnbaosi.modbus.enums.AddrType;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ModbusRequest;
import net.wimpi.modbus.msg.ModbusResponse;
import net.wimpi.modbus.msg.ReadCoilsRequest;
import net.wimpi.modbus.msg.ReadCoilsResponse;
import net.wimpi.modbus.msg.ReadInputDiscretesRequest;
import net.wimpi.modbus.msg.ReadInputDiscretesResponse;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteCoilRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;

/**
 * @description modbus工具类
 * @author guozhidong
 *
 */
public final class ModbusUtil {

	private static final Logger logger = LoggerFactory.getLogger(ModbusUtil.class);

	private static int SLAVE_ID = 1;

	private static ModbusServerConfig config;

	private static TCPMasterConnection connection;

	private static ModbusRequest request;

	private static ModbusUtil util = new ModbusUtil();

	private ModbusUtil() {
	}

	public static ModbusUtil getInstance(ModbusServerConfig config) {
		ModbusUtil.config = config;
		return util;
	}

	/**
	 * @description 创建connection
	 * 
	 * @param host
	 * @param port 服务器端口
	 * @return
	 */
	public static TCPMasterConnection createConnection() {
		TCPMasterConnection connection = null;
		try {
			InetAddress addr = InetAddress.getByName(config.getHost());
			connection = new TCPMasterConnection(addr);
			connection.setPort(config.getPort());
			connection.connect();
		} catch (Exception e) {
			logger.error("TCPMasterConnection创建失败:", e.getMessage());
		}
		return connection;
	}

	/**
	 * @description 关闭连接
	 */
	public static void close() {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * @description 给指定地址类型寄存器读取数据
	 * 
	 * @param slaveId  客户端Id
	 * @param addrType 寄存器地址类型
	 * @throws ModbusException
	 */
	public static int readModbusValue(int slaveId, AddrType addrType) throws ModbusException {
		try {
			// 当读取的是料斗地址,则重新设置料斗的slaveId
			switch (addrType) {
			case TopBoundAlarm:
			case BottomBoundAlarm:
			case ServoAlarm:
			case LeftDoorOpenFail:
			case LeftDoorCloseFail:
			case RightDoorOpenFail:
			case RightDoorCloseFail:
			case DoorCheck:
				SLAVE_ID = 1;
				break;
			default:
				break;
			}

			switch (addrType.getDataType()) {
			case COIL_STATUS:
				request = new ReadCoilsRequest(addrType.getAddress(), 1);
				break;
			case INPUT_STATUS:
				request = new ReadInputDiscretesRequest(addrType.getAddress(), 1);
				break;
			case HOLDING_REGISTER:
				request = new ReadMultipleRegistersRequest(addrType.getAddress(), 1);
				break;
			case INPUT_REGISTERS:
				request = new ReadInputRegistersRequest(addrType.getAddress(), 1);
				break;
			default:
				break;
			}
			request.setUnitID(SLAVE_ID);
			ModbusResponse response = sendRequest(request);
			int result = 0;
			switch (addrType.getDataType()) {
			case COIL_STATUS:
				result = ((ReadCoilsResponse) response).getCoilStatus(0) ? 1 : 0;
				break;
			case INPUT_STATUS:
				result = ((ReadInputDiscretesResponse) response).getDiscreteStatus(0) ? 1 : 0;
				break;
			case HOLDING_REGISTER:
				result = ((ReadMultipleRegistersResponse) response).getRegisterValue(0);
				break;
			case INPUT_REGISTERS:
				result = ((ReadInputRegistersResponse) response).getRegisterValue(0);
				break;
			default:
				throw new ModbusException(addrType.getDataType().getRw().getDesc());
			}
			return result;
		} finally {
			close();
		}
	}

	/**
	 * @description 给指定地址类型寄存器写入数据
	 * 
	 * @param slaveId  客户端Id
	 * @param addrType 寄存器地址类型
	 * @param value    输入值
	 * @throws ModbusException
	 */
	public static Boolean writeModbusValue(int slaveId, AddrType addrType, int value) throws ModbusException {
		boolean flag = false;
		// 当写入的是料斗地址,则重新指定slaveId
		switch (addrType) {
		case TopBoundAlarm:
		case BottomBoundAlarm:
		case ServoAlarm:
		case DoorCheck:
		case LeftDoorOpenFail:
		case LeftDoorCloseFail:
		case RightDoorOpenFail:
		case RightDoorCloseFail:
			slaveId = SLAVE_ID;
			break;
		default:
			break;
		}

		try {
			switch (addrType.getDataType()) {
			case COIL_STATUS:
				request = new WriteCoilRequest(addrType.getAddress(), value == 1);
				break;
			case HOLDING_REGISTER:
				request = new WriteSingleRegisterRequest(addrType.getAddress(), new SimpleInputRegister(value));
				break;
			case INPUT_STATUS:
			case INPUT_REGISTERS:
			default:
				logger.error("写入请求错误,类型:{},描述:{}", addrType.getDataType().getName(),
						addrType.getDataType().getRw().getDesc());
				throw new ModbusException(addrType.getDataType().getRw().getDesc());
			}
			request.setUnitID(slaveId);
			sendRequest(request);
			flag = true;
		} finally {
			close();
		}
		return flag;
	}

	/**
	 * @description 发送ModbusRequest
	 * 
	 * @param request
	 * @return
	 * @throws ModbusException
	 */
	private static ModbusResponse sendRequest(ModbusRequest request) throws ModbusException {
		connection = createConnection();
		ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection); // the transaction
		transaction.setRetries(5);
		transaction.setRequest(request);
		try {
			transaction.execute();
		} catch (ModbusException e) {
			throw new ModbusException(e.getMessage());
		}
		return transaction.getResponse();
	}
}