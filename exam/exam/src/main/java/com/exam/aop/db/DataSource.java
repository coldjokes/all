package com.exam.aop.db;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.exam.entity.enums.DataSourceEnum;


/**
  * 数据库注解
 * 
 * @author Yifeng Wang  
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
	DataSourceEnum value() default DataSourceEnum.DEFAULT;
}
