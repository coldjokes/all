package com.dosth.admin.common.config.properties;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.util.ToolUtil;

/**
 * 系统管理
 * 
 * @author guozhidong
 *
 */
@Component
public class DosthProperties {
	
	@Value("${dosth.logo}")
	private String logo;
	@Value("${dosth.dataCenterId}")
	private String dataCenterId;
	// 是否开启登录时验证码 true/false, 默认false
	@Value("${dosth.kaptcha-open:false}")
	private Boolean kaptchaOpen;
	// 上传临时文件路径
	@Value("${dosth.file-tmp-path}")
	private String fileTmpPath;
	// 脸谱存储路径
	@Value("${dosth.sso.facePath}")
	private String facePath;

	Integer sessionInvalidateTime = 30 * 60; // session 失效时间（默认为30分钟 单位：秒）

	Integer sessionValidationInterval = 15 * 60; // session 验证失效时间（默认为15分钟 单位：秒）

	public String getFileTmpPath() {
		// 如果没有写文件上传路径,保存到临时目录
		if (ToolUtil.isEmpty(fileTmpPath)) {
			return ToolUtil.getTempPath();
		} else {
			File file = new File(fileTmpPath);
			// 判断目录存不存在,不存在得加上
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		return this.procFilePath(this.fileTmpPath);
	}

	public String getFacePath() {
		if (this.facePath == null) {
			throw new DoSthException(DoSthExceptionEnum.FILE_NOT_FOUND);
		}
		File file = new File(this.facePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return this.procFilePath(this.facePath);
	}

	public String getLogo() {
		if (this.logo == null || "".equals(this.logo)) {
			this.logo = "bsc";
		}
		return this.logo;
	}

	public String getDataCenterId() {
		return this.dataCenterId;
	}

	public Boolean getKaptchaOpen() {
		return this.kaptchaOpen;
	}

	public Integer getSessionInvalidateTime() {
		return this.sessionInvalidateTime;
	}

	public Integer getSessionValidationInterval() {
		return this.sessionValidationInterval;
	}

	/**
	 * 处理文件路径
	 * 
	 * @param filePath
	 * @return
	 */
	private String procFilePath(String filePath) {
		if (filePath.endsWith(File.separator)) {
			return filePath;
		}
		return filePath + File.separator;
	}
}