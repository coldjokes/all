package com.dosth.common.util;

import java.io.Serializable;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * 唯一id生成器
 * 
 * @author guozhidong
 *
 */
public class IdGenerator implements Configurable, IdentifierGenerator {
	private final IdWorker idWorker = new IdWorker(2);
	
	public String dataCenterID;

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		 this.dataCenterID = params.getProperty("dosth.dataCenterId");  
	}
	
	@Override
	public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
		return idWorker.nextId();
	}
}