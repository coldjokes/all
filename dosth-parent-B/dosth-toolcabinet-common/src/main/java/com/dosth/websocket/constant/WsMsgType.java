package com.dosth.websocket.constant;

/**
 * WebSocket消息类型
 * 
 * @author guozhidong
 *
 */
public enum WsMsgType {
	
	/**
	 * @Desc 文本消息
	 */
	TEXT(0, "文本消息"),
	/**
	 * @Desc 柜子格子详细信息
	 */
	LATTICEDETAIL(1, "柜子格子详细信息"),
	/**
	 * @Desc 柜子格子数据
	 */
	LATTICEVALUE(2, "柜子格子数据"),
	/**
	 * @Desc 柜子状态
	 */
	LATTICESTATUS(3, "柜子状态"),
	/**
	 * @Desc 问询式对话框
	 */
	CONFIRM(4, "问询"),
	/**
	 * @Desc 用户刷IC卡登陆智能柜
	 */
	ICSWIPING(5, "用户刷IC卡登陆智能柜"), 
	/**
	 * @Desc 手机二维码扫描仪启用停用通讯
	 */
	PRINTSCAN(6, "手机二维码扫描仪启用停用通讯"),
	/**
	 * @Desc 取料口门已打开
	 */
	DOOROPENED(7, "取料口门已打开"),
	/**
	 * @Desc 取料口门已关闭
	 */
	DOORCLOSED(8, "取料口门已关闭"),
	/**
	 * @Desc 锁孔板状态
	 */
	LOCKSTATUS(9, "锁孔板状态"),
	
	/**
	 * @Desc 暂存柜弹出信息
	 */
	SUBALERTINFO(10,"暂存柜弹出信息"),
	
	/**
	 * @Desc 归还信息
	 */
	RETURN_BACK_INFO(11, "归还信息"),
	/**
	 * @Desc 打印好的条码信息
	 */
	PRINTED_CODE_INFO(12, "已打印条码信息"),
	/**
	 * @description 暂存柜未监测到信号
	 */
	SUB_UNRECEIVED(13, "未监测到信号"),
	/**
	 * @description 门板未归位
	 */
	DOOR_ERR(14, "门未归位"),
    /**
     * @description 打印机缺纸
     */
    PRINT_NO_PAPER(15, "打印机缺纸"),
    /**
     * @description 打印成功
     */
    PRINT_SUCC(16, "打印成功"),
    /**
     * @description 打印失败
     */
    PRINT_FIAL(17, "打印失败"),
    /**
     * @description 盒子柜被打开状态
     */
    BOX_CABINET_OPENED(18, "盒子柜被打开"),
    /**
     *  @description 响应超时或通讯失败
     */
    TIME_OUT(19, "响应超时或通讯失败"),
    /**
	 * @description 抽屉打开成功
	 */
	SUB_OPEN_SUC(20, "抽屉打开成功"),
	/**
	 * @description 故障
	 */
	ERR_TIP(101, "故障"),
	/**
	 * @description 警告
	 */
	WARN_TIP(102, "警告"),
	/**
	 * @description 信息
	 */
	INFO_TIP(103, "信息"),
	/**
	 * @description 可控抽屉柜重试
	 */
	TROL_DRAWER_AGAIN(21, "可控抽屉柜重试");
	

	private int code;
	private String name;

	private WsMsgType(int code, String name) {
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}
}