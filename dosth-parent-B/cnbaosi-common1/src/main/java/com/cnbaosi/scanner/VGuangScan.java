package com.cnbaosi.scanner;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description 微光互联扫描仪
 * @author guozhidong
 *
 */
public abstract class VGuangScan {

	private static Logger logger = LoggerFactory.getLogger(VGuangScan.class);
	
	Vapi v = new Vapi();
	
	// 超时
    private int timeout = 60;
	
	/**
	 * @description 打开扫描仪串口
	 * @param scanComm 扫描仪串口
	 */
	public void open(String scanComm) {
		logger.info("打开扫描仪串口:" + scanComm);
		boolean  flag = v.vbarOpen(scanComm);
		if (flag) {
			logger.info("打开扫描仪串口成功");
		} else {
			logger.error("打开扫描仪串口失败");
		}
	}

	/**
	 * @description 接收扫描结果
	 * @param result 扫描结果
	 */
	public abstract void receiveScanResult(String result);
	
	/**
	 * @description 未扫描到信息回调方法
	 */
	public abstract void unReceiveScan();
	
	/**
     * @description 启动扫描仪
     * @param params 超时参数 params[0] 超时时间,默认60秒
     */
	public void start(int... params) {
		if (params != null && params.length > 0) {
	        this.timeout = params[0];
	    }
		logger.info("启动方法,开灯");
		v.vbarBacklight(true);
        long start = System.currentTimeMillis();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					String decode = null;
					try {
						decode = v.vbarScan();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// 超时直接关闭串口
	                if (System.currentTimeMillis() - start > timeout * 1000) {
	                    logger.info("超时关闭串口");
						v.vbarBacklight(false);
						unReceiveScan();
	                    break;
	                }
					if (decode != null) {
						v.vbarBeep((byte) 1); // 调用解码响一声
						v.vbarBacklight(false);
						receiveScanResult(decode.trim()); 
						break;
					} else {
						logger.info("没有扫描到任何东西");
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}
	
	public void close() {
		if (v != null) {
			v.vbarClose();
		}
	}
}