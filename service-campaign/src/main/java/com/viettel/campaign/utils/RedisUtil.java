package com.viettel.campaign.utils;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hanv_itsol
 * @project campaign
 */
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

//    private static volatile RedisUtil INSTANCE = null;
    private static RedisUtil INSTANCE = null;
    private JedisCluster jedisCluster;
    private String redisAddress;
    private int redisTimeout;

    private RedisUtil(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public RedisUtil(String redisAddress, int redisTimeout) {
        this.redisAddress = redisAddress;
        this.redisTimeout = redisTimeout;
    }

    public RedisUtil() {
    }

    public synchronized RedisUtil setup() {
        if (INSTANCE == null) {
            logger.info("Start connect Redis: " + redisAddress);
            Set<HostAndPort> hostAndPortNodes = new HashSet();
            String[] hostAndPorts = redisAddress.split(",");
            for (String hostAndPort : hostAndPorts) {
                String[] parts = hostAndPort.split(":");
                String host = parts[0];
                int port = Integer.parseInt(parts[1].trim());
                hostAndPortNodes.add(new HostAndPort(host, port));
            }
            INSTANCE = new RedisUtil(new JedisCluster(hostAndPortNodes, redisTimeout));
        }
        return INSTANCE;
    }

    public static synchronized RedisUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RedisUtil().setup();
        }
        return INSTANCE;
    }

    public JedisCluster getRedis() {
        return INSTANCE.jedisCluster;
    }

    public void setRedisAddress(String dress) {
        redisAddress = dress;
    }

    public void setRedisTimeout(int timeout) {
        redisTimeout = timeout;
    }

    public String getRedisAddress() {
        return redisAddress;
    }

    public int getRedisTimeout() {
        return redisTimeout;
    }




    public  boolean set(String key, Serializable value) {
        if (key == null) {
            return false;
        }

//        Jedis jedis = null;
        try {
            JedisCluster jedis = getRedis();
            jedis.set(key.getBytes(), SerializationUtils.serialize(value));
            jedis.expire(key.getBytes(), 60); // exprire
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }


        return true;
    }

    public  Object get(String key) {
//        Jedis jedis = null;
        Object object = null;
        try {
//            jedis = pool.getResource();
            JedisCluster jedis = getRedis();
            byte[] value = jedis.get(key.getBytes());
            if (value != null && value.length > 0) {
                object = (Object) SerializationUtils.deserialize(value);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }


        return object;
    }

    public  void del(String key) {
//        Jedis jedis = null;
//        Object object = null;
        try {
//            jedis = pool.getResource();
            JedisCluster jedis = getRedis();
            jedis.del(key);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }
}
