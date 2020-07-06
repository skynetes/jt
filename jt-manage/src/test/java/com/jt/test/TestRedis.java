package com.jt.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.Transaction;

public class TestRedis {
	
	private Jedis jedis;
	
	@Before	//当执行@Test测试方法之前执行
	public void init() {
		
		jedis = new Jedis("192.168.241.128", 6379);
	}
	
	/**
	 * 报错:
	 * 		1.执行关闭防火墙命令  service iptables stop
	 * 		2.redis-server redis.conf(默认启动 权限不正确)
	 * 		3.检查配置文件配置3处 ip绑定 保护模式关闭 后台启动开启
	 * 1.操作String类型
	 * @throws InterruptedException 
	 */
	@Test
	public void testString() throws InterruptedException {
		
		jedis.set("1908","好好学习,天天进步!!!!!");
		System.out.println(jedis.get("1908"));
		
		jedis.set("1908", "不好好学习,打屁股!!!");
		System.out.println(jedis.get("1908"));
		
		//3.如果key已经存在,则不允许操作redis.
		//原理:只能操作不存在的key  0 失败  /1 成功!!!
		Long flag = jedis.setnx("1909", "今天周二!!!!");
		System.out.println(jedis.get("1909"));
		System.out.println("标识符:"+flag);
		
		//4.为数据添加超时时间 10秒
		//jedis.set("1908", "abc");
		//jedis.expire("1908", 10);
		jedis.setex("1908", 20, "随机");
		//jedis.psetex(key, milliseconds, value)
		Thread.sleep(3000L);
		System.out.println("存活时间:"+jedis.ttl("1908"));
	}
	
	/**
	 * 要求:setnx和setex的方法要求同时完成
	 * 实际用法: 实现redis分布式锁的关键.
	 */
	
	@Test
	public void testNXEX() {
		String result = 
				jedis.set("abc", "小学生之手,人头狗", "NX", "EX", 20);
		System.out.println("成功返回OK,不成功返回null");
		System.out.println("获取结果:"+result);
	}
	
	
	/**
	 * 操作hash类型 
	 * 	一般用于保存对象
	 */
	@Test
	public void testHash() {
		jedis.hset("persion", "id", "100");
		jedis.hset("persion","name","tomcat");
		Map<String,String> map = jedis.hgetAll("persion");
		System.out.println(map);
	}
	
	
	/**
	 * 操作List集合
	 * 队列和栈
	 * 特点:取出数据之后,内存中不会保存该数据.
	 *
	 */
	@Test
	public void list() {
		jedis.lpush("list","1","2","3","4","5");
		System.out.println(jedis.lpop("list"));
		
	}
	
	
	/**
	 * redis中的事务
	 */
	@Test
	public void testTx() {
		//1.开启事务
		Transaction transaction = jedis.multi();
		try {
			transaction.set("aaa", "aaa");
			transaction.set("bbb", "bbb");
			transaction.exec();
		} catch (Exception e) {
			e.printStackTrace();
			transaction.discard();
		}
	}
	
	
	/**
	 * 测试redis分片机制
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<>();
		shards.add(new JedisShardInfo("192.168.241.128",6379));
		shards.add(new JedisShardInfo("192.168.241.128",6380));
		shards.add(new JedisShardInfo("192.168.241.128",6381));
		//操作分片的工具API
		ShardedJedis jedis = new ShardedJedis(shards);
		jedis.set("1908", "分片测试案例!!!!!");
		System.out.println(jedis.get("1908"));
	}
	
	/**
	 * masterName: 主机的变量名
	 * 哨兵地址:  host:port
	 */
	@Test
	public void testSentinel() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add("192.168.241.128:26379");
		JedisSentinelPool pool = 
				new JedisSentinelPool("mymaster", sentinels);
		Jedis jedis = pool.getResource();
		jedis.set("abc", "aaaaa");
		System.out.println(jedis.get("abc"));
		
	}
	
	
	/**
	 * 实现redis集群测试
	 * 
	 */
	@Test
	public void testCluster() {
		Set<HostAndPort> nodes = new HashSet<HostAndPort>();
		nodes.add(new HostAndPort("192.168.241.128",7000));
		nodes.add(new HostAndPort("192.168.241.128",7001));
		nodes.add(new HostAndPort("192.168.241.128",7002));
		nodes.add(new HostAndPort("192.168.241.128",7003));
		nodes.add(new HostAndPort("192.168.241.128",7004));
		nodes.add(new HostAndPort("192.168.241.128",7005));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//hash槽算法
		jedisCluster.set("1908", "redis集群搭建完成!!!");
		System.out.println(jedisCluster.get("1908"));
	}
	
	
	
	
	
	
	
}
