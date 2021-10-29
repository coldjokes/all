package com.dosth.admin.util.face;

import java.io.File;

import org.opencv.core.Core;

import com.dosth.common.util.AESUtil;

/**
 * 人脸识别密码工具类
 * 
 * @author guozhidong
 *
 */
public class FaceVerificationUtil {

	/**
	 * 脸谱识别码加密
	 * 
	 * @param password
	 *            原始密码
	 * @param salt
	 *            加密盐
	 * @return 加密密码
	 */
	public static String faceEncode(String password, String salt) {
		return AESUtil.aesEncode(password + salt);
	}

	/**
	 * 脸谱识别码解密
	 * 
	 * @param enPassword
	 *            加密密码
	 * @param salt
	 *            加密盐
	 * @return 原始密码
	 */
	public static String faceDecode(String enPassword, String salt) {
		String before = AESUtil.aesDecode(enPassword);
		return before.substring(0, before.length() - salt.length());
	}

	/**
	 * 验证人员脸谱是被账号
	 * 
	 * @param path
	 * @param facePath
	 * @return 匹配度大于20的账号序号
	 */
	public static String compare(String path, String facePath) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String accountId = null;
		File[] faces = new File(facePath).listFiles();
		for (File face : faces) {
			if (FeatureMatchingUtil.doMaping(path, face.getAbsolutePath()) > 10) {
				accountId = face.getAbsolutePath();
			}
		}
		if (accountId == null) {
			throw new RuntimeException();
		}
		return accountId.substring(accountId.lastIndexOf(File.separator) + 1, accountId.lastIndexOf("."));
	}
}