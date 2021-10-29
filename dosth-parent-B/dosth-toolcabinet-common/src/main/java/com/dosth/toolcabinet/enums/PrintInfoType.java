package com.dosth.toolcabinet.enums;

public enum PrintInfoType {
	QRINFO {
		@Override
		public String getDesc() {
			return "二维码信息:";
		}
	},
	MATNAME {
		@Override
		public String getDesc() {
			return "名称:";
		}
	},
	BARCODE {
		@Override
		public String getDesc() {
			return "编码";
		}
	},
	MATSPEC {
		@Override
		public String getDesc() {
			return "型号:";
		}
	},
	USERNAME {
		@Override
		public String getDesc() {
			return "还料人:";
		}
	},
	PRINTTIME {
		@Override
		public String getDesc() {
			return "日期:";
		}
	},
	RETURNBACKTYPENAME {
		@Override
		public String getDesc() {
			return "归还类型:";
		}
	},
	RETURNBACKINFO {
		@Override
		public String getDesc() {
			return "归还详情:";
		}
	},
	PACKNUM {
		@Override
		public String getDesc() {
			return "包装数量:";
		}
	},
	BACKNUM {
		@Override
		public String getDesc() {
			return "正常归还:";
		}
	},
	BROKENNUM {
		@Override
		public String getDesc() {
			return "损坏数量:";
		}
	},
	LOSENUM {
		@Override
		public String getDesc() {
			return "遗失数量:";
		}
	},
	GRINDING {
		@Override
		public String getDesc() {
			return "修磨数量:";
		}
	},
	WRONGCOLLAR {
		@Override
		public String getDesc() {
			return "错领数量:";
		}
	},
	CONTINUEDUSE {
		@Override
		public String getDesc() {
			return "续用数量:";
		}
	},
	ISGETNEWONE {
		@Override
		public String getDesc() {
			return "以旧换新:";
		}
	},
	REALBACKNUM {
		@Override
		public String getDesc() {
			return "实际归还:";
		}
	},
	RETURNBACKNO {
		@Override
		public String getDesc() {
			return "归还编码:";
		}
	};

	/**
	 * @description 描述
	 * @return
	 */
	public abstract String getDesc();
}