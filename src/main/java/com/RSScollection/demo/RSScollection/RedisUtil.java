package com.RSScollection.demo.RSScollection;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
  private static ThreadLocal<Jedis> threadLocal = new ThreadLocal<Jedis>();
  private static JedisPool pool;

  static{
    pool = new JedisPool(new JedisPoolConfig(), "localhost");
  }

  private RedisUtil(){}

  public static Jedis getRedisConnect(){
    Jedis jedis = threadLocal.get();

    if(jedis == null){
			
			jedis = pool.getResource();
			
			threadLocal.set(jedis);
		}
		
		return jedis;
  }

  public static void closeRedisConnect(){
		
		Jedis jedis = threadLocal.get();
		
		if(jedis != null){
			
			jedis.close();
			
			threadLocal.remove();
		}
	}	  
}