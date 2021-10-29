package com.cnbaosi.common.constant;

/**
 * @description 枚举门
 * @author guozhidong
 *
 */
public enum EnumDoor {

	LEFT {
		@Override
		public String desc() {
			return "左侧门";
		}
	},
	RIGHT {
		@Override
		public String desc() {
			return "右侧门";
		}
	},
	ALL {
		@Override
		public String desc() {
			return "全部";
		}
		
	}
	;
	
	public abstract String desc();
}