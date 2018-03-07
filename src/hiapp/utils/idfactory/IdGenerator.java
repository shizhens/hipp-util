/**
 * 
 */
package hiapp.utils.idfactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hiapp.utils.idfactory.data.IdRepository;
import hiapp.utils.serialize.SerializeUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * @author zhang
 *
 */
public class IdGenerator {
	private JedisPool jedisPool;
	private String idHead;
	private IdRepository repository;
	private Date date;
	private IdCache idCache;
	private SimpleDateFormat dateFmt;
	
	private final String redisKey_CacheMax;
	private final String redisKey_IdCacheList;
	
	public IdGenerator(IdRepository repository, String idHead, int batchSize, JedisPool jedisPool) {
		this.jedisPool = jedisPool;
		this.repository = repository;
		this.idHead = idHead;
		this.dateFmt = new SimpleDateFormat("yyyyMMdd");
		
		this.redisKey_CacheMax = String.format("IDF_CACHEMAX_%s", idHead);
		this.redisKey_IdCacheList = String.format("IDF_CACHELIST_%s", idHead);
		
		refreshDate();
		
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			IdCache cacheMax = new IdCache();
			cacheMax.setDate(this.date);
			cacheMax.setSeed(1);
			cacheMax.setMax(1);
			jedis.setnx(this.redisKey_CacheMax.getBytes(), SerializeUtil.serialize(cacheMax));
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}

		if (!this.updataCache()) {
			this.pullBatch();
		}
	}

	/**
	 * 
	 */
	private void refreshDate() {
		String strDate = this.dateFmt.format(new Date());
		try {
			this.date = this.dateFmt.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getIdHead() {
		return idHead;
	}
	
	public synchronized String newId() {
		if (this.idCache == null || !DateUtils.isSameDay(new Date(), this.idCache.getDate())) {
			if (!this.updataCache()) {
				if (!this.pullBatch()) {
					return null;
				}
			}
		}
		
		return generateId();
	}
	
	public synchronized List<String> newIds(int count) {
		if (this.idCache == null || !DateUtils.isSameDay(new Date(), this.idCache.getDate())) {
			if (!this.updataCache()) {
				if (!this.pullBatch()) {
					return null;
				}
			}
		}
		
		List<String> ids = new ArrayList<String>();
		
		if (count < 1) {
			return ids;
		}
		
		int loopTimes = 0;
		for (loopTimes = 0; loopTimes < count; ++loopTimes) {
			String id = generateId();
			if (id != null) {
				ids.add(id);
			}
		}
		
		return ids;
	}
	
	private boolean pullBatch() {
		Logger logger = LogManager.getLogger("hiapp");
		logger.info("pullBatch");
		Jedis jedis = null;
		Transaction tx = null;
		refreshDate();
		try {
			//从数据库中取得ID数据
			IdHeadData idHeadData = this.repository.getIdHeadData(this.getIdHead());
			if (null == idHeadData) {
				return false;
			}
			if (null == idHeadData.getSeedDate()) {
				return false;
			}
			
			jedis = this.jedisPool.getResource();
			jedis.watch(this.redisKey_CacheMax);
			byte[] cacheMaxBytes = jedis.get(this.redisKey_CacheMax.getBytes());
			IdCache cacheMax = (IdCache) SerializeUtil.unserialize(cacheMaxBytes);
				
			//生成批量数据
			IdCache[] idCaches = new IdCache[5];
			if (!DateUtils.isSameDay(idHeadData.getSeedDate(), this.date)) {
				idHeadData.setSeedDate(this.date);
				idHeadData.setSeed(1);
			}
			if (cacheMax.getDate().compareTo(idHeadData.getSeedDate()) > 0 || 
					(DateUtils.isSameDay(cacheMax.getDate(), idHeadData.getSeedDate()) && cacheMax.getMax() > idHeadData.getSeed())) {
				return false;
			}
			
			cacheMax.setDate(idHeadData.getSeedDate());
			cacheMax.setSeed(idHeadData.getSeed());
			for (int index = 0; index < idCaches.length; ++index) {
				idCaches[index] = new IdCache();
				idCaches[index].setDate(idHeadData.getSeedDate());
				idCaches[index].setSeed(idHeadData.getSeed());
				idCaches[index].setMax(idHeadData.getSeed() + idHeadData.getBatchSize() - 1);
				idHeadData.setSeed(idCaches[index].getMax() + 1);
			}
			cacheMax.setMax(idHeadData.getSeed() - 1);
			
			//批量数据写入redis
			tx = jedis.multi();// 开启事务
			tx.set(this.redisKey_CacheMax.getBytes(), SerializeUtil.serialize(cacheMax));
			for (int index = 0; index < idCaches.length; ++index) {
				tx.rpush(redisKey_IdCacheList.getBytes(), SerializeUtil.serialize(idCaches[index]));
			}
            List<Object> returnList = tx.exec();// 提交事务，如果此时watchkeys被改动了，则返回null
			
            if (returnList != null && returnList.size() > 0) { //执行成功
            	logger.info(String.format("pullBatch success; nextSeed=%d;", idHeadData.getSeed()));
            	//批量数据入库
            	this.repository.updateBatchIds(idHeadData);
            } else {
            	logger.info(String.format("pullBatch fail; nextSeed=%d;", idHeadData.getSeed()));
            }
			this.updataCache();
		} catch (Exception ex) {
			logger.info(String.format("pullBatch fail; %s", ex.getMessage()));
			ex.printStackTrace();
		} finally {
			if (null != tx) {
				try {
					tx.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != jedis) {
				jedis.unwatch();
				jedis.close();
			}
		}
		
		return true;
	}
	
	private boolean updataCache() {
		Jedis jedis = null;
		Transaction tx = null;
		//Logger logger = LogManager.getLogger("hiapp");
		try {
			refreshDate();
			jedis = this.jedisPool.getResource();
			IdCache tempIdCache = null;
			do {
				byte[] idCacheBytes = jedis.lpop(this.redisKey_IdCacheList.getBytes());
				if (idCacheBytes == null) {
					return false;
				}
				
				if (idCacheBytes.length < 1) {
					continue;
				}
				
				tempIdCache = (IdCache) SerializeUtil.unserialize(idCacheBytes);
		        
		        if (!DateUtils.isSameDay(tempIdCache.getDate(), this.date)) {
		        	tempIdCache = null;
		        	continue;
		        }
			}
			while (tempIdCache == null);
			
			this.idCache = tempIdCache;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (null != tx) {
				try {
					tx.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != jedis) {
				jedis.close();
			}
		}
		
		return true;
	}
	
	private String generateId() {
		int id = -1;
		if (this.idCache.getSeed() > this.idCache.getMax()) {
			if (!this.updataCache()) {
				if (!this.pullBatch()) {
					return null;
				}
			}
		}
		
		id = this.idCache.getSeed();
		this.idCache.setSeed(this.idCache.getSeed() + 1);
		
		return String.format("%s_%s_%d", this.getIdHead(), this.dateFmt.format(this.date), id);
	}
}
