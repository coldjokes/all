package com.dosth.instruction.board.enums;

/**
 * @description 通信指令
 * @author guozhidong
 *
 */
public enum Instruction {
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
	 * @description 推杆电机
	 */
	HANDSPIKE {
		@Override
		public String getDesc() {
			return "推杆电机";
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
			return -1;
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
	 * @description 到达取料口门
	 */
	TODOOR {
		@Override
		public String getDesc() {
			return "取料口门";
		}

		@Override
		public byte getInstructionWord() {
			return (byte) 0x94;
		}

		@Override
		public byte exc() {
			return -1;
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
	 * @description 取料口开门
	 */
	OPENDOOR {
		@Override
		public String getDesc() {
			return "取料口开门";
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
	 * @description 取料口关门
	 */
	CLOSEDOOR {
		@Override
		public String getDesc() {
			return "取料口关门";
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
	 * @description 开灯
	 */
	ONLIGHT {
		@Override
		public String getDesc() {
			return "取料口开灯";
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
	 * @description 关灯
	 */
	OFFLIGHT {
		@Override
		public String getDesc() {
			return "取料口关灯";
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
			return -1;
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
	 * @description 灯和电磁铁状态
	 */
	LIGHTELECTROSTATUS {
		@Override
		public String getDesc() {
			return "灯和电磁铁状态";
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
			return "电磁铁";
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
	 * @description 成功
	 * @return
	 */
	public byte succ() {
		return 0x00;
	}

	/**
	 * @description 失败
	 * @return
	 */
	public byte fail() {
		return 0x01;
	}

	/**
	 * @description 异常
	 * @return
	 */
	public abstract byte exc();
}