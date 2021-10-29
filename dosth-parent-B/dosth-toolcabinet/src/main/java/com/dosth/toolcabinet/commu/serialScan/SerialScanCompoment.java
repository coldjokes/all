package com.dosth.toolcabinet.commu.serialScan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.dosth.comm.Compoment;
import com.dosth.comm.Mediator;
import com.dosth.constant.EnumDoor;
import com.dosth.toolcabinet.commu.ConcreteMediator;
import com.dosth.toolcabinet.commu.scaner.Scaner;
import com.dosth.toolcabinet.service.ToolService;

public class SerialScanCompoment extends Compoment {

	public static final Logger logger = LoggerFactory.getLogger(SerialScanCompoment.class);
	private SerialScanResponseUser scanUser;
	private Scaner scanObj;
	
	@Autowired
	private ToolService toolService;
	
	public SerialScanCompoment() {
		scanUser = new SerialScanResponseUser(this);
	}
	
	public SerialScanCompoment(Mediator mediator, String comPort, String comParams, String cabinetId, String ip) {
		super(mediator);
		scanObj = new Scaner();
		scanUser = new SerialScanResponseUser(this, scanObj, cabinetId, ip);
		if(scanObj.openCOMPort(comPort, comParams)){
			scanObj.setUser(scanUser);
			scanObj.startDecode();
		} else {
			logger.error("开启串口失败");
		}
	}
	
	/**
	 * @description 柜子是否有人在使用（料斗，马达弹簧在使用中）
	 * @return true 没有用户使用，ready状态
	 */
	public synchronized boolean isNonReturning() {
		return ((ConcreteMediator)mediator).isCurrShoppingCartTakenOut();
	}
	
	/**
	 * @description 预约是否取出
	 * @param appointmentID 预约ID
	 * @return 是否取出
	 */
	public synchronized boolean isAppointmentCompletedByID(String appointmentID) {
		boolean isCompleted = false;
		isCompleted = toolService.isAppointmentCompletedByID(appointmentID);
		return isCompleted;
	}
	
	/**
	 * @description 父柜ID
	 * @param 
	 * @return 
	 */
	public String getCabinetId(String cabinetId) {
		return toolService.getCabinetId(cabinetId);
	}
	
	/**
	 * @description APP左右门获取
	 * @param 
	 * @return 
	 */
	public EnumDoor getDoor(String ip) {
		return toolService.getDoor(ip);
	}
}