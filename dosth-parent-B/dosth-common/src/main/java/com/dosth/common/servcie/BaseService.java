package com.dosth.common.servcie;

import java.io.Serializable;

import com.dosth.common.exception.DoSthException;

/**
 * 基础Service
 * 
 * @author guozhidong
 *
 */
public interface BaseService<T> {
	/**
	 * 添加
	 * 
	 * @param entity
	 * @throws DoSthException
	 */
	public void save(T entity) throws DoSthException;

	/**
	 * 根据用户Id查询信息
	 * 
	 * @param id
	 *            序列Id
	 * @return 对象
	 * @throws DoSthException
	 */
	public T get(Serializable id) throws DoSthException;

	/**
	 * 修改
	 * 
	 * @param entity
	 * @throws DoSthException
	 */
	public T update(T entity) throws DoSthException;

	/**
	 * 删除
	 * 
	 * @param entity
	 * @throws DoSthException
	 */
	public void delete(T entity) throws DoSthException;
}