package com.cnbaosi.modbus.dto;

import java.io.Serializable;

import com.cnbaosi.enums.TipLevel;
import com.cnbaosi.modbus.enums.AddrType;

/**
 * @description modbus监控数据
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class MonitorData implements Serializable {
	/**
	 * @description 监控地址类型
	 */
	private AddrType type;
	/**
	 * @description 提示等级
	 */
	private TipLevel level;
	/**
	 * @description 监控结果 1为正常 0为异常
	 */
	private Integer result;

	public MonitorData(AddrType type, Integer result) {
		this.type = type;
		this.result = result;
	}

	public AddrType getType() {
		return this.type;
	}

	public void setLevel(TipLevel level) {
		this.level = level;
	}

	public TipLevel getLevel() {
		return this.level;
	}

	public Integer getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return  this.level.name() + "[" +  type.getCode() + "]:" + type.getDesc();
	}
}