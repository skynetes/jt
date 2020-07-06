package com.jt.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecutionAop {
	
	
	//如果程序出现了异常,则需要拦截,打印异常信息
	@AfterThrowing
	(pointcut = "execution(* com.jt.service..*.*(..))",
	 throwing = "throwable")
	public void afterThrow(JoinPoint joinPoint,Throwable throwable) {
		
		Class<?> targetClass = joinPoint.getTarget().getClass();
		String methodName = joinPoint.getSignature().getName();
		Class<?> throwClass = throwable.getClass();
		String msg = throwable.getMessage();
		System.out.println("目标对象类型:"+targetClass);
		System.out.println("目标方法:"+methodName);
		System.out.println("异常类型:"+throwClass);
		System.out.println("异常信息:"+msg);
	}
}
