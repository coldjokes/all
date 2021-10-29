package com.dosth.tool.common.util;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 工具提取器
 * 
 * @author guozhidong
 *
 */
public class ToolPullManager {

	/** 异步操作日志线程池 */
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

	/** 工具提取记录延时 */
	private final int OPERATE_DELAY_TIME = 10;

	public static ToolPullManager toolPullManager = new ToolPullManager();

	private ToolPullManager() {
	}

	public static ToolPullManager me() {
		return toolPullManager;
	}

	public void executeLog(TimerTask task) {
		executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
	}
}