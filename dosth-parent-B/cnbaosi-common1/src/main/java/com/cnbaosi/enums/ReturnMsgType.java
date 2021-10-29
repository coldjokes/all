package com.cnbaosi.enums;
 
/**
 * @descrption 返回消息类型
 * @author guozhidong
 *
 */
public enum ReturnMsgType {
	/**
	 * @description 错误码
	 */
	ERR_CODE("错误码"),
	/**
	 * @description 系统正忙
	 */
	BUSY("系统正忙"),
    /**
     * @description 读取失败
     */
    READ_FAIL("读取失败"),
	/**
	 * @description 夹手警报
	 */
	CLIP_HAND_ALARM("夹手警报"),
    /**
     * @description 打开成功
     */
    OPEN_SUCC("打开成功"),
    /**
     * @description 打开失败
     */
    OPEN_FAIL("打开失败"),
    /**
     * @description 门开启
     */
    OPENED("门已开启"),
    /**
     * @description 门未开启
     */
    NO_OPEN("门未开启"),
    /**
     * @description 关门成功
     */
    CLOSED_SUCC("关门成功"),
    /**
     * @description 关门失败
     */
    CLOSED_FAIL("关门失败"),
    /**
     * @description 门已关闭
     */
    CLOSED("门已关闭"),
    /**
     * @description 门未关闭
     */
    NO_CLOSE("门未关闭"),
    /**
     * @description 马达异常
     */
    MOTORERR("马达异常"),
    /**
     * @description 伺服复位成功
     */
    SERVOR_RESET_SUCC("伺服复位成功"),
    /**
     * @description 伺服复位失败
     */
    SERVOR_RESET_FAIL("伺服复位失败"),
    /**
     * @description 伺服驱动器复位ALM
     */
    SERVOR_REST_ALM("伺服驱动器复位ALM"),
    /**
     * @description 伺服驱动器上限位报警
     */
    SERVOR_TOP_ALM("伺服驱动器上限位报警"),
    /**
     * @description 伺服驱动器下限报警
     */
    SERVOR_BOTTOM_ALM("伺服驱动器下限报警"),
    /**
     * @description 伺服驱动器ALM
     */
    SERVOR_ALM("伺服驱动器ALM"),
    /**
     * @description 伺服驱动器异常
     */
    SERVOR_FAIL("伺服驱动器异常"),
    /**
     * @description 到达取料口失败
     */
    TODOOR_FAIL("到达取料口失败"),
    /**
     * @description 回收口复位成功
     */
    RECOVERY_SUCC("回收口复位成功"),
    /**
     * @description 回收口复位失败
     */
    RECOVERY_FAIL("回收口复位失败"), 
    /**
     * @description 到门口
     */
    TODOOR("到门口"),
    /**
     * @description 伺服器
     */
    SERVOR("伺服器"),
    /**
     * @description 伺服器复位
     */
    SERVOR_REST("伺服器复位"),
    /**
     * @description 料斗操作成功
     */
    SERVOR_OP_SUCC("料斗操作成功"),
    /**
     * @description 料斗操作失败
     */
    SERVOR_OP_FAIL("料斗操作失败"),
    /**
     * @description 打印机缺纸
     */
    PRINT_NO_PAPER("打印机缺纸"),
    /**
     * @description 打印成功
     */
    PRINT_SUCC("打印成功"),
    /**
     * @description 打印失败
     */
    PRINT_FIAL("打印失败"),
    /**
     * @description 未接收到反馈信息
     */
    UNRECEIVED("未接收到反馈信息"),
    /**
     * @description 超时
     */
    TIME_OUT("超时");
 
    private String desc; // 描述
 
    private ReturnMsgType(String desc) {
        this.desc = desc;
    }
 
    public String getDesc() {
        return this.desc;
    }
}