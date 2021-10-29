package com.dosth.tool.common.config;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description 工具配置系统配置
 * 
 * @author guozhidong
 *
 */
@Component
@ConfigurationProperties(prefix = ToolProperties.PREFIX)
public class ToolProperties {
	public static final String PREFIX = "tool";

	/**
	 * @description 柜子版本号
	 */
	private String ver;
	/**
	 * @description 应用端口
	 */
	@Value("${server.port}")
	private String port = "8080";

	/**
	 * @description ip
	 */
	@Value("${spring.cloud.client.ipAddress}")
	private String ip;

	/**
	 * @description 上传临时路径
	 */
	private String tmpUploadPath;
	/**
	 * @description 上传实际路径
	 */
	private String uploadPath;

	/**
	 * @description excel上传时图片文件路径
	 */
	private String iconsPath;

	/**
	 * @description netty服务端Host
	 */
	private String nettyServerHost;
	/**
	 * @description netty服务端Port
	 */
	private int nettyServerPort;
	/**
	 * @description 请求外部接口地址
	 */
	private String externalServerHost;
	/**
	 * @description 邮箱服务器
	 */
	private String mailHost;
	/**
	 * @description 协议
	 */
	private String mailProtocol;
	/**
	 * @description 发送人
	 */
	private String mailSender;
	/**
	 * @description 发送邮箱
	 */
	private String mailAddr;
	/**
	 * @description 邮件授权码
	 */
	private String mailAuthorCode;
	/**
	 * @description 领料单请求路径
	 */
	private String applyVoucherUrl;
	/**
	 * @description 获取入库单
	 */
	private String warehouseFeedUrl;
	/**
	 * @description 领用推送
	 */
	private String borrowPostUrl;

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getTmpUploadPath() {
		File file = new File(this.tmpUploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return this.tmpUploadPath;
	}

	public String getUploadPath() {
		File file = new File(this.uploadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		return this.uploadPath;
	}

	public String getNettyServerHost() {
		return this.nettyServerHost;
	}

	public void setNettyServerHost(String nettyServerHost) {
		this.nettyServerHost = nettyServerHost;
	}

	public int getNettyServerPort() {
		return this.nettyServerPort;
	}

	public void setNettyServerPort(int nettyServerPort) {
		this.nettyServerPort = nettyServerPort;
	}

	public String getExternalServerHost() {
		return externalServerHost;
	}

	public void setExternalServerHost(String externalServerHost) {
		this.externalServerHost = externalServerHost;
	}

	public void setTmpUploadPath(String tmpUploadPath) {
		this.tmpUploadPath = tmpUploadPath;
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	public String getMailHost() {
		return this.mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getMailProtocol() {
		return this.mailProtocol;
	}

	public void setMailProtocol(String mailProtocol) {
		this.mailProtocol = mailProtocol;
	}

	public String getMailSender() {
		return this.mailSender;
	}

	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}

	public String getMailAddr() {
		return this.mailAddr;
	}

	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}

	public String getMailAuthorCode() {
		return this.mailAuthorCode;
	}

	public void setMailAuthorCode(String mailAuthorCode) {
		this.mailAuthorCode = mailAuthorCode;
	}

	public String getIconsPath() {
		return iconsPath;
	}

	public void setIconsPath(String iconsPath) {
		this.iconsPath = iconsPath;
	}

	/**
	 * @description 获取图片网络路径
	 * @return
	 */
	public String getImgUrlPath() {
		return "http://" + this.ip + ":" + this.port + "/" + ToolProperties.PREFIX + "/";
	}

	public String getApplyVoucherUrl() {
		return this.applyVoucherUrl;
	}

	public void setApplyVoucherUrl(String applyVoucherUrl) {
		this.applyVoucherUrl = applyVoucherUrl;
	}

	public String getWarehouseFeedUrl() {
		return warehouseFeedUrl;
	}

	public void setWarehouseFeedUrl(String warehouseFeedUrl) {
		this.warehouseFeedUrl = warehouseFeedUrl;
	}

	public String getBorrowPostUrl() {
		return borrowPostUrl;
	}

	public void setBorrowPostUrl(String borrowPostUrl) {
		this.borrowPostUrl = borrowPostUrl;
	}
	
}