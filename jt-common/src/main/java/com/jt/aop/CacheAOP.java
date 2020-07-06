package com.jt.aop;

import java.lang.reflect.Method;

import org.apache.http.impl.conn.SingleClientConnManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.CacheFind;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

//定义缓存切面
@Component //万能注解 交给容器管理
@Aspect	   //自定义切面
public class CacheAOP {
	
	@Autowired(required = false)
	private JedisCluster jedis;
	//private Jedis jedis;	//哨兵的jedis对象
	//private ShardedJedis jedis;
	//private Jedis jedis;
	
	/**
	 * 	环绕通知的语法
	 * 	返回值类型:  任意类型用Obj包裹 
	 * 	参数说明:      必须包含并且位置是第一个  
	 * 			   ProceedingJoinPoint
	 * 	通知标识:	 
	 * 		1.@Around("切入点表达式")
	 * 		2.@Around(切入点())
	 */
	@SuppressWarnings("unchecked")
	@Around("@annotation(cacheFind)")
	public Object around(ProceedingJoinPoint joinPoint,CacheFind cacheFind) {
		//定义数据的返回值
		Object result = null;
		String key = getKey(joinPoint,cacheFind);
		String value = jedis.get(key);
		if(StringUtils.isEmpty(value)) {
			try {
				//缓存数据为null.查询数据库
				result = joinPoint.proceed();
				String json = ObjectMapperUtil.toJSON(result);
				
				if(cacheFind.seconds() >0) {
					//需要添加超时时间
					jedis.setex(key,cacheFind.seconds(), json);
				}else {
					jedis.set(key, json);
				}
				System.out.println("AOP实现数据库查询!!!!!");
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			
		}else {
			//缓存数据不为null 需要将json转化为对象
			Class returnType = getReturnType(joinPoint);
			result = ObjectMapperUtil.toObject(value, returnType);
			System.out.println("AOP实现缓存查询!!!!!");
		}
		
		return result;
	}

	/**
	 * 如何获取方法的返回值类型????
	 * 利用反射机制,动态获取当前方法对象Method对象
	 * @param joinPoint
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Class getReturnType(ProceedingJoinPoint joinPoint) {
		/*
		 * //1.获取方法的类型 Class targetClass = joinPoint.getTarget().getClass(); String
		 * methodName = joinPoint.getSignature().getName(); Object[] objs =
		 * joinPoint.getArgs(); Class[] paramClass = new Class[objs.length]; for(int
		 * i=0;i<objs.length;i++) { paramClass[i] = objs[i].getClass(); }
		 * 
		 * Class<?> returnType = null; try { Method method =
		 * targetClass.getMethod(methodName, paramClass); returnType =
		 * method.getReturnType(); } catch (NoSuchMethodException | SecurityException e)
		 * {
		 * 
		 * e.printStackTrace(); }
		 * 
		 * return returnType;
		 */
		
		//实现了方法对象的封装
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}

	/**
	 *   获取key数据
	 *   !null 以用户的key为主
	 *   null  自动生成key 包名.类名.方法名::第一个参数
	 * @param joinPoint
	 * @param cacheFind
	 * @return
	 */
	private String getKey(ProceedingJoinPoint joinPoint, CacheFind cacheFind) {
		
		String key = cacheFind.key();
		if(StringUtils.isEmpty(key)) {
			
			String className = 
				joinPoint.getSignature().getDeclaringTypeName();
			String methodName = joinPoint.getSignature().getName();
			Object firstArgs = joinPoint.getArgs()[0];
			return className+"."+methodName+"::"+firstArgs;
			
		}else {
			//以用户的数据为主
			return key;
		}
	}
}
