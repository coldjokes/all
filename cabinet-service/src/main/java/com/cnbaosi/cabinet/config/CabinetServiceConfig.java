package com.cnbaosi.cabinet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description 柜子配置
 * 
 * @author guozhidong
 *
 */
@Component
@ConfigurationProperties(prefix = CabinetServiceConfig.PREFIX)
public class CabinetServiceConfig {
	public static final String PREFIX = "cabinet-service";
	
	private String projectLocation;
	private String filePath;
	private String tempPath;
	private String picturePath;
	private String blueprintPath;
	private String stockPath;
	
	@Value("${cabinet-service.mail.host}")
	private String mailHost; // 邮箱服务器地址
	@Value("${cabinet-service.mail.port}")
	private String mailPort; // 邮箱端口
	@Value("${cabinet-service.mail.protocol}")
	private String mailProtocol; // 邮箱协议
	@Value("${cabinet-service.mail.sender}")
	private String mailSender; // 邮箱发件箱地址
	@Value("${cabinet-service.mail.senderName}")
	private String mailSenderName; // 邮箱发件箱昵称
	@Value("${cabinet-service.mail.authcode}")
	private String mailAuthCode; // 邮箱授权码
	
	@Value("${cabinet-service.face.ver}")
	private String faceVer;
	@Value("${cabinet-service.face.sdkLibPath}")
	private String faceSdkLibPath; 
	@Value("${cabinet-service.face.appId}")
	private String faceAppId;
	@Value("${cabinet-service.face.sdkKey}")
	private String faceSdkKey;
	@Value("${cabinet-service.face.passRate}")
	private Integer facePassRate;
	
	public String getProjectLocation() {
		return projectLocation;
	}
	public void setProjectLocation(String projectLocation) {
		this.projectLocation = projectLocation;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getPicturePath() {
		return picturePath;
	}
	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}
	public String getBlueprintPath() {
		return blueprintPath;
	}
	public void setBlueprintPath(String blueprintPath) {
		this.blueprintPath = blueprintPath;
	}
	public String getTempPath() {
		return tempPath;
	}
	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}
	public String getMailHost() {
		return mailHost;
	}
	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}
	public String getMailPort() {
		return mailPort;
	}
	public void setMailPort(String mailPort) {
		this.mailPort = mailPort;
	}
	public String getMailProtocol() {
		return mailProtocol;
	}
	public void setMailProtocol(String mailProtocol) {
		this.mailProtocol = mailProtocol;
	}
	public String getMailSender() {
		return mailSender;
	}
	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}
	public String getMailAuthCode() {
		return mailAuthCode;
	}
	public void setMailAuthCode(String mailAuthCode) {
		this.mailAuthCode = mailAuthCode;
	}
	public String getStockPath() {
		return stockPath;
	}
	public void setStockPath(String stockPath) {
		this.stockPath = stockPath;
	}
	public String getMailSenderName() {
		return mailSenderName;
	}
	public void setMailSenderName(String mailSenderName) {
		this.mailSenderName = mailSenderName;
	}
	public String getFaceVer() {
		return faceVer;
	}
	public void setFaceVer(String faceVer) {
		this.faceVer = faceVer;
	}
	public String getFaceSdkLibPath() {
		return faceSdkLibPath;
	}
	public void setFaceSdkLibPath(String faceSdkLibPath) {
		this.faceSdkLibPath = faceSdkLibPath;
	}
	public String getFaceAppId() {
		return faceAppId;
	}
	public void setFaceAppId(String faceAppId) {
		this.faceAppId = faceAppId;
	}
	public String getFaceSdkKey() {
		return faceSdkKey;
	}
	public void setFaceSdkKey(String faceSdkKey) {
		this.faceSdkKey = faceSdkKey;
	}
	public Integer getFacePassRate() {
		return facePassRate;
	}
	public void setFacePassRate(Integer facePassRate) {
		this.facePassRate = facePassRate;
	}
	
}