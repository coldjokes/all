package com.dosth.toolcabinet.dto.enums;

/**
 * 
 * @description 锁板状态
 * @author guozhidong
 *
 */
public enum LockStatus {
	OPEN {
		@Override
		public String getDesc() {
			return "打开";
		}
	},
	CLOSE {
		@Override
		public String getDesc() {
			return "关闭";
		}
	};
	/**
	 * @description 描述
	 * @return
	 */
	public abstract String getDesc();
}