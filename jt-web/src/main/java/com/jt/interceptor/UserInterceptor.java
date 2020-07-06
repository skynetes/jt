package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.jt.pojo.User;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

@Component
public class UserInterceptor implements HandlerInterceptor{
	
	
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 用户如果不登录,则不允许访问服务器.
	 * 判断依据:
	 * 	JT_TICKET和JT_USER是否有数据.
	 * 
	 * boolean: 
	 * 			true:代表放行 
	 * 			false: 当前请求拦截 一般和重定向联用
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//1.获取cookie的值
		Cookie ticketCookie = CookieUtil.get(request, "JT_TICKET");
		Cookie userNameCookie = CookieUtil.get(request, "JT_USER");
		
		if(ticketCookie==null || userNameCookie==null) {
			response.sendRedirect("/user/login.html"); //重定向登陆页面
			return false; //表示拦截
		}
		
		//2.获取数据
		String ticket = ticketCookie.getValue();
		String username = userNameCookie.getValue();
		
		if(StringUtils.isEmpty(ticket) || StringUtils.isEmpty(username)) {
			response.sendRedirect("/user/login.html"); //重定向登陆页面
			return false; //表示拦截
		}
		
		//3.开启校验 防止cookie被盗用.校验IP
		String requestIP = IPUtil.getIpAddr(request);
		String realIP = jedisCluster.hget(ticket, "JT_USER_IP");
		if(!requestIP.equals(realIP)) {
			CookieUtil.deleteCookie("JT_TICKET", "/", "jt.com", response);
			CookieUtil.deleteCookie("JT_USER", "/", "jt.com", response);
			response.sendRedirect("/user/login.html"); //重定向登陆页面
			return false; //表示拦截
		}
		
		//4.判断是否为正确的用户登陆
		String realTicket = jedisCluster.get("JT_USER_"+username);
		if(!ticket.equals(realTicket)) {
			CookieUtil.deleteCookie("JT_TICKET", "/", "jt.com", response);
			CookieUtil.deleteCookie("JT_USER", "/", "jt.com", response);
			response.sendRedirect("/user/login.html"); //重定向登陆页面
			return false; //表示拦截
		}
		
		//5.动态获取user数据  username可以的但是ticket更快.
		String userJSON = jedisCluster.hget(ticket, "JT_USER");
		User user = ObjectMapperUtil.toObject(userJSON, User.class);
		//利用request对象动态传递数据信息.
		request.setAttribute("JT_USER", user);
		//利用ThreadLocal实现数据共享.
		UserThreadLocal.set(user);
		
		//5.表示用户信息正确的,应该放行
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
		//为了防止内存泄漏
		UserThreadLocal.remove();
	}
}
