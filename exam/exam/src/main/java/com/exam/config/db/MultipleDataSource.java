package com.exam.config.db;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Yifeng Wang  
 */
public class MultipleDataSource extends AbstractRoutingDataSource {
	 
	/**
     * 取得当前使用哪个数据源
     */
	@Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}
