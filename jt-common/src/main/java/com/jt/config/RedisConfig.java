package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

@Configuration //标识配置类  xml配置
//引入主启动类所在项目的配置文件.
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	@Value("${redis.nodes}")
	private String nodes;	//node,node,node
	
	@Scope("prototype")
	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodeSet = new HashSet<>();
		String[] arrayNode = nodes.split(",");
		for (String node : arrayNode) { //host:port
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			HostAndPort hostAndPort = new HostAndPort(host, port);
			nodeSet.add(hostAndPort);
		}
		
		return new JedisCluster(nodeSet);
	}
}
	/*
	 * @Value("${redis.sentinel}") private String nodes;
	 * 
	 * //问题: 1.连接池频繁创建 2.哨兵地址写死了
	 * 
	 * @Bean //将池交给spring容器管理 public JedisSentinelPool pool() { Set<String>
	 * sentinels = new HashSet<>(); sentinels.add(nodes); return new
	 * JedisSentinelPool("mymaster", sentinels); }
	 */
	/**
	 * @Bean注解说明 可以帮助实例化对象.
	 *  当实例化对象时 如果有参数,则自动的完成注入.
	 * @param pool
	 * @return
	 */
	/*
	 * @Bean
	 * 
	 * @Scope("prototype") public Jedis jedis(JedisSentinelPool pool) {
	 * 
	 * return pool.getResource(); }
	 */
	


	/*
	 * //添加redis的分片
	 * 
	 * @Value("${redis.nodes}") private String nodes; //node,node,node
	 * 
	 * 
	 * @Scope("prototype") //多例对象
	 * 
	 * @Bean public ShardedJedis shardedJedis() { List<JedisShardInfo> shards = new
	 * ArrayList<>(); String[] arrayNode = nodes.split(","); for (String node :
	 * arrayNode) { //host:port String host = node.split(":")[0]; int port =
	 * Integer.parseInt(node.split(":")[1]); JedisShardInfo info = new
	 * JedisShardInfo(host, port); shards.add(info); }
	 * 
	 * return new ShardedJedis(shards); }
	 */





	/*
	 * @Value("${redis.host}") private String host;
	 * 
	 * @Value("${redis.port}") private Integer port;
	 * 
	 * //默认单例对象,修改为多例对象.
	 * 
	 * @Scope("prototype")
	 * 
	 * @Bean public Jedis jedis() {
	 * 
	 * return new Jedis(host,port); }
	 */

