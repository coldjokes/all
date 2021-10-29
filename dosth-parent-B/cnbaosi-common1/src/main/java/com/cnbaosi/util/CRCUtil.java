package com.cnbaosi.util;

/*
* 校验码：CRC16占用两个字节，包含了一个 16 位的二进制值。CRC 值由传输设备计算出来，
* 然后附加到数据帧上，接收设备在接收数据时重新计算 CRC 值，然后与接收到的 CRC 域中的值
* 进行比较，如果这两个值不相等，就发生了错误。
* 生成一个 CRC16 的流程为：
* (1) 预置一个 16 位寄存器为 0FFFFH（全为1），称之为 CRC 寄存器。
* (2) 把数据帧中的第一个字节的 8 位与 CRC 寄存器中的低字节进行异或运算，结果存回 CRC 寄存器。
* (3) 将 CRC 寄存器向右移一位，最高位填以 0，最低位移出并检测。
* (4) 如果最低位为 0：重复第三步（下一次移位）；如果最低位为 1，将 CRC 寄存器与一个预设的固定值 0A001H 进行异或运算。
* (5) 重复第三步和第四步直到 8 次移位。这样处理完了一个完整的八位。
* (6) 重复第 2 步到第 5 步来处理下一个八位，直到所有的字节处理结束。
* (7) 最终 CRC 寄存器的值就是 CRC16 的值。
*/
public class CRCUtil {
	/**
	 * 一个字节包含位的数量 8
	 */
	private static final int BITS_OF_BYTE = 8;

	/**
	 * 多项式
	 */
	private static final int POLYNOMIAL = 0xA001;

	/**
	 * 初始值
	 */
	private static final int INITIAL_VALUE = 0xFFFF;

	/**
	 * CRC16 编码
	 *
	 * @param bytes 编码内容
	 * @return 编码结果
	 */
	private static String crc16(byte[] bytes) {
		int res = INITIAL_VALUE;
		for (byte data : bytes) {
			res = res ^ data;
			for (int i = 0; i < BITS_OF_BYTE; i++) {
				res = (res & 0x0001) == 1 ? (res >> 1) ^ POLYNOMIAL : res >> 1;
			}
		}
		return convertToHexString(revert(res));
	}

	/**
	 * 翻转16位的高八位和低八位字节
	 *
	 * @param src 翻转数字
	 * @return 翻转结果
	 */
	private static int revert(int src) {
		int lowByte = (src & 0xFF00) >> 8;
		int highByte = (src & 0x00FF) << 8;
		return lowByte | highByte;
	}

	/**
	 * @description 16进制转字符串
	 * @param src
	 * @return
	 */
	private static String convertToHexString(int src) {
		return Integer.toHexString(src);
	}

	/**
	 * 将16进制字符串转换为byte[]
	 *
	 * @param str 16进制字符串
	 * @return
	 */
	private static byte[] toBytes(String str) {
		if (str == null || str.trim().equals("")) {
			return new byte[0];
		}

		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
		}

		return bytes;
	}

	/**
	 * @description 创建发送数据
	 * @param data 待发送数据
	 * @return 添加校验码的数据
	 */
	public static byte[] createSendData(byte[] data) {
		byte[] bytes = toBytes(CRCUtil.crc16(data));
		byte[] result = new byte[data.length + bytes.length];
		int i = 0;
		for (int j = 0; j < data.length; j++) {
			result[i] = data[j];
			i++;
		}
		for (int j = 0; j < bytes.length; j++) {
			result[i] = bytes[j];
			i++;
		}
		return result;
	}
}