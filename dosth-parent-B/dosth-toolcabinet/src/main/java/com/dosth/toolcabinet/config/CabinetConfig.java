package com.dosth.toolcabinet.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.dosth.toolcabinet.dto.AgenInfo;

/**
 * @description 柜子配置
 * 
 * @author guozhidong
 *
 */
@Component
@ConfigurationProperties(prefix = CabinetConfig.PREFIX)
public class CabinetConfig {

	public static final String PREFIX = "cabinet";

	/**
	 * @description 刀具柜序列号
	 */
	private String serialNo;

	/**
	 * @description 软件版本号
	 */
	private String version;

	/**
	 * @description logo标识
	 */
	private String logo;
	/**
	 * @description netty服务端Host
	 */
	private String nettyServerHost;
	/**
	 * @description netty服务端Port
	 */
	private int nettyServerPort;
	/**
	 * @description tool服务端Port
	 */
	private int toolServerPort;
	/**
	 * 音频路径
	 */
	private String audioPath;
	/**
	 * @description 申请单远程路径
	 */
	private String applyVoucherUrl;
	/**
	 * @description 领料单领取结果
	 */
	private String applyVoucherResultUrl;
	/**
	 * @description 同步补料单
	 */
	private String syncFeedingListUrl;
	/**
	 * @description 确认入库单
	 */
	private String warehouseResultUrl;

	private Map<String, AgenInfo> agenMap;

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

	public int getToolServerPort() {
		return toolServerPort;
	}

	public void setToolServerPort(int toolServerPort) {
		this.toolServerPort = toolServerPort;
	}

	public String getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLogo() {
		if (this.logo == null || "".equals(this.logo)) {
			this.logo = "bsc";
		}
		return this.logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getApplyVoucherUrl() {
		return this.applyVoucherUrl;
	}

	public void setApplyVoucherUrl(String applyVoucherUrl) {
		this.applyVoucherUrl = applyVoucherUrl;
	}

	public String getApplyVoucherResultUrl() {
		return this.applyVoucherResultUrl;
	}

	public void setApplyVoucherResultUrl(String applyVoucherResultUrl) {
		this.applyVoucherResultUrl = applyVoucherResultUrl;
	}

	public String getSyncFeedingListUrl() {
		return this.syncFeedingListUrl;
	}

	public void setSyncFeedingListUrl(String syncFeedingListUrl) {
		this.syncFeedingListUrl = syncFeedingListUrl;
	}

	public String getWarehouseResultUrl() {
		return warehouseResultUrl;
	}

	public void setWarehouseResultUrl(String warehouseResultUrl) {
		this.warehouseResultUrl = warehouseResultUrl;
	}

	public Map<String, AgenInfo> getAgenMap() {
		return this.agenMap;
	}

	public void setAgenMap(Map<String, AgenInfo> agenMap) {
		this.agenMap = agenMap;
	}
}