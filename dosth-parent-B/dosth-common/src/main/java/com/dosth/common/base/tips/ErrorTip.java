package com.dosth.common.base.tips;

/**
 * 返回给前台的错误提示
 * 
 * @author guozhidong
 *
 */
public class ErrorTip extends Tip {

    public ErrorTip(int code, String message) {
        super();
        this.code = code;
        this.message = message;
    }
}