package com.dosth.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 
 * @description 网络工具类
 * @author guozhidong
 *
 */
public class NetUtil {
	/**
	 * 
	 * @description 获取本地网络地址
	 * @return
	 * @throws Exception
	 */
	private static InetAddress getLocalHostLANAddress() throws Exception {
		try {
			InetAddress candidateAddress = null;
			Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
			Enumeration<InetAddress> inetAddrs = null;
			InetAddress inetAddr = null;
			// 遍历所有的网络接口
			while (ifaces.hasMoreElements()) {
				inetAddrs = ifaces.nextElement().getInetAddresses();
				// 在所有的接口下再遍历IP
				while (inetAddrs.hasMoreElements()) {
					inetAddr = inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr;
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			return jdkSuppliedAddress;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @description 获取本地Ip地址
	 * @return
	 */
	public static String getLocalIp() {
		try {
			return getLocalHostLANAddress().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}