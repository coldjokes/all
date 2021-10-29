package com.dosth.comm.softhand;

public enum SoftHandComm {
	SHIFT {
		@Override
		public String desc() {
			return "取料";
		}
	},
	LIFT {
		@Override
		public String desc() {
			return "料斗";
		}
	},
	MOTORERR {
		@Override
		public String desc() {
			return "马达错误";
		}
	},
	UNDEFINED {
		@Override
		public String desc() {
			return "未定义";
		}
	};
	/**
	 * @description 描述
	 * @return
	 */
	public abstract String desc();
}