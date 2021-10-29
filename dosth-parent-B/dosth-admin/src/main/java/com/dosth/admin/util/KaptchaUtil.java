package com.dosth.admin.util;

import com.dosth.admin.common.config.properties.DosthProperties;
import com.dosth.common.util.SpringContextHolder;

/**
 * 验证码工具类
 * 
 * @author guozhidong
 *
 */
public class KaptchaUtil {

	/**
	 * 获取验证码开关
	 * 
	 * @return
	 */
	public static Boolean getKaptchaOnOff() {
		return SpringContextHolder.getBean(DosthProperties.class).getKaptchaOpen();
	}
}