package com.teradata.openapi.framework.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 内存数据库Redis集群的辅助类，负责对内存数据库的所有操作 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月5日 下午4:10:06
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class RedisUtil {

	private static RedisUtil singleton = null;

	private volatile static JedisCluster jedisCluster = null;

	public static RedisUtil getInstance() {
		if (singleton == null) {
			synchronized (RedisUtil.class) {
				if (singleton == null) {
					singleton = new RedisUtil();
				}
			}
		}
		return singleton;
	}

	private RedisUtil() {
		initPools();
	}

	/**
	 * 构建redis连接池
	 * 
	 * @param <HostAndPort>
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 */
	public void initPools() {
		ResourceBundle bundle = ResourceBundle.getBundle("redis");
		if (bundle == null) {
			throw new IllegalArgumentException("[redis.properties] is not found!");
		}

		if (jedisCluster == null) {
			try {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(Integer.parseInt(bundle.getString("redis.pool.maxtotal")));
				config.setMaxIdle(Integer.parseInt(bundle.getString("redis.pool.maxIdle")));
				config.setMaxWaitMillis(Integer.parseInt(bundle.getString("redis.pool.maxWait")));
				config.setTestOnBorrow(Boolean.parseBoolean(bundle.getString("redis.pool.testOnBorrow")));
				config.setTestOnReturn(Boolean.parseBoolean(bundle.getString("redis.pool.testOnReturn")));

				String[] hosts = bundle.getString("redis.ips").split(",");
				String[] ports = bundle.getString("redis.ports").split(",");
				if (hosts.length > ports.length) {
					throw new Exception("集群主机端口号个数不匹配");
				} else {
					Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
					for (int i = 0; i < hosts.length; i++) {
						jedisClusterNodes.add(new HostAndPort(hosts[i], Integer.valueOf(ports[i])));
					}
					jedisCluster = new JedisCluster(jedisClusterNodes, config);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 执行器，{@link com.futurefleet.framework.base.redis.RedisUtil}的辅助类， 它保证在执行操作之后释放数据源returnResource(jedis)
	 * 
	 * @version V1.0
	 * @author houbl
	 * @param <T>
	 */
	abstract class Executor<T> {

		JedisCluster jedis;

		public Executor(JedisCluster jedisCluster) {
			this.jedis = jedisCluster;
		}

		/**
		 * 回调
		 * 
		 * @return 执行结果
		 */
		abstract T execute();

		/**
		 * 调用{@link #execute()}并返回执行结果 它保证在执行{@link #execute()} 之后释放数据源returnResource(jedis)
		 * 
		 * @return 执行结果
		 */
		public T getResult() {
			T result = null;
			try {
				result = execute();
			}
			catch (Throwable e) {
				throw new RuntimeException("Redis execute exception:" + e.getMessage(), e);
			}
			return result;
		}
	}

	/**
	 * 删除
	 * 
	 * @param key 匹配的key
	 * @return 删除成功的条数
	 */
	public Long delKey(final String key) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.del(key);
			}
		}.getResult();
	}

	/**
	 * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。
	 * 
	 * @param key key
	 * @param expire 生命周期，单位为秒
	 * @return 1: 设置成功 0: 已经超时或key不存在
	 */
	public Long expire(final String key, final int expire) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.expire(key, expire);
			}
		}.getResult();
	}

	/**
	 * 一个跨jvm的id生成器，利用了redis原子性操作的特点
	 * 
	 * @param key id的key
	 * @return 返回生成的Id
	 */
	public long makeId(final String key) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				long id = jedis.incr(key);
				if ((id + 75807) >= Long.MAX_VALUE) {
					// 避免溢出，重置，getSet命令之前允许incr插队，75807就是预留的插队空间
					jedis.getSet(key, "0");
				}
				return id;
			}
		}.getResult();
	}

	/*
	 * ======================================Strings============================== ========
	 */

	/**
	 * 将字符串值 value 关联到 key 。 如果 key 已经持有其他值， setString 就覆写旧值，无视类型。 对于某个原本带有生存时间（TTL）的键来说， 当 setString 成功在这个键上执行时， 这个键原有的 TTL 将被清除。
	 * 时间复杂度：O(1)
	 * 
	 * @param key key
	 * @param value string value
	 * @return 在设置操作成功完成时，才返回 OK 。
	 */
	public String setString(final String key, final String value) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				return jedis.set(key, value);
			}
		}.getResult();
	}

	/**
	 * 将值 value 关联到 key ，并将 key 的生存时间设为 expire (以秒为单位)。 如果 key 已经存在， 将覆写旧值。 类似于以下两个命令: SET key value EXPIRE key expire # 设置生存时间
	 * 不同之处是这个方法是一个原子性(atomic)操作，关联值和设置生存时间两个动作会在同一时间内完成，在 Redis 用作缓存时，非常实用。 时间复杂度：O(1)
	 * 
	 * @param key key
	 * @param value string value
	 * @param expire 生命周期
	 * @return 设置成功时返回 OK 。当 expire 参数不合法时，返回一个错误。
	 */
	public String setString(final String key, final String value, final int expire) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				return jedis.setex(key, expire, value);
			}
		}.getResult();
	}

	/**
	 * 将 key 的值设为 value ，当且仅当 key 不存在。若给定的 key 已经存在，则 setStringIfNotExists 不做任何动作。 时间复杂度：O(1)
	 * 
	 * @param key key
	 * @param value string value
	 * @return 设置成功，返回 1 。设置失败，返回 0 。
	 */
	public Long setStringIfNotExists(final String key, final String value) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.setnx(key, value);
			}
		}.getResult();
	}

	/**
	 * 返回 key 所关联的字符串值。如果 key 不存在那么返回特殊值 nil 。 假如 key 储存的值不是字符串类型，返回一个错误，因为 getString 只能用于处理字符串值。 时间复杂度: O(1)
	 * 
	 * @param key key
	 * @return 当 key 不存在时，返回 nil ，否则，返回 key 的值。如果 key 不是字符串类型，那么返回一个错误。
	 */
	public String getString(final String key) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				return jedis.get(key);
			}
		}.getResult();
	}

	/**
	 * 判断key值是存在
	 * 
	 * @param key
	 * @return
	 * @author houbl
	 */
	public Boolean isExists(final String key) {
		return new Executor<Boolean>(jedisCluster) {

			@Override
			Boolean execute() {
				return jedis.exists(key);
			}
		}.getResult();
	}

	/*
	 * ======================================Object============================== ========
	 */
	/**
	 * get:(获取对象)
	 * 
	 * @param key
	 * @return
	 * @author houbl
	 */
	public <T> T getObject(final String key) {
		return new Executor<T>(jedisCluster) {

			@Override
			T execute() {
				byte[] bs = jedis.get(SerializeUtil.serialize(key));
				if (bs != null) {
					return SerializeUtil.unserialize(bs);
				} else {
					return null;
				}

			}
		}.getResult();
	}

	/**
	 * 保存对象
	 * 
	 * @param key
	 * @param value
	 * @return 在设置操作成功完成时，才返回 OK
	 * @author houbl
	 */
	public <T> String setObject(final String key, final T value) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				return jedis.set(SerializeUtil.serialize(key), SerializeUtil.serialize(value));

			}
		}.getResult();
	}

	/**
	 * remove:(清除对象)
	 * 
	 * @param key
	 * @author houbl
	 */
	public Long removeObject(final String key) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.del(SerializeUtil.serialize(key));

			}
		}.getResult();
	}

	public <T> String hashMultipleObjectSet(final String key, final Map<T, T> hash) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				Map<byte[], byte[]> inMap = new HashMap<byte[], byte[]>();
				for (Map.Entry<T, T> entry : hash.entrySet()) {
					inMap.put(SerializeUtil.serialize(entry.getKey()), SerializeUtil.serialize(entry.getValue()));
				}
				return jedis.hmset(SerializeUtil.serialize(key), inMap);
			}
		}.getResult();
	}

	public <T> Map<T, T> hashObjectGetAll(final String key) {
		return new Executor<Map<T, T>>(jedisCluster) {

			@Override
			Map<T, T> execute() {
				Map<T, T> outMap = new HashMap<T, T>();
				Map<byte[], byte[]> resultMap = jedis.hgetAll(SerializeUtil.serialize(key));
				for (Map.Entry<byte[], byte[]> entry : resultMap.entrySet()) {
					T outKey = SerializeUtil.unserialize(entry.getKey());
					T outValue = SerializeUtil.unserialize(entry.getValue());
					outMap.put(outKey, outValue);
				}
				return outMap;
			}
		}.getResult();
	}

	public <T> Object hashObjectGet(final String key, final String field) {
		return new Executor<Object>(jedisCluster) {

			@Override
			Object execute() {
				byte[] result = jedis.hget(SerializeUtil.serialize(key), SerializeUtil.serialize(field));
				return SerializeUtil.unserialize(result);
			}
		}.getResult();
	}

	/*
	 * ======================================Hashes============================== ========
	 */

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 。 如果 key 不存在，一个新的哈希表被创建并进行 hashSet 操作。 如果域 field 已经存在于哈希表中，旧值将被覆盖。 时间复杂度: O(1)
	 * 
	 * @param key key
	 * @param field 域
	 * @param value string value
	 * @return 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 1 。如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 0 。
	 */
	public Long hashSet(final String key, final String field, final String value) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.hset(key, field, value);
			}
		}.getResult();
	}

	/**
	 * 返回哈希表 key 中给定域 field 的值。 时间复杂度:O(1)
	 * 
	 * @param key key
	 * @param field 域
	 * @return 给定域的值。当给定域不存在或是给定 key 不存在时，返回 nil 。
	 */
	public String hashGet(final String key, final String field) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				return jedis.hget(key, field);
			}
		}.getResult();
	}

	/**
	 * 同时将多个 field-value (域-值)对设置到哈希表 key 中。 时间复杂度: O(N) (N为fields的数量)
	 * 
	 * @param key key
	 * @param hash field-value的map
	 * @return 如果命令执行成功，返回 OK 。当 key 不是哈希表(hash)类型时，返回一个错误。
	 */
	public String hashMultipleSet(final String key, final Map<String, String> hash) {
		return new Executor<String>(jedisCluster) {

			@Override
			String execute() {
				return jedis.hmset(key, hash);
			}
		}.getResult();
	}

	/**
	 * 返回哈希表 key 中，一个或多个给定域的值。如果给定的域不存在于哈希表，那么返回一个 nil 值。 时间复杂度: O(N) (N为fields的数量)
	 * 
	 * @param key key
	 * @param fields field的数组
	 * @return 一个包含多个给定域的关联值的表，表值的排列顺序和给定域参数的请求顺序一样。
	 */
	public List<String> hashMultipleGet(final String key, final String... fields) {
		return new Executor<List<String>>(jedisCluster) {

			@Override
			List<String> execute() {
				return jedis.hmget(key, fields);
			}
		}.getResult();
	}

	/**
	 * 返回哈希表 key 中，所有的域和值。在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。 时间复杂度: O(N)
	 * 
	 * @param key key
	 * @return 以列表形式返回哈希表的域和域的值。若 key 不存在，返回空列表。
	 */
	public Map<String, String> hashGetAll(final String key) {
		return new Executor<Map<String, String>>(jedisCluster) {

			@Override
			Map<String, String> execute() {
				return jedis.hgetAll(key);
			}
		}.getResult();
	}

	/*
	 * ======================================List================================ ======
	 */

	/**
	 * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
	 * 
	 * @param key key
	 * @param values value的数组
	 * @return 执行 listPushTail 操作后，表的长度
	 */
	public Long listPushTail(final String key, final String... values) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.rpush(key, values);
			}
		}.getResult();
	}

	/**
	 * 将一个或多个值 value 插入到列表 key 的表头
	 * 
	 * @param key key
	 * @param value string value
	 * @return 执行 listPushHead 命令后，列表的长度。
	 */
	public Long listPushHead(final String key, final String value) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.lpush(key, value);
			}
		}.getResult();
	}

	/**
	 * 返回list所有元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
	 * 
	 * @param key key
	 * @return list所有元素
	 */
	public List<String> listGetAll(final String key) {
		return new Executor<List<String>>(jedisCluster) {

			@Override
			List<String> execute() {
				return jedis.lrange(key, 0, -1);
			}
		}.getResult();
	}

	/**
	 * 返回指定区间内的元素，下标从0开始，负值表示从后面计算，-1表示倒数第一个元素，key不存在返回空列表
	 * 
	 * @param key key
	 * @param beginIndex 下标开始索引（包含）
	 * @param endIndex 下标结束索引（不包含）
	 * @return 指定区间内的元素
	 */
	public List<String> listRange(final String key, final long beginIndex, final long endIndex) {
		return new Executor<List<String>>(jedisCluster) {

			@Override
			List<String> execute() {
				return jedis.lrange(key, beginIndex, endIndex - 1);
			}
		}.getResult();
	}

	/*
	 * ======================================Sorted set=================================
	 */

	/**
	 * 将一个 member 元素及其 score 值加入到有序集 key 当中。
	 * 
	 * @param key key
	 * @param score score 值可以是整数值或双精度浮点数。
	 * @param member 有序集的成员
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 */
	public Long addWithSortedSet(final String key, final double score, final String member) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.zadd(key, score, member);
			}
		}.getResult();
	}

	/**
	 * 将多个 member 元素及其 score 值加入到有序集 key 当中。
	 * 
	 * @param key key
	 * @param scoreMembers score、member的pair
	 * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
	 */
	public Long addWithSortedSet(final String key, final Map<String, Double> scoreMembers) {
		return new Executor<Long>(jedisCluster) {

			@Override
			Long execute() {
				return jedis.zadd(key, scoreMembers);
			}
		}.getResult();
	}

	/**
	 * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。 有序集成员按 score 值递减(从大到小)的次序排列。
	 * 
	 * @param key key
	 * @param max score最大值
	 * @param min score最小值
	 * @return 指定区间内，带有 score 值(可选)的有序集成员的列表
	 */
	public Set<String> revrangeByScoreWithSortedSet(final String key, final double max, final double min) {
		return new Executor<Set<String>>(jedisCluster) {

			@Override
			Set<String> execute() {
				return jedis.zrevrangeByScore(key, max, min);
			}
		}.getResult();
	}

	/**
	 * 获取所有keys值
	 * 
	 * @param pattern
	 * @return
	 * @author houbl
	 */
	public TreeSet<String> getAllkeys(String pattern) {
		TreeSet<String> keys = new TreeSet<>();
		Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
		for (String k : clusterNodes.keySet()) {
			JedisPool jp = clusterNodes.get(k);
			Jedis connection = jp.getResource();
			try {
				keys.addAll(connection.keys(pattern));
			}
			catch (Exception e) {
				throw new RuntimeException("Getting keys error: {}", e);
			}
			finally {
				connection.close();// 用完一定要close这个链接！！！
			}
		}
		return keys;
	}

	/**
	 * main函数.
	 * 
	 * @param args 启动参数
	 * @throws Exception Exception
	 */
	public static void main(String... args) throws Exception {
		try {
			// RedisUtil.getInstance().setString("teradata", "openapi cluster");
			TreeSet<String> values = RedisUtil.getInstance().getAllkeys("*");
			System.out.println(values.size());
			for (String value : values) {
				System.out.println(value);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		// System.out.println(RedisUtil.getInstance().getString("teradata"));
	}
}
