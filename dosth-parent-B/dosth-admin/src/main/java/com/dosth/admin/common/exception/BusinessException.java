package com.dosth.admin.common.exception;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;

/**
 * 业务异常的封装
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class BusinessException extends DoSthException {

	public BusinessException(DoSthExceptionEnum bizExceptionEnum) {
		super(bizExceptionEnum.getCode(), bizExceptionEnum.getMessage(), bizExceptionEnum.getUrlPath());
	}

	public BusinessException(BizExceptionEnum errorWrapperField) {
		super(errorWrapperField.getCode(), errorWrapperField.getMessage(), errorWrapperField.getUrlPath());
	}
}