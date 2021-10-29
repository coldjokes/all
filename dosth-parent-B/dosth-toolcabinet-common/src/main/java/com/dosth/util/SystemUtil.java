package com.dosth.util;

import java.io.IOException;

/**
 *  
 * 
 * @author Yifeng Wang  
 */
public class SystemUtil {
	
	private static final String SHUTDOWN = "shutdown -s -t "; //关机
	private static final String RESTART = "shutdown -r -t "; //重启

    // 关机
    public static void shutdown(Integer sec) {
    	if(sec == null) sec = 0;
    	executeCode(SHUTDOWN + sec);
    }
    
    // 重启
    public static void restart(Integer sec) {
    	if(sec == null) sec = 0;
    	executeCode(RESTART + sec);
    }
    
    // 执行脚本
    public static void executeBat(String path) {
    	String cmd = "cmd /k start " + path;
    	executeCode(cmd);
    }
    
	/**
	 * @param code 系统指令
	 */
	public static void executeCode(String code) {
        try {
            Runtime.getRuntime().exec(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
}

