package com.dosth.enums;

/**
 * 刀具柜配置类
 * 
 * @author Weifeng.Li
 *
 */
public class SetupKey {

	private static class CabinetProp {
		/**
		 * @description 门
		 */
		public static final String DOOR = "DOOR";
	}

	/**
	 * 刀具柜主要信息
	 */
	public static class Cabinet {
		/**
		 * 刀具柜序列号
		 */
		public static final String SERIAL_NO = "SERIAL_NO";
		/**
		 * 刀具柜类型
		 */
		public static final String CABINET_TYPE = "CABINET_TYPE";
		/**
		 * 刀具柜名称
		 */
		public static final String CABINET_NAME = "CABINET_NAME";
		/**
		 * @description 主柜Id
		 */
		public static final String MAIN_CABINET_ID = "MAIN_CABINET_ID";
	}

	/**
	 * 刀具柜公共信息
	 */
	public static class Public {
		/**
		 * 人脸识别登录
		 */
		public static final String FACE_LOGIN = "FACE_LOGIN";
		/**
		 * 打印机COM口
		 */
		public static final String PRINT_COM = "PRINT_COM";
		/**
		 * 打印机是否自动切刀
		 */
		public static final String PRINT_CUT = "PRINT_CUT";
		/**
		 * @description 打印条码类型
		 */
		public static final String PRINT_TYPE_CODE = "PRINT_TYPE_CODE";
		/**
		 * @description 打印机波特率
		 */
		public static final String PRINT_BAUD = "PRINT_BAUD";
		/**
		 * 扫描仪COM口
		 */
		public static final String SCAN_COM = "SCAN_COM";
		/**
		 * 扫描仪类型
		 */
		public static final String SCAN_TYPE = "SCAN_TYPE";
		/**
		 * 描仪波特率
		 */
		public static final String SCAN_BAUD = "SCAN_BAUD";

	}

	/**
	 * PLC
	 *
	 */
	public static class Plc extends CabinetProp {
		/**
		 * PLC ip地址
		 */
		public static final String PLC_IP = "PLC_IP";
		/**
		 * PLC 端口
		 */
		public static final String PLC_PORT = "PLC_PORT";
		/**
		 * @description 顶层
		 */
		public static final String PLC_TOP_LEVEL = "PLC_TOP_LEVEL";
	}

	/**
	 * 行列式
	 *
	 */
	public static class Det extends CabinetProp {
		/**
		 * 行列式COM口
		 */
		public static final String DET_COM = "DET_COM";
		/**
		 * @description 行列式COM口波特率
		 */
		public static final String DET_BAUD = "DET_BAUD";
		/**
		 * 行列式栈号
		 */
		public static final String DET_BOARD_NO = "DET_BOARD_NO";
		/**
		 * 行列式门高
		 */
		public static final String DET_DOOR_HEIGHT = "DET_DOOR_HEIGHT";
		/**
		 * 行列式初始层高
		 */
		public static final String DET_LEVEL_HEIGHT = "DET_LEVEL_HEIGHT";
		/**
		 * 行列式层间距
		 */
		public static final String DET_LEVEL_SPACING = "DET_LEVEL_SPACING";
		/**
		 * @description 顶层高度
		 */
		public static final String DET_TOP_HEIGHT = "DET_TOP_HEIGHT";
	}

	/**
	 * C型柜
	 *
	 */
	public static class CCabinet {
		/**
		 * 行列式栈号-A
		 */
		public static final String DET_BOARD_NO_A = "DET_BOARD_NO_A";
		/**
		 * 行列式栈号-B
		 */
		public static final String DET_BOARD_NO_B = "DET_BOARD_NO_B";
		/**
		 * 行列式栈号-A-ID
		 */
		public static final String DET_BOARD_NO_A_ID = "DET_BOARD_NO_A_ID";
		/**
		 * 行列式栈号-B-ID
		 */
		public static final String DET_BOARD_NO_B_ID = "DET_BOARD_NO_B_ID";
	}

	/**
	 * 虚拟仓
	 *
	 */
	public static class VirtualWarehouse {

	}

	/**
	 * 暂存柜
	 *
	 */
	public static class TemCabinet {
		/**
		 * 暂存柜COM口
		 */
		public static final String TEM_COM = "TEM_COM";
		/**
		 * 暂存柜波特率
		 */
		public static final String TEM_BAUD = "TEM_BAUD";
		/**
		 * 暂存柜栈号
		 */
		public static final String TEM_BOARD_NO = "TEM_BOARD_NO";
		/**
		 * 暂存柜行号
		 */
		public static final String TEM_ROW_NO = "TEM_ROW_NO";
		/**
		 * 暂存柜列号
		 */
		public static final String TEM_COL_NO = "TEM_COL_NO";
		/**
		 * 暂存柜共享开关
		 */
		public static final String TEM_SHARE_SWITCH = "TEM_SHARE_SWITCH";
	}

	/**
	 * 储物柜
	 *
	 */
	public static class StoreCabinet {
		/**
		 * 储物柜COM口
		 */
		public static final String STORE_COM = "STORE_COM";
		/**
		 * 储物柜波特率
		 */
		public static final String STORE_BAUD = "STORE_BAUD";
		/**
		 * 储物柜栈号
		 */
		public static final String STORE_BOARD_NO = "STORE_BOARD_NO";
		/**
		 * 储物柜行号
		 */
		public static final String STORE_ROW_NO = "STORE_ROW_NO";
		/**
		 * 储物柜列号
		 */
		public static final String STORE_COL_NO = "STORE_COL_NO";
	}

	/**
	 * 回收柜
	 *
	 */
	public static class RecCabinet {
		/**
		 * 扫描仪COM口
		 */
		public static final String REC_SCAN_COM = "REC_SCAN_COM";
		/**
		 * 扫描仪类型
		 */
		public static final String REC_SCAN_TYPE = "REC_SCAN_TYPE";
		/**
		 * 扫描仪波特率
		 */
		public static final String REC_SCAN_BAUD = "REC_SCAN_BAUD";
	}
	
	/**
	 * 可控抽屉柜
	 * @author Zhidong.Guo
	 *
	 */
	public static class TrolDrawerCabinet {
		/**
		 * COM口
		 */
		public static final String TROL_COM = "TROL_COM";
		/**
		 * 波特率
		 */
		public static final String TROL_BAUD = "TROL_BAUD";
	}
}
