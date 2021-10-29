package com.dosth.toolcabinet.commu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.comm.Compoment;
import com.dosth.comm.Mediator;
import com.dosth.toolcabinet.commu.scaner.AppointmentScanerResponseUser;
import com.dosth.toolcabinet.commu.scaner.Scaner;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.OpTip;

public class AppointmentScanCompoment extends Compoment {

	public static final Logger logger = LoggerFactory.getLogger(AppointmentScanCompoment.class);
	private ToolService toolService;
	private Scaner scanObj;
	private AppointmentScanerResponseUser scanUser;
	private String AppScanCOM = "";
	private boolean isConnCOMSucc = true;
	private volatile boolean isOneScanDone = true;
	private volatile int appointmentCount = 0;
	
	public AppointmentScanCompoment() {
		scanObj = new Scaner();
		scanUser = new AppointmentScanerResponseUser(this,scanObj, null);
		
		try {
			scanObj.openCOMPort(AppScanCOM);
		} catch (Exception e) {
			isConnCOMSucc = false;
		}
		if (isConnCOMSucc) {
			scanObj.onLight();
		}
		scanObj.setUser(scanUser);
	}
	
	/**
	 * @description set ToolService对象供后面使用
	 */
	public synchronized void setToolService(ToolService toolService) {
		this.toolService = toolService;
	}
	
	public AppointmentScanCompoment(Mediator mediator, String AppScanCOM, String ip) {
		super(mediator);
		this.AppScanCOM = AppScanCOM;
		scanObj = new Scaner();
		scanUser = new AppointmentScanerResponseUser(this, scanObj, ip);
		scanObj.openCOMPort(AppScanCOM);
		scanObj.setUser(scanUser);
	}
	
	/**
	 * @description 预约是否取出
	 * @param appointmentID 预约ID
	 * @return 是否取出
	 */
	public synchronized boolean isAppointmentCompletedByID(String appointmentID) {
		boolean isCompleted = false;
		toolService.isAppointmentCompletedByID(appointmentID);
		return isCompleted;
	}
	
	/**
	 * @description 设置预约是否完成
	 * @param cabinetID 柜子ID
	 * @param isCompleted true 完成； false 未完成
	 */
	public synchronized OpTip setAppointmentCompletedByID(String appointmentID, boolean isCompleted) {
		OpTip opTip = toolService.setAppointmentCompletedByID(appointmentID, isCompleted);
		return opTip;
	}
	
	/**
	 * @description 启动预约扫描仪，开始解码（等待用户扫描），同时记录这台柜子的预约数目
	 */
	public synchronized void startMonitorScan() {
		appointmentCount++;
		isOneScanDone = false;
		scanObj.startDecode();
	}
	
	/**
	 * @description 结束预约扫描仪，同时记录这台柜子的预约数目减去1
	 */
	public synchronized void stopMonitorScan() {
		appointmentCount--;
		scanObj.offLight();
		scanObj.stopDecode();
	}
	
	/**
	 * @description 判断一份预约扫码是否完成
	 */
	public synchronized boolean isOnceScanDone() {
		logger.warn("Roc Enter AppointmentScanCompoment.isOnceScanDone(),isOneScanDone:"+isOneScanDone);
		return isOneScanDone;
	} 
	
	/**
	 * @description 设置一份预约扫码完成
	 */
	public synchronized void setOnceScanDoneFlag() {
		isOneScanDone = true;
		if (appointmentCount > 0) {
			scanObj.onLight();
			scanObj.startDecode();
		}
	} 
	
	public void connCOM(String COM) {
		disconnCOM();
		scanObj.openCOMPort(AppScanCOM);
	}
	
	public void disconnCOM() {
		scanObj.closeCOMPort();
	}
	
	/**
	 * @description set scaner status,keeping monitoring or just once(outer command make scanner ready for user scan)
	 * @param isKeepMonitoringMode mode flag
	 */
	public synchronized void setMonitoringMode(boolean isKeepMonitoringMode) {
		scanObj.setMonitoringMode(isKeepMonitoringMode);
	}
	
	/**
	 * @description set current cabinet ID
	 * @param accountID account ID
	 */
	public synchronized void setCabinetID(String cabinetID) {
		scanUser.setCabinetIDInner(cabinetID);
	}
	
	/**
	 * @description 柜子是否有人在使用（料斗，马达弹簧在使用中）
	 * @return true 没有用户使用，ready状态
	 */
	public synchronized boolean isNonReturning() {
		boolean flag = ((ConcreteMediator)mediator).isCurrShoppingCartTakenOut();
		return flag;
	}
}