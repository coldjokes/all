package com.exam.aop.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.exam.entity.enums.LogTypeEnum;
import com.exam.entity.modal.Log;
import com.exam.serivce.LogService;
import com.exam.util.NetworkUtil;

/**
 *  日志记录切面
 * 
 * @author Yifeng Wang
 */
@Aspect
@Component
public class LogRecordAspect {
	
    private static final Logger logger = LoggerFactory.getLogger(LogRecordAspect.class);
    private static final String ANNOTATION_KEY = "value";
    
    private HttpServletRequest request;

    @Autowired
    private LogService logSvc;
    
    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Pointcut("@annotation(com.exam.aop.log.LogRecord)") //TODO 路径
    public void methodLogRecordPointcut() {
        //系统日志切面
    }

    @Around("methodLogRecordPointcut()")
    public Object doController(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        String method = request.getMethod().toUpperCase();
        String url = request.getRequestURL().toString();
        
        try {
        	//获取日志类型
            boolean containsLogin = url.contains("login");
            int logType = 0;
            if(RequestMethod.POST.toString().equals(method)) {
            	if(!containsLogin) {
            		logType = LogTypeEnum.ADD.getCode();
            	} else {
            		logType = LogTypeEnum.LOGIN.getCode();
            	}
            } else if(RequestMethod.DELETE.toString().equals(method)) {
            	if(!containsLogin) {
            		logType = LogTypeEnum.LOGIN.getCode();
            	} else {
            		logType = LogTypeEnum.LOGOUT.getCode();
            	}
            } else if(RequestMethod.PUT.toString().equals(method)) {
            	logType = LogTypeEnum.UPDATE.getCode();
            } else if(RequestMethod.GET.toString().equals(method)) {
            	logType = LogTypeEnum.QUERY.getCode();
            }
            
            //获取参数
            String params = NetworkUtil.getRequestBodyParams(joinPoint);
            
            //获取注解值
            Map<String, String> map = NetworkUtil.getControllerMethodDescription(joinPoint, ANNOTATION_KEY);

            //IP地址
            String ipAddress = NetworkUtil.getClientIp(request);
            
            Log log = new Log();
        	log.setLogType(logType);
        	log.setBusiness(map.get(ANNOTATION_KEY));
        	log.setIpAddress(ipAddress);
        	log.setParams(params);
        	logSvc.addLog(log);
        } catch (Exception e) {
            logger.error("记录日志失败！", e);
        }
        return result;
    }
}
