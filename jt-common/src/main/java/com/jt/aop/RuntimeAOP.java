package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RuntimeAOP {
	
	/**
	 * execution(* com.jt.service..*.*(..))
	 * 拦截service中的全部类的全部方法的任意参数.
	 * @param joinPoint
	 * @return
	 */
	@Around("execution(* com.jt.service..*.*(..))")
	public Object around(ProceedingJoinPoint joinPoint) {
		Long startTime = System.currentTimeMillis();
		Object obj = null;
		//执行目标方法.
		try {
			obj = joinPoint.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		Long endTime = System.currentTimeMillis();
		Class<?> targetClass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		System.out.println("目标对象的类型:"+targetClass);
		System.out.println("目标方法的名称:"+methodName);
		System.out.println("方法的执行时间:"+(endTime-startTime)+"毫秒");
		return obj;
	}
}
