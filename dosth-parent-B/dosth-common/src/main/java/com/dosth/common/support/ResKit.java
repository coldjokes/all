package com.dosth.common.support;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 资源文件相关的操作类
 * 
 * @author guozhidong
 *
 */
public class ResKit {

	/**
	 * 批量获取classpath下的资源文件
	 * 
	 * @param pattern
	 * @return
	 */
	public static Resource[] getClassPathResources(String pattern) {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			return resolver.getResources(pattern);
		} catch (IOException e) {
			throw new RuntimeException("加载resource文件时,找不到文件" + pattern);
		}
	}

	/**
	 * 批量获取classpath下的资源文件
	 * 
	 * @param file
	 * @return
	 */
	public static String getClassPathFile(String file) {
		return Thread.currentThread().getContextClassLoader().getResource(file).getPath();
	}
}