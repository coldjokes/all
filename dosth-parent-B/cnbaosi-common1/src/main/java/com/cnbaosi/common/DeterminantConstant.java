package com.cnbaosi.common;

/**
 * @description 行列式常量
 * @author guozhidong
 *
 */
public class DeterminantConstant {
	/**
	 * @description 指令起始符
	 */
	public static final byte[] START = { 0x57, 0x4B, 0x4C, 0x59 };

	/**
	 * @description 可控抽屉命令起始符
	 */
	public static final byte[] TRO_START = { 0x42, 0x53, 0x43 };
	/**
	 * @description 成功标识
	 */
	public static final byte[] OK = { 0x4F, 0x4B };
	
	/**
	 * @description 料斗位置偏移
	 */
	public static final byte DEVIATION = (byte) 0xFF;
	
	/**
	 * @description BUSY码
	 */
	public static final byte[] BUSY = {0x42, 0x55, 0x53, 0x59};
}