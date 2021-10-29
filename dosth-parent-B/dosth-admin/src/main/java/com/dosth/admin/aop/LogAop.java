package com.dosth.admin.aop;

import com.dosth.admin.common.log.LogTaskFactory;
import com.dosth.admin.common.shiro.ShiroKit;
import com.dosth.common.annotion.BussinessLog;
import com.dosth.common.cache.LogObjectHolder;
import com.dosth.common.log.LogManager;
import com.dosth.common.shiro.ShiroAccount;
import com.dosth.common.support.HttpKit;
import com.dosth.common.util.Contrast;

import org.apache.shiro.authc.AuthenticationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 日志记录
 * 
 * @author guozhidong
 *
 */
@Aspect
@Component
public class LogAop {

	private Logger log = LoggerFactory.getLogger(this.getClass());
	// 实体包路径
	private String packagePath = "com.dosth.admin.entity.";

	@Pointcut(value = "@annotation(com.dosth.common.annotion.BussinessLog)")
	public void cutService() {
	}

	@Around("cutService()")
	public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
		// 先执行业务
		Object result = point.proceed();
		try {
			this.handle(point);
		} catch (Exception e) {
			log.error("日志记录出错!", e);
		}
		return result;
	}

	private void handle(ProceedingJoinPoint point) throws Exception {
		// 获取拦截的方法名
		Signature sig = point.getSignature();
		MethodSignature msig = null;
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("该注解只能用于方法");
		}
		msig = (MethodSignature) sig;
		Object target = point.getTarget();
		// 获取操作名称
		Method currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
		String methodName = currentMethod.getName();
		BussinessLog annotation = currentMethod.getAnnotation(BussinessLog.class);
		// 如果当前用户未登录，不做日志
		ShiroAccount account = ShiroKit.getAccount();
		if (null == account) {
			throw new AuthenticationException();
		}
		// 获取拦截方法的参数
		String className = point.getTarget().getClass().getName();
		Object[] params = point.getArgs();
		String bussinessName = annotation.businessName();
		// 如果涉及到修改,比对变化
		Map<String, String> parameters = HttpKit.getRequestParameters();
		String msg = null;
		if (!annotation.ignore()) {
			if (bussinessName.indexOf("修改") != -1 || bussinessName.indexOf("编辑") != -1) {
				Object obj = LogObjectHolder.me().get();
				msg = Contrast.getInstance(packagePath).contrastObj(obj, parameters);
			} else {
				msg = Contrast.getInstance(packagePath).parseMutiKey(params, parameters);
			}
		} else {
			msg = "数据变更被忽略";
		}
		LogManager.me()
				.executeLog(LogTaskFactory.businessLog(account.getId(), bussinessName, className, methodName, msg));
	}
}