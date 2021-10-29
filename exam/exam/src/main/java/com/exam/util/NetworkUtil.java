package com.exam.util;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.exam.aop.log.LogRecord;
import com.google.gson.Gson;

/**
 *  网络工具类
 * 
 * @author Yifeng Wang  
 */
public class NetworkUtil {
	
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_HOST = "127.0.0.1";
    
    /**
     * 拼凑url后参数
     */
    public static String getUrlParams(HttpServletRequest request) {
        Map<String, String[]> paramsMap = request.getParameterMap();
        String operationCondition = "";
        if (paramsMap != null && !paramsMap.isEmpty()) {
            Set<String> keySet = paramsMap.keySet();
            for (String key : keySet) {
                String params = "";
                String[] values = paramsMap.get(key);
                for (String str : values) {
                    params = params + str;
                }

                operationCondition = operationCondition + key + " = " + params + " and ";
            }
            operationCondition = operationCondition.substring(0, operationCondition.lastIndexOf(" and "));
        }
        //数据库限制长度500是按照字节计算的
        operationCondition = StringUtil.substringBytes(operationCondition, 500);

        return operationCondition;
    }

    /**
     * 获取body请求参数
     */
	public static String getRequestBodyParams(ProceedingJoinPoint joinPoint) {
		 Object[] paramValues = joinPoint.getArgs();
		 MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
         String[] paramNames = methodSignature.getParameterNames();
         
         StringBuilder condition = new StringBuilder();
		 Gson gson = new Gson();
		 
		 int paramNameLength = paramNames.length;
		 int paramValueLength = paramValues.length;
		 if(paramNameLength > 0 && paramValueLength > 0) {
			 for(int i = 0; i < paramNameLength; i ++) {
				 
				 Object value = paramValues[i];
				 if(value instanceof HttpServletRequest || value instanceof HttpServletResponse) {
					 continue;
				 }
				 
				 condition.append(paramNames[i] + " = ");
				 
				 condition.append(gson.toJson(paramValues[i]));
				 
				 if (i < paramValueLength - 1) {
	                    condition.append(" and ");
	                }
			 }
		 }
		 
		 //参数过长需要截取
		 String result = condition.toString();
		 if (result != null && result.length() > 1000) {
//			 result = StringUtil.substringBytes(result, 1000);
			 result = result.substring(0, 1000);
         }
		 
         return result;
	}
    
    /**
     * 获取方法注解
     */
    public static Map<String, String> getControllerMethodDescription(JoinPoint joinPoint, String key) throws ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class<?> targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class<?>[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    map.put(key, method.getAnnotation(LogRecord.class).value());
                    break;
                }
            }
        }
        return map;
    }
    
    /**
     * 获取服务器IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();

            if (ip.equals(LOCAL_HOST)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    System.out.println("获取网络地址失败..");
                }
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15 && ip.indexOf(',') > -1) {
            ip = ip.substring(0, ip.indexOf(','));
        }
        return ip;
    }

}

