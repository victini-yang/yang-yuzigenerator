package com.yupi.web.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Cos 对象存储操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Component
public class CacheManager {

    //    引入Caffeine缓存、将多级缓存逻辑抽离
//    相当于比redis缓存更高一级的缓存，读取更快
//    引入依赖
    @Resource
    private RedisTemplate<String ,Object> redisTemplate;

    Cache<String, Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(100, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 写入缓存
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
//        放入caffeine
        localCache.put(key, value);
//        放入redis
        redisTemplate.opsForValue().set(key, value,100, TimeUnit.MINUTES);
    }

    /**
     * 读缓存
     * @param key
     * @return
     */
    public Object get(String key) {

//        先从本地缓存中获取
        Object value = localCache.getIfPresent(key);
        if (value != null) {
            return value;
        }

//        本地缓存未命中，尝试从Redis获取
        value = redisTemplate.opsForValue().get(key);
        if (value != null) {
//            将redis的值写入到本地缓存
            localCache.put(key, value);
        }
        return value;
    }

    /**
     * 移除缓存
     * @param key
     */
    public void delete(String key) {
        localCache.invalidate(key);
        redisTemplate.delete(key);
    }


}
