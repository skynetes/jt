package com.jt.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.util.IPUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private JedisCluster jedisCluster;
	
	//测试方法
	@RequestMapping("/findAll")
	public List<User> findAll(){
		
		return userService.findAll();
	}
	
	/**
	 * url地址:http://sso.jt.com/user/check/{param}/{type}
	 * @return
	 */
	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(
			@PathVariable String param,
			@PathVariable Integer type,
			String callback) {

		//定义boolean 判断用户是否存在
		boolean flag = userService.findUserByType(param,type);
		SysResult sysResult = SysResult.success(flag);
		return new JSONPObject(callback, sysResult);
	}
	
	
	//用户信息key:JT_USER_admin123
	@RequestMapping("/query/{ticket}/{username}")
	public JSONPObject findUserByTicket(@PathVariable String ticket,
			@PathVariable String username,
			HttpServletRequest request,
			HttpServletResponse response,
			String callback) {
		
		JSONPObject object = null;
		
		//校验ticket是否有效 从redis中获取最终的ticket完成校验
		String redisTicket = jedisCluster.get("JT_USER_"+username);
		if(StringUtils.isEmpty(redisTicket)) {
			//IP地址不正确.
			object = new JSONPObject(callback,SysResult.fail());
			//删除cookie信息
			CookieUtil.deleteCookie("JT_TICKET","/","jt.com", response);
			CookieUtil.deleteCookie("JT_USER","/","jt.com", response);
			return object;
		}
		
		//如果数据不相等,说明数据有误,不能展现.
		if(!redisTicket.equals(ticket)) {
			
			//IP地址不正确.
			object = new JSONPObject(callback,SysResult.fail());
			//删除cookie信息
			CookieUtil.deleteCookie("JT_TICKET","/","jt.com", response);
			CookieUtil.deleteCookie("JT_USER","/","jt.com", response);
			return object;
		}
		
		
		//校验IP地址
		String IP = IPUtil.getIpAddr(request);
		Map<String,String> map = jedisCluster.hgetAll(ticket);
		
		//1.校验IP是否有效.
		if(!IP.equals(map.get("JT_USER_IP"))) {
			
			//IP地址不正确.
			object = new JSONPObject(callback,SysResult.fail());
			//删除cookie信息
			CookieUtil.deleteCookie("JT_TICKET","/","jt.com", response);
			//删除cookie信息
			CookieUtil.deleteCookie("JT_USER","/","jt.com", response);
			return object;
		}
		
		//2.校验ticket数据信息.
		String userJSON = map.get("JT_USER");
		if(StringUtils.isEmpty(userJSON)) {
			
			//IP地址不正确.
			object = new JSONPObject(callback,SysResult.fail());
			CookieUtil.deleteCookie("JT_TICKET","/","jt.com", response);
			CookieUtil.deleteCookie("JT_USER","/","jt.com", response);
			return object;
		}
		
		//3.表示校验成功
		object = new JSONPObject(callback, SysResult.success(userJSON));
		return object;
	}
}
