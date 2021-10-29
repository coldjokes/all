package com.dosth.util;

/**
 *  
 * 
 * @author Yifeng Wang  
 */
public class MathUtil {
	
	/**
	 * 十六进制字符串转成字符串  61 61 62 62 63 63 64 64 65 65 66 66 67 67 --> aabbccddeeffgg
	 */
	public static String hexStringToString(String hexString) {
		String[] hexStrings = hexString.split(" ");
		String[] strings = new String[hexString.length() / 3];
		int i = 0;
		for (String a : hexStrings) {
			// 将十六进制转化成十进制
			int valueTen2 = Integer.parseInt(a, 16);
			strings[i] = String.valueOf(valueTen2);
			i++;
		}
		return asciiToString(strings);
	}

	public static String hexStringToString1(String s) {
	    if (s == null || s.equals("")) {
	        return null;
	    }
	    s = s.replace(" ", "");
	    byte[] baKeyword = new byte[s.length() / 2];
	    for (int i = 0; i < baKeyword.length; i++) {
	        try {
	            baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    try {
	        s = new String(baKeyword, "UTF-8");
	        new String();
	    } catch (Exception e1) {
	        e1.printStackTrace();
	    }
	    return s;
	}
	
	public static String hexStr2Str(String hexStr) {
	    String str = "0123456789ABCDEF";
	    char[] hexs = hexStr.toCharArray();
	    byte[] bytes = new byte[hexStr.length() / 2];
	    int n;
	    for (int i = 0; i < bytes.length; i++) {
	        n = str.indexOf(hexs[2 * i]) * 16;
	        n += str.indexOf(hexs[2 * i + 1]);
	        bytes[i] = (byte) (n & 0xff);
	    }
	    return new String(bytes);
	}
	
	/**
	 * asc码转字符串 [97, 97, 98, 98, 99, 99, 100, 100, 101, 101, 102, 102, 103, 103] --> aabbccddeeffgg
	 */
	public static String asciiToString(String[] asciiArr) {// ASCII转字符串
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < asciiArr.length; i++) {
			builder.append((char) Integer.parseInt(asciiArr[i]));
		}
		return builder.toString();
	}

	/**
	 * 字节码转成十六进进制字符串 [85, -86, 36, 0, 0, 0, -37, 85] --> 55aa24000000db55
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		
		int length = src.length;
		if (src == null || length <= 0) {
			return null;
		}
		for (int i = 0; i < length; i ++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 十六进制字空格分隔符串转byte[]   55 AA 24 01 00 00 DA-->[85, -86, 36, 1, 0, 0, -38]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		hexString = hexString.toLowerCase();
		String[] hexStrings = hexString.split(" ");
		byte[] bytes = new byte[hexStrings.length];
		for (int i = 0; i < hexStrings.length; i++) {
			char[] hexChars = hexStrings[i].toCharArray();
			bytes[i] = (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
		}
		return bytes;
	}

	
	/**
	 *   求异或值
	 */
	public static String xor(String strHex_X, String strHex_Y) {
		// 将x、y转成二进制形式
		String anotherBinary = Integer.toBinaryString(Integer.valueOf(strHex_X, 16));
		String thisBinary = Integer.toBinaryString(Integer.valueOf(strHex_Y, 16));
		String result = "";
		// 判断是否为8位二进制，否则左补零
		if (anotherBinary.length() != 8) {
			for (int i = anotherBinary.length(); i < 8; i++) {
				anotherBinary = "0" + anotherBinary;
			}
		}
		if (thisBinary.length() != 8) {
			for (int i = thisBinary.length(); i < 8; i++) {
				thisBinary = "0" + thisBinary;
			}
		}
		// 异或运算
		for (int i = 0; i < anotherBinary.length(); i++) {
			// 如果相同位置数相同，则补0，否则补1
			if (thisBinary.charAt(i) == anotherBinary.charAt(i))
				result += "0";
			else {
				result += "1";
			}
		}
		return Integer.toHexString(Integer.parseInt(result, 2));
	}

	/**
	 *   byte转十六进制
	 */
	public static String byteToHexString(byte times) {
		return String.format("%02X", times);
	}
	
	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
	}
}

