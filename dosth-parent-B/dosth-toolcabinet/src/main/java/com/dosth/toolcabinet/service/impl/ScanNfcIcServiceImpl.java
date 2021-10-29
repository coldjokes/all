package com.dosth.toolcabinet.service.impl;

import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cnbaosi.scanner.ScanNfcCompound;
import com.cnbaosi.scanner.enums.ScanNfcType;
import com.dosth.comm.NotASerialPort;
import com.dosth.comm.SerialPortParameterFailure;
import com.dosth.enums.SetupKey;
import com.dosth.toolcabinet.DosthToolcabinetRunnerInit;
import com.dosth.toolcabinet.commu.iccard.IcReader;
import com.dosth.toolcabinet.service.ScanNfcIcService;
import com.dosth.toolcabinet.service.ToolService;
import com.dosth.util.OpTip;
import com.dosth.websocket.constant.WsMsgType;
import com.dosth.websocket.dto.WsMsg;
import com.dosth.websocket.util.WebSocketUtil;

import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;

/**
 * @description 扫描NFC/IC设备接口实现
 * @author guozhidong
 *
 */
@Service
public class ScanNfcIcServiceImpl implements ScanNfcIcService {
	
	private static final Logger logger = LoggerFactory.getLogger(ScanNfcIcServiceImpl.class);

	@Autowired
	private ToolService toolService;
	@Autowired
	private IcReader icReader;

	@Override
	public void startScanNfcIc() {
		// 扫描NFC串口配置项不为空
		String scanNfcComm = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_COM);
		if (scanNfcComm != null && !"".equals(scanNfcComm)) {
			String scanNfcIcType = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_TYPE);
			if (scanNfcIcType.equals(ScanNfcType.IC.name())) {
				logger.info("IC读卡器启动");
				try {
					icReader.start();
				} catch (NoSuchPortException | PortInUseException | SerialPortParameterFailure | NotASerialPort
						| TooManyListenersException e) {
					e.printStackTrace();
					logger.error("IC启动失败:" + e.getMessage());
				}
			} else {
				logger.info("读卡扫描二合一启动");
				ScanNfcCompound compound = new ScanNfcCompound(ScanNfcType.valueOf(scanNfcIcType), true) {
					@Override
					public void receiveScanResult(String result) {
						String printComm = DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.PRINT_COM);
						if (printComm == null || "".equals(printComm)) {
							OpTip tip = toolService.findBybarcode(result);
							if (tip.getCode() != 202) {
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.PRINTED_CODE_INFO, result));
							} else {
								WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.TEXT, "此条码或二维码已被使用"));
							}
						}
					}
	
					@Override
					public void receiveNfcResult(String result) {
						WebSocketUtil.sendMsgSingle(DosthToolcabinetRunnerInit.mainCabinetId, new WsMsg(WsMsgType.ICSWIPING, result));
					}
	
					@Override
					public void timeout() {
					}
				};
				try {
					compound.start(scanNfcComm, Integer.valueOf(DosthToolcabinetRunnerInit.getCabinetParam(DosthToolcabinetRunnerInit.mainCabinetId, SetupKey.Public.SCAN_BAUD)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}