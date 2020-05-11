package com.solactive.tickstatistics.repository.impl;

import com.solactive.tickstatistics.repository.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

    private static final String KEY = "INSTRUMENTLASTUPDATE";

    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void insert(String instrument, Long timestamp) {
        hashOperations.put(KEY, instrument, timestamp);
    }

    @Override
    public void delete(String instrument) {
        hashOperations.delete(KEY, instrument);
    }

    @Override
    public Long get(String instrument) {
        Object value = hashOperations.get(KEY, instrument);
        if(value != null)
            return (Long)value;
        else
            return 0L;
    }

    @Override
    public Map<String, Object> getMap() {
        return hashOperations.entries(KEY);
    }
}
