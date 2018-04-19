/**
 * 
 */
package hiapp.utils.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hiapp.utils.base.HiAppContext;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author zhang
 *
 */
@Configuration
public class UtilsRootConfig {

	@Bean
	public JedisPool jedisPool(HiAppContext appContext) {
		JedisPool jedisPool = null;
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(1024);
			config.setMaxIdle(200);
			config.setMaxWaitMillis(10000);
			config.setTestOnBorrow(true);
			
			String redisServer = appContext.getServletContext().getInitParameter("redisServer");
			int redisPort = Integer.parseInt(appContext.getServletContext().getInitParameter("redisPort"));
			String redisPassword = appContext.getServletContext().getInitParameter("redisPassword");
			
			if (redisPassword != null && redisPassword.length() > 0) {
				jedisPool = new JedisPool(config, redisServer, redisPort, 10000, redisPassword);
			} else {
				jedisPool = new JedisPool(config, redisServer, redisPort, 10000);
			}
			System.out.println("JedisPool Created!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return jedisPool;
	}
}
