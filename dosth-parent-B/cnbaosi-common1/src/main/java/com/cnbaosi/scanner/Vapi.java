package com.cnbaosi.scanner;

import java.io.UnsupportedEncodingException;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

/**
 * @description 微光互联接口协议
 * @author guozhidong
 *
 */
public class Vapi {

	public interface Vdll extends Library {
		Vdll INSTANCE = (Vdll) Native.loadLibrary("vbar", Vdll.class);

		// 打开设备
		public IntByReference vbar_connectDevice(String devnum);

		// 设置串口参数
		public int vbar_channel_config(IntByReference vbar_device, String arg);

		// 背光控制
		public int vbar_backlight(IntByReference vbar_device, boolean on);

		// 添加码制
		public int vbar_addCodeFormat(IntByReference vbar_device, byte codeformat);

		// 蜂鸣器控制
		public int vbar_beepControl(IntByReference vbar_device, byte times);

		// 扫码
		public int vbar_getResultStr(IntByReference vbar_device, byte[] result_buffer, IntByReference result_size,
				IntByReference result_type);

		// 设置间隔时间
		public int vbar_interval(IntByReference vbar_device, int time);

		// 断开连接
		public void vbar_disconnectDevice(IntByReference vbar_device);
	}

	// 初始化设备变量
	IntByReference device = null;

	IntByReference result_size = new IntByReference(1024);
	IntByReference result_type = new IntByReference(1024);

	// 断开设备
	public void vbarClose() {
		Vdll.INSTANCE.vbar_disconnectDevice(device);
		device = null;
	}

	// 打开设备
	public boolean vbarOpen(String devnum) {
		if (device == null) {
			device = Vdll.INSTANCE.vbar_connectDevice(devnum);
		}
		return device != null;
	}

	// 设置串口参数
	public boolean vbarSetserial(String port) {
		return Vdll.INSTANCE.vbar_channel_config(device, port) == 0;
	}

	// 蜂鸣器控制
	public boolean vbarBeep(byte times) {
		return Vdll.INSTANCE.vbar_beepControl(device, times) == 0;
	}

	// 背光控制
	public boolean vbarBacklight(boolean bool) {
		return Vdll.INSTANCE.vbar_backlight(device, bool) == 0;
	}

	// 添加要支持的码制
	public boolean vbarAddSymbolType(byte symbol_type) {
		return Vdll.INSTANCE.vbar_addCodeFormat(device, symbol_type) == 0;
	}

	// 设置间隔时间
	public Boolean vbarInterval(int times) {
		return Vdll.INSTANCE.vbar_interval(device, times) == 0;
	}

	// 扫码
	public String vbarScan() throws UnsupportedEncodingException {
		byte[] result_buffer = new byte[1024];
		String decode = null;
		if (Vdll.INSTANCE.vbar_getResultStr(device, result_buffer, result_size, result_type) == 0) {
			decode = new String(result_buffer, "UTF-8");
			result_buffer = null;
			return decode;
		} else {
			return null;
		}
	}
}