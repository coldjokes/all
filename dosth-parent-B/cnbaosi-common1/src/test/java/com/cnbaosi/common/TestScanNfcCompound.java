package com.cnbaosi.common;

import com.cnbaosi.scanner.ScanNfcCompound;
import com.cnbaosi.scanner.enums.ScanNfcType;

/**
 * @description 测试扫描NFC二合一
 * @author guozhidong
 *
 */
public class TestScanNfcCompound {
	public static void main(String[] args) {
		ScanNfcCompound compound = new ScanNfcCompound(ScanNfcType.DWS, true) {
			@Override
			public void receiveScanResult(String result) {
				System.err.println("扫描>>>" + result);
			}
			
			@Override
			public void receiveNfcResult(String result) {
				System.err.println("NFC>>>" + result);
			}

			@Override
			public void timeout() {
				System.err.println("接收信息超时");
			}
		};
		try {
			int baudrate = 115200;
			compound.start("COM7", baudrate, 20);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}