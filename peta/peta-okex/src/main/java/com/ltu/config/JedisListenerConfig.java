package com.ltu.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**  
 * @Description: 使用jedis监听redis key过期事件订阅
 * @date 2019年7月7日 上午10:21:57
 * @version V1.0  
 */

//@Configuration
//@Import(value=TerminalAdminApplication.class)
public class JedisListenerConfig {
	/* 使用springboot 已经注入好的reids属性bean配置Jedis */
//	@Autowired
	private	RedisProperties  properties;

//	@Bean
	public  JedisPool   jedisPool(){
		JedisPoolConfig  config=new JedisPoolConfig();
		config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        JedisPool pool = new JedisPool(config, properties.getHost(),properties.getPort(),1000,properties.getPassword(),properties.getDatabase());
        Jedis jedis=pool.getResource();
//        jedis.psubscribe(new JedisPubsubKeyListener() , "__keyevent@0__:expired");
        return pool;
	}
}
	
