package com.cnbaosi.enums;

/**
 * @description 板子协议
 * @author guozhidong
 *
 */
public enum BoardInstruction {
	/***************     锁控板          *****************/
	OPENSINGLE {
		@Override
		public String getDesc() {
			return "开单个通道锁";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x82;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x09;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x0B;
		}

		@Override
		public byte exc() {
			return (byte) 0xFF;
		}
	},
	READSINGLE {
		@Override
		public String getDesc() {
			return "读单个锁状态";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x83;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x09;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x0B;
		}

		@Override
		public byte exc() {
			return -1;
		}
	},
	READALL {
		@Override
		public String getDesc() {
			return "读取所有通道锁状态";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x84;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x08;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x3E;
		}

		@Override
		public byte exc() {
			return -1;
		}
	},
	OPENALL {
		@Override
		public String getDesc() {
			return "开全部通道锁";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x86;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x08;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}

		@Override
		public byte exc() {
			return -1;
		}
	},
	OPENMUTIL {
		@Override
		public String getDesc() {
			return "开多个通道锁";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x87;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x08;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}

		@Override
		public byte exc() {
			return -1;
		}
	},
	
	
	
	
	/***************     行列板          *****************/	
	
	/**
	 * @description 货道电机旋转
	 */
	REVOLVE {
		@Override
		public String getDesc() {
			return "货道电机旋转";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x90;
		}

		@Override
		public byte exc() {
			return (byte) 0xFF;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x0A;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	
	/**
	 * @description 货道复位
	 */
	SPRINGRESET {
		@Override
		public String getDesc() {
			return "货道复位";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x91;
		}

		@Override
		public byte exc() {
			return -1;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x08;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 开门
	 */
	OPENDOOR {
		@Override
		public String getDesc() {
			return "开门";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x92;
		}

		@Override
		public byte exc() {
			return 0x02;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x09;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 关门
	 */
	CLOSEDOOR {
		@Override
		public String getDesc() {
			return "关门";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x92;
		}

		@Override
		public byte exc() {
			return 0x02;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x09;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 翻转电机
	 */
	REVERSAL {
		@Override
		public String getDesc() {
			return "翻转电机";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x93;
		}

		@Override
		public byte exc() {
			return 0x01;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x09;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 伺服电机
	 */
	SERVOCONTROL {
		@Override
		public String getDesc() {
			return "伺服电机";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x94;
		}

		@Override
		public byte exc() {
			return 0x01;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x0B;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 到达取料口
	 */
	TODOOR {

		@Override
		public String getDesc() {
			return "到达取料口";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x94;
		}

		@Override
		public byte exc() {
			return 0x01;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x0B;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 伺服系统复位
	 */
	SERVORESET {
		@Override
		public String getDesc() {
			return "伺服系统复位";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x95;
		}

		@Override
		public byte exc() {
			return 0x01;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x08;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 读取状态
	 */
	READSTATUS {
		@Override
		public String getDesc() {
			return "读取状态";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x96;
		}

		@Override
		public byte exc() {
			return -1;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x08;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x0E;
		}
	},
	/**
	 * @description 灯和电磁铁控制
	 */
	LIGHTELECTROCONTROL {
		@Override
		public String getDesc() {
			return "灯和电磁铁控制";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x97;
		}

		@Override
		public byte exc() {
			return -1;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x0A;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x09;
		}
	},
	/**
	 * @description 可控抽屉柜读状态
	 */
	TROREAD {
		@Override
		public String getDesc() {
			return " 可控抽屉柜读状态";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0xC0;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x07;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x0B;
		}

		@Override
		public byte exc() {
			return 0x01;
		}
	},
	/**
	 * @description 可控抽屉柜打开
	 */
	TROOPEN {
		@Override
		public String getDesc() {
			return "可控抽屉柜打开";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0xC1;
		}

		@Override
		public byte getSendFrameLength() {
			return 0x09;
		}

		@Override
		public byte getReceiveFrameLength() {
			return 0x0B;
		}

		@Override
		public byte exc() {
			return 0x02;
		}
	};
	
	/**
	 * @description 描述
	 * @return
	 */
	public abstract String getDesc();

	/**
	 * @description 指令字
	 * @return
	 */
	public abstract byte getInstructionWord();

	/**
	 * @description 发送协议长度
	 * @return
	 */
	public abstract byte getSendFrameLength();
	
	/**
	 * @description 接收协议长度
	 * @return
	 */
	public abstract byte getReceiveFrameLength();

	/**
	 * @description 异常
	 * @return
	 */
	public abstract byte exc();
}