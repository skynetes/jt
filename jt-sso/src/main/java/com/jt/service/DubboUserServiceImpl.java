package com.jt.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.JedisCluster;

/**
 *   构建接口的实现类
 * @author Administrator
 *   该实现类需要交给dubbo管理
 */
@Service
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private JedisCluster jedisCluster;
	
	/**
	 * 1.md5加密取值多少种????
	 * 	 32位16进制数=2^128
	 * 	 
	 */
	@Override
	public void insertUser(User user) {
		String md5Pass = DigestUtils
						.md5DigestAsHex
						(user.getPassword().getBytes());
		user.setPassword(md5Pass)
			.setEmail(user.getPhone()) //暂时使用电话代替
			.setCreated(new Date())
			.setUpdated(user.getCreated());
		userMapper.insert(user);
	}
	
	
	/**
	 * 1.根据username和password(明文~~密码)查询数据库.
	 * 2.为null return null   
	 * 3.不为null, 准备ticket数据 UUID 准备userJSON数据
	 * 	 将数据保存到redis中. 7天有效.
	 * 4.返回秘钥.
	 */
	@Override
	public String findUserByUP(User user,String userIP) {
		String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(password); //密码加密
		//根据对象中不为null的属性充当where条件 关系符=号
		QueryWrapper<User> queryWrapper = 
				new QueryWrapper<User>(user);
		//根据条件查询数据库记录
		User userDB = userMapper.selectOne(queryWrapper);
		
		//判断userDB是否为null
		if(userDB == null) {
			//用户名和密码不正确
			return null;
		}
		
		/**
		 * 为了保证redis资源不浪费,则需要校验数据.
		 * 如果检查发现当前用户已经登陆过,则删除之前的数据.
		 */
		if(jedisCluster.exists("JT_USER_"+user.getUsername())) {
			//之前已经登录过.删除之前的ticket
			String oldTicket = jedisCluster.get("JT_USER_"+user.getUsername());
			jedisCluster.del(oldTicket);
		}
		
		
		//程序执行到这里说明用户输入正确.
		//3.1获取uuid
		String ticket = UUID.randomUUID().toString();
		//3.2准备userJSON数据  数据必须进行脱敏处理
		userDB.setPassword("123456");
		String userJSON = ObjectMapperUtil.toJSON(userDB);
		jedisCluster.hset(ticket, "JT_USER", userJSON);
		jedisCluster.hset(ticket, "JT_USER_IP", userIP);
		jedisCluster.expire(ticket, 7*24*3600);
		
		//将用户名和ticket信息绑定
		jedisCluster
		.setex("JT_USER_"+user.getUsername(),7*24*3600,ticket);
		
		//用户名和ticket绑定即可!!!!!!
		return ticket;
	}
	
}
