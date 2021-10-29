package com.dosth.comm.plc;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleInputRegister;

public class PLCStatusReader implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(PLCStatusReader.class);

	protected ProcessPLCStatus processer;

	private boolean isDone;
	private String HOST = "192.168.2.46";
	private int PORT = 502;
	private int SLAVE_ID = 1; // here default

	private TCPMasterConnection connection;

	public PLCStatusReader() {

	}

	public PLCStatusReader(String host, int port, int slave_id, ProcessPLCStatus processer) {
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

	public void setProcesser(ProcessPLCStatus processer) {
		this.processer = processer;
	}

	public void run() {
		logger.info("PLCStatusReader Thread Running... ");
		isDone = false;
		if (isLiftArrive(PLCUtil.TIME_OUT)) {
			processer.onLiftArrive();
		} else {
			processer.onLiftNotArrive();
		}
		isDone = true;
		connection.close();
	}

	public boolean isDone() {
		return isDone;
	}

	/**
	 * @description 验证超时时间内是否到达
	 * @param timeout 超时时间
	 * @return
	 */
	private boolean isLiftArrive(int timeout) {
		boolean flag = false;
		long dur = timeout * 1000;
		long start = System.currentTimeMillis();
		long now = System.currentTimeMillis();
		while ((now - start) < dur) {
			try {
				flag = readCoilValue(PLCUtil.ON_ARRIVE_COIL);
			} catch (Exception e) {
				e.printStackTrace();
				flag = false; // exception
			}
			if (flag) {
				break; // Arrived
			}
			now = System.currentTimeMillis();
		}
		return flag;
	}

	public void startRead() {
		isDone = false;
		Thread thread = new Thread(this);
		long id = thread.getId();
		logger.info("PLCStatusReader Thread ID " + id + " running");
		thread.start();
	}

	/**
	 * @description 读取modbus地址值
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public int readModbusValue(int address) throws Exception {
		int result = -1;
		try {
			ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(address, 2);
			request.setUnitID(SLAVE_ID);
			ModbusResponse response = sendRequest(request);
			int low = ((ReadMultipleRegistersResponse) response).getRegisterValue(0);
			int high = ((ReadMultipleRegistersResponse) response).getRegisterValue(1);
			result = PLCUtil.geneInt(low, high);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @description 读取联系的线圈值
	 * @param address 地址
	 * @param count   地址数
	 * @return
	 * @throws Exception
	 */
	public boolean[] readCoilValue(int address, int count) throws Exception {
		boolean result[] = new boolean[count];
		try {
			ReadCoilsRequest request = new ReadCoilsRequest(address, count);
			request.setUnitID(SLAVE_ID);
			ModbusResponse response = sendRequest(request);
			for (int i = 0; i < count; i++) {
				result[i] = ((ReadCoilsResponse) response).getCoilStatus(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @description 读取线圈值
	 * @param address 地址
	 * @return
	 * @throws Exception
	 */
	public boolean readCoilValue(int address) throws Exception {
		boolean result = false;
		try {
			ReadCoilsRequest request = new ReadCoilsRequest(address, 1);
			request.setUnitID(SLAVE_ID);
			ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
			transaction.setRetries(5);
			transaction.setRequest(request);
			try {
				transaction.execute();
			} catch (ModbusException e) {
				throw new ModbusException(e.getMessage());
			}
			ModbusResponse response = transaction.getResponse();
			result = ((ReadCoilsResponse) response).getCoilStatus(0);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			connection.close();
		}
		return result;
	}

	/**
	 * @description 写入modbus地址值
	 * @param address 地址
	 * @param value   值
	 * @return
	 * @throws Exception
	 */
	public boolean writeModbusValue(int address, int value) throws Exception {
		boolean flag = false;
		try {
			ModbusRequest request = null;
			int[] valSep = PLCUtil.int4ToInt2(value);
			SimpleInputRegister[] values = { new SimpleInputRegister(valSep[0]), new SimpleInputRegister(valSep[1]) };
			request = new WriteMultipleRegistersRequest(address, values);
			request.setUnitID(SLAVE_ID);
			sendRequest(request);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 写入线圈值
	 * @param address 地址
	 * @param value   值
	 * @return
	 * @throws Exception
	 */
	public boolean writeCoilValue(int address, boolean value) throws Exception {
		boolean flag = false;
		try {
			ModbusRequest request = new WriteCoilRequest(address, value);
			request.setUnitID(SLAVE_ID);
			sendRequest(request);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * @description 发送modbus请求
	 * @param request modbus请求
	 * @return
	 * @throws ModbusException
	 * @throws UnknownHostException
	 */
	private ModbusResponse sendRequest(ModbusRequest request) throws ModbusException, UnknownHostException {
		InetAddress addr = InetAddress.getByName(HOST);
		TCPMasterConnection connection = new TCPMasterConnection(addr);
		connection.setPort(PORT);
		try {
			connection.connect();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
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