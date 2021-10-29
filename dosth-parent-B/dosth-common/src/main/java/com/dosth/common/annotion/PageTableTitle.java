package com.dosth.common.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页表格表头
 * 
 * @author guozhidong
 *
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PageTableTitle {
	/**
	 * 属性中文说明,例如"姓名"
	 * 
	 * @return
	 */
	String value() default "";

	/**
	 * 属性是否外键
	 * 
	 * @return
	 */
	boolean isForeign() default false;

	/**
	 * 属性是否为枚举类型
	 * 
	 * @return
	 */
	boolean isEnum() default false;

	/**
	 * 是否是Id列
	 * 
	 * @return
	 */
	boolean isId() default false;
	
	/**
	 * 是否显示
	 */
	boolean isVisible() default true;
}