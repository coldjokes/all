package com.dosth.common.base.tips;

/**
 * 返回给前台的成功提示
 * 
 * @author guozhidong
 *
 */
public class SuccessTip extends Tip {

	public SuccessTip() {
		super.code = 200;
		super.message = "操作成功";
	}
}