package com.dosth.comm.motorboard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MotorResponseUser implements ProcessMotorResponse {
	
	private static final Logger logger = LoggerFactory.getLogger(MotorResponseUser.class);

	public synchronized void processMotorResponse(int motorIndex, int motorResponse) {

		if (motorResponse == -1)
			logger.info(motorIndex + " Motor No response returned,exception or timeout occur");
		else {
			logger.info(motorIndex + " Motor Status Data " + motorResponse);
			String statusStr = ModbusUtil.parseStatusData(motorResponse);
			logger.info(motorIndex + " Motor Status Description " + statusStr);
		}
	}
}