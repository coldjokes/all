package com.exam.config.db;

/**
 * @author Yifeng Wang  
 */
public class DataSourceContextHolder {
	private static final ThreadLocal<String> contextHolder = new InheritableThreadLocal<>();

	/**
	 * 设置数据源
	 */
    public static void setDataSource(String db){
        contextHolder.set(db);
    }

    /**
     * 取得当前数据源
     */
    public static String getDataSource(){
        return contextHolder.get();
    }

    /**
     * 清除上下文数据
     */
    public static void clear(){
        contextHolder.remove();
    }
}
