package com.dosth.common.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dosth.common.base.tips.ErrorTip;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 * 
 * @author guozhidong
 *
 */
public class BaseControllerExceptionHandler {
 
    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 拦截业务异常
     * @param e
     * @return
     */
    @ExceptionHandler(DoSthException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip notFound(DoSthException e) {
        log.error("业务异常:", e);
        return new ErrorTip(e.getCode(), e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip notFound(RuntimeException e) {
        log.error("运行时异常:", e);
        return new ErrorTip(DoSthExceptionEnum.SERVER_ERROR.getCode(), DoSthExceptionEnum.SERVER_ERROR.getMessage());
    }
}