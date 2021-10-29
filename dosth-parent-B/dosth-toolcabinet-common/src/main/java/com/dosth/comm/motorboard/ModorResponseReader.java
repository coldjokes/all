package com.dosth.comm.motorboard;

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
import net.wimpi.modbus.procimg.SimpleInputRegister;

public class ModorResponseReader implements Runnable {
	public static final Logger logger = LoggerFactory.getLogger(ModorResponseReader.class);
	private int motorIndex;
	private ProcessMotorResponse processer;

	private boolean isDone;

	private String HOST = "192.168.2.46";
	private int PORT = 502;
	private int SLAVE_ID = 1;
	private final int max_reset_count = 3;

	TCPMasterConnection connection;

	public ModorResponseReader() {

	}

	public ModorResponseReader(String host, int port, int slave_id, int motorIndex, ProcessMotorResponse processer) {
		this.motorIndex = motorIndex;
		this.processer = processer;
		HOST = host;
		PORT = port;
		SLAVE_ID = slave_id;

		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(HOST);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		connection = new TCPMasterConnection(addr);
		connection.setPort(PORT);
		try {
			connection.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setMotorIndex(int motorIndex) {
		this.motorIndex = motorIndex;
	}

	public void setProcesser(ProcessMotorResponse processer) {
		this.processer = processer;
	}

	public void run() {
		isDone = false;
		int flag = isMotorMoveDone(motorIndex, ModbusUtil.getMotorMoveDoneTimeout());
		if (flag == 0) {
			int response = readMotorStatusData(motorIndex);
			processer.processMotorResponse(motorIndex, response);
		} else {
			processer.processMotorResponse(motorIndex, -1); // input -1 if exception or timeout occur
		}
		isDone = true;
		connection.close();
	}

	public boolean isDone() {
		return isDone;
	}

	private int readModbusValue(int address) throws Exception {
		logger.info("Enter ModorResponseReader.readModbusValue(),address " + address);
		int result = 0;
		try {
			ModbusRequest request = null;
			request = new ReadMultipleRegistersRequest(address, 1);
			request.setUnitID(SLAVE_ID);

			ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
			transaction.setRetries(5);
			transaction.setRequest(request);

			try {
				transaction.execute();
			} catch (ModbusException e) {
				logger.warn("transaction.execute() exception: " + e.getMessage());
				throw new ModbusException(e.getMessage());
			}

			ModbusResponse response = transaction.getResponse();
			result = ((ReadMultipleRegistersResponse) response).getRegisterValue(0);
		} finally {
//			connection.close();
		}

		return result;
	}

	//
	private boolean writeModbusValue(int address, int value) {
		logger.info("读取modbus键值对,address-value: " + address + "-" + value);
		boolean flag = true;
		try {
			ModbusRequest request = null;
			request = new WriteSingleRegisterRequest(address, new SimpleInputRegister(value));
			request.setUnitID(SLAVE_ID);

			ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
			transaction.setRetries(5);
			transaction.setRequest(request);
			try {
				transaction.execute();
			} catch (ModbusException e) {
				flag = false;
				logger.error("transaction.execute() exception: " + e.getMessage());
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			logger.warn("读取modbus exception: " + e.getMessage());
		}

		return flag;
	}

	// timeout in second,default value is 60 second
	// 0-done 1-exception 2-timeout
	private int isMotorMoveDone(int motorIndex, int timeout) {
		logger.info(motorIndex + " motor enter isMotorMoveDone");
		int flag = 2;
		int address = ModbusUtil.MotorRegister[motorIndex];

		long dur = timeout * 1000; // millsecond
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		int reset_count_on_fault = 0;
		while ((now - start) < dur) {
			myWait(100);
			try {
				int response = readModbusValue(address);
				if (response == 0) {
					flag = 0; // done
				} else { // not done
					myWait(100);
					if (readMotorResetStatus(motorIndex) != 0) {
						reset_count_on_fault++;
						if (reset_count_on_fault > max_reset_count) {
							logger.info("MotorBorar HOST " + HOST + " MotorIndex " + this.motorIndex
									+ " reset more than 3 times on fault");
							flag = 2;
							break;
						} else {
							logger.info(
									"MotorBorar HOST " + HOST + " MotorIndex " + this.motorIndex + " reset on fault");
							myWait(100);
							resetShortCircuitFault(motorIndex);
							myWait(100);
							resetOverLoadFault(motorIndex);
							myWait(100);
							resetMotor(motorIndex);
						}
					}
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
		logger.info(
				"MotorBorar HOST " + HOST + " MotorIndex " + this.motorIndex + " isMotorMoveDone() flag is " + flag);
		return flag;
	}

	private int readMotorStatusData(int motorIndex) {

		int status = 0;
		int mark = ModbusUtil.MARK[motorIndex];

		try {
			int response = readModbusValue(ModbusUtil.OVERLOAD_FAULT_ADDRESS);

			int selfVal = mark & response;
			if (selfVal != 0) {
				status += ModbusUtil.OVERLOAD_FAULT;
			}

			response = readModbusValue(ModbusUtil.OFF_CIRCUIT_ADDRESS);
			selfVal = mark & response;
			if (selfVal != 0) {
				status += ModbusUtil.OFF_CIRCUIT_FAULT;
			}

			response = readModbusValue(ModbusUtil.SHORT_CIRCUIT_ADDRESS);
			selfVal = mark & response;
			if (selfVal != 0) {
				status += ModbusUtil.SHORT_CIRCUIT_FAULT;
			}

			response = readModbusValue(ModbusUtil.MICRO_MOVE_SWITHCH_ADDRESS4);
			selfVal = mark & response;
			if (selfVal != 0) {
				status += ModbusUtil.MICRO_MOVE_SWITHCH_FAULT;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("读取马达状态" + status);
		return status;
	}

	private int readMotorResetStatus(int motorIndex) {
		logger.info("读取马达重置状态,motorIndex " + motorIndex);
		int status = 0;
		int mark = ModbusUtil.MARK[motorIndex];
		int response = 666666;
		try {
			response = readModbusValue(ModbusUtil.RESET_ADDRESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		status = mark & response;
		logger.info("马达重置状态,response-status " + response + "-" + status);
		return status;
	}

	/////
	// PCB reset faulst
	public void resetMotor(int motorIndex) {
		int address = ModbusUtil.RESET_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = readModbusValue(address);
			int write_val = (~markVal) & read_res;
			writeModbusValue(address, write_val);
			logger.info("重置马达, Read " + address + " Response " + read_res);
			logger.info("重置马达, Write " + address + " Value " + write_val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// reset short circuit
	public void resetShortCircuitFault(int motorIndex) {
		int address = ModbusUtil.SHORT_CIRCUIT_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = readModbusValue(address);
			int write_val = (~markVal) & read_res;
			writeModbusValue(address, write_val);
			logger.warn("短路故障, Read " + address + " Response " + read_res);
			logger.warn("短路故障, Write " + address + " Value " + write_val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// reset short circuit
	public void resetOverLoadFault(int motorIndex) {
		int address = ModbusUtil.OVERLOAD_FAULT_ADDRESS;
		int markVal = ModbusUtil.MARK[motorIndex];
		try {
			int read_res = readModbusValue(address);
			int write_val = (~markVal) & read_res;
			writeModbusValue(address, write_val);
			logger.warn("过载故障, Read " + address + " Response " + read_res);
			logger.warn("过载故障, Write " + address + " Value " + write_val);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startRead() {
		isDone = false;
		Thread thread = new Thread(this);
		thread.start();
	}

	private void myWait(int millSec) {
		try {
			Thread.sleep(millSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}