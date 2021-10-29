package com.dosth.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.softhand.SoftHandCommMsg;

public abstract class Compoment {
	
	private static final Logger logger = LoggerFactory.getLogger(Compoment.class);

	protected Mediator mediator;

	public Compoment() {

	}

	protected Compoment(Mediator mediator) {
		this.mediator = mediator;
	}

	public synchronized void send(SoftHandCommMsg message) {
		logger.info("Send Msg:" + message.getMessage());
		mediator.send(message.getMessage(), this);
	}

	public synchronized void receive(SoftHandCommMsg message) {
		logger.info("Receive Msg:" + message.getMessage());
	}
}