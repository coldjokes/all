package com.cnbaosi.exception;

/**
 * @description 鲍斯异常类
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class BscException extends RuntimeException {

	public BscException() {
	}

	public BscException(String errMsg) {
		super(errMsg);
	}
}