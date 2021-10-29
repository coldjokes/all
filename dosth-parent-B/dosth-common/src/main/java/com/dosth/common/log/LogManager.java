package com.dosth.common.log;

import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 日志管理器
 * 
 * @author guozhidong
 *
 */
public class LogManager {

	/** 异步操作日志线程池 */
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);

	/** 日志记录延时 */
	private final int OPERATE_DELAY_TIME = 10;

	public static LogManager logManager = new LogManager();
	
	private LogManager() {
	}

	public static LogManager me() {
		return logManager;
	}
	
	public void executeLog(TimerTask task) {
		executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
	}
}