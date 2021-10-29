package com.cnbaosi.enums;
 
/**
 * @description 料斗动作
 * @author guozhidong
 *
 */
public enum HopperWork {
    /**
     * @description 料斗上升
     */
    UP(1, "料斗上升"),
    /**
     * @description 料斗下降
     */
    DOWN(2, "料斗下降"),
    /**
     * @description 料斗复位
     */
    RESET(3, "料斗复位");
 
    private int code;
    private String desc;
 
    private HopperWork(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
 
    public int getCode() {
        return this.code;
    }
 
    public String getDesc() {
        return this.desc;
    }
}