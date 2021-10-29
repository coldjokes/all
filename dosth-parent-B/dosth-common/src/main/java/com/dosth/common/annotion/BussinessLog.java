package com.dosth.common.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 标记需要做业务日志的方法
 * 
 * @author guozhidong
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface BussinessLog {

	/**
	 * 业务的名称,例如:"修改菜单"
	 */
	String businessName() default "";

	/**
	 * 是否忽略日志明细
	 */
	boolean ignore() default false;
}