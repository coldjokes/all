package com.exam.aop.db;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.exam.config.db.DataSourceContextHolder;

/**
 * 
  * 数据库切面
 * 
 * @author Yifeng Wang  
 */
@Component
@Aspect
@Order(-1)
public class DataSourceAspect {
	@Pointcut("@within(com.exam.aop.db.DataSource) || @annotation(com.exam.aop.db.DataSource)")  //TODO 路径
    public void pointCut(){}

    @Before("pointCut() && @annotation(dataSource)")
    public void doBefore(DataSource dataSource){
    	System.out.println("切换数据--------" + dataSource.value().getValue());
        DataSourceContextHolder.setDataSource(dataSource.value().getValue());
    }

    @After("pointCut()")
    public void doAfter(){
        DataSourceContextHolder.clear();
    }
}
