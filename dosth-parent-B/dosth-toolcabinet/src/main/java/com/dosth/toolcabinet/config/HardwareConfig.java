package com.dosth.toolcabinet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dosth.comm.MotorCompoment;
import com.dosth.comm.PLCCompoment;
import com.dosth.comm.UICompoment;
import com.dosth.toolcabinet.commu.ConcreteMediator;

/**
 * @description 硬件配置
 * @Author guozhidong
 */
@Configuration
public class HardwareConfig {

	@Autowired
	private ConcreteMediator concreteMediator;

	@Value("${spring.cloud.client.ipAddress}")
	private String ipAddress;

//	@Bean
//	public PrinterUtil printUtil() {
//		return new PrinterUtil(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.PRINT_COM));
//	}

	@Bean
	public UICompoment uiCompoment() {
		UICompoment uiCom = new UICompoment(concreteMediator);
		concreteMediator.setUICompoment(uiCom);
		return uiCom;
	}

	@Bean
	public PLCCompoment plcCompoment() {
		PLCCompoment plcCom = new PLCCompoment(concreteMediator);
		concreteMediator.setPLCCompoment(plcCom);
		return plcCom;
	}

	@Bean
	public MotorCompoment motorCompoment() {
		MotorCompoment motorCom = new MotorCompoment(concreteMediator);
		concreteMediator.setMotorCompoment(motorCom);
		return motorCom;
	}

//	@Bean
//	public ReturnScanCompoment returnScanCompoment() {
//		ReturnScanCompoment returnScanCompoment = new ReturnScanCompoment(concreteMediator, cabinetConfig.getScanComm());
//		concreteMediator.setReturnScanCompoment(returnScanCompoment);
//		return returnScanCompoment;
//	}

//	@Bean
//	public AppointmentScanCompoment appointmentScanCompoment() {
//		AppointmentScanCompoment appointmentScanCompoment = new AppointmentScanCompoment(concreteMediator,
//				this.cabinetConfig.getAppScanComm(), ipAddress);
//		concreteMediator.setAppScanCompoment(appointmentScanCompoment);
//		return appointmentScanCompoment;
//	}

//	@Bean
//	public SerialScanCompoment serialScanCompoment() {
//		SerialScanCompoment serialScanComponent = new SerialScanCompoment(concreteMediator, 
//				DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_COM),
//				DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_BAUD) + "-8-N-1",
//				DosthToolcabinetRunnerInit.mainCabinetId, ipAddress);
//		
//		SerialScanCompoment serialScanComponent = new SerialScanCompoment(concreteMediator, 
//				"COM33",
//				"115200-8-N-1",
//				DosthToolcabinetRunnerInit.mainCabinetId, ipAddress);
//		concreteMediator.setSerialScanCom(serialScanComponent);
//		return serialScanComponent;
//	}
}