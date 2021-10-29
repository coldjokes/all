package com.dosth.ic;

import java.util.List;

import javax.usb.UsbConfiguration;
import javax.usb.UsbConst;
import javax.usb.UsbControlIrp;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbPipe;
import javax.usb.util.UsbUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dosth.instruction.board.Board;


@SuppressWarnings({ "unchecked", "unused" })
public class UsbICReader {
	private static final Logger logger = LoggerFactory.getLogger(UsbICReader.class);
	
	// VENDOR_ID
	private static final short VENDOR_ID = (short) 0xFFFF;
	// PRODUCT_ID
	private static final short PRODUCT_ID = 0x0035;
	private static final byte INTERFACE_AD = 0x00;
	// 输入地址位
	private static final byte ENDPOINT_OUT = 0x02;
	// 输出地址位
	private static final byte ENDPOINT_IN = (byte) 0x81;
	private static final byte[] COMMAND = { 0x01, 0x00 };

	public static UsbDevice findMissileLauncher(UsbHub hub) {
		UsbDevice launcher = null;

		for (UsbDevice device : (List<UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.isUsbHub()) {
				launcher = findMissileLauncher((UsbHub) device);
				if (launcher != null)
					return launcher;
			} else {
				UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
				if (desc.idVendor() == VENDOR_ID && desc.idProduct() == PRODUCT_ID) {
					logger.info("发现设备" + device);
					return device;
				}
			}
		}
		return null;
	}

	// command for controlTransfer
	public static void sendMessage(UsbDevice device, byte[] message) throws UsbException {
		UsbControlIrp irp = device.createUsbControlIrp(
				(byte) (UsbConst.REQUESTTYPE_TYPE_CLASS | UsbConst.REQUESTTYPE_RECIPIENT_INTERFACE), (byte) 0x09,
				(short) 2, (short) 1);
		irp.setData(message);
		device.syncSubmit(irp);
	}

	/**
	 * Class to listen in a dedicated Thread for data coming events. This really
	 * could be used for any HID device.
	 */
	public static class HidMouseRunnable implements Runnable {
		/*
		 * This pipe must be the HID interface's interrupt-type in-direction endpoint's
		 * pipe.
		 */
		public HidMouseRunnable(UsbPipe pipe) {
			usbPipe = pipe;
		}

		public void run() {
			byte[] buffer = new byte[UsbUtil
					.unsignedInt(usbPipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize())];

			while (running) {
				try {
					usbPipe.syncSubmit(buffer);
				} catch (UsbException uE) {
					if (running) {
						logger.info("Unable to submit data buffer to HID mouse : " + uE.getMessage());
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						};
					}
				}
				if (running) {
					try {
						if (buffer.length == 8 && buffer[0] == 0x00 && buffer[1] == 0x00 && buffer[2] == 0x00 
								&& buffer[3] == 0x00 && buffer[4] == 0x00 
								&& buffer[5] == 0x00 && buffer[6] == 0x00 && buffer[7] == 0x00) {
							logger.info("未捕获到刷卡信息");
							Thread.sleep(500);
							continue;
						}
						logger.info(buffer.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		/**
		 * Stop/abort listening for data events.
		 */
		public void stop() {
			running = false;
			usbPipe.abortAllSubmissions();
		}

		public boolean running = true;
		public UsbPipe usbPipe = null;
	}

	/**
	 * get the correct Interface for USB
	 * 
	 * @param device
	 * @return
	 * @throws UsbException
	 */
	public static UsbInterface readInit() throws UsbException {
		UsbDevice device = findMissileLauncher(UsbHostManager.getUsbServices().getRootUsbHub());
		if (device == null) {
			logger.error("Missile launcher not found.");
			System.exit(1);
		}
		UsbConfiguration configuration = device.getActiveUsbConfiguration();
		UsbInterface iface = configuration.getUsbInterface(INTERFACE_AD);// Interface Alternate Number
		iface.claim(new UsbInterfacePolicy() {
			@Override
			public boolean forceClaim(UsbInterface arg0) {
				return true;
			}
		});
		return iface;
	}

	/**
	 * 异步bulk传输,by tong
	 * 
	 * @param usbInterface
	 * @param data
	 */
	public static void syncSend(UsbInterface usbInterface, byte[] data) {
		UsbEndpoint endpoint = usbInterface.getUsbEndpoint(ENDPOINT_OUT);
		UsbPipe outPipe = endpoint.getUsbPipe();
		try {
			outPipe.open();
			outPipe.syncSubmit(data);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				outPipe.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @description 监听数据
	 * @param usbInterface
	 * @return
	 */
	public static HidMouseRunnable listenData(UsbInterface usbInterface) {
		UsbEndpoint endpoint = usbInterface.getUsbEndpoint(ENDPOINT_IN);
		UsbPipe inPipe = endpoint.getUsbPipe();
		HidMouseRunnable hmR = null;
		try {
			inPipe.open();
			hmR = new HidMouseRunnable(inPipe);
			Thread t = new Thread(hmR);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hmR;
	}
	
	/**
	 * @description 启动
	 */
	public static void start() {
		try {
			logger.info("USB IC读卡器监听启动");
			listenData(readInit());
		} catch (UsbException e) {
			logger.error("USB IC读卡器监听失败" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 主程序入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		start();
	}
}