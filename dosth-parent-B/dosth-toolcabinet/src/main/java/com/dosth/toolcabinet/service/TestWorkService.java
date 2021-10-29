package com.dosth.toolcabinet.service;

import com.dosth.toolcabinet.enums.TestWorkType;
import com.dosth.util.OpTip;

/**
 * @description 测试工作接口
 * @author guozhidong
 *
 */
public interface TestWorkService {
	/**
	 * @description 测试工作
	 * @param testWork 测试工作类型
	 * @return
	 */
	public OpTip testWork(TestWorkType testWork);
}