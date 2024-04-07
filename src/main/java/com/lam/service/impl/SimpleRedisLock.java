package com.lam.service.impl;

import com.lam.service.Ilock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

public class SimpleRedisLock implements Ilock {

    public static final String KEY_PREFIX="Lock";

    @Autowired
    RedisTemplate redisTemplate;

    private String name;

    public SimpleRedisLock(){}

    public SimpleRedisLock(String name){
        this.name=name;
    }

    @Override
    public boolean tryLock(long timeOutSec) {

        String ThreadId=Long.valueOf(Thread.currentThread().getId()).toString();

        boolean isSucessful=redisTemplate.opsForValue().setIfAbsent(KEY_PREFIX+name,ThreadId+" ",timeOutSec, TimeUnit.SECONDS);

        return Boolean.TRUE.equals(isSucessful);
    }

    @Override
    public void unLock() {
        redisTemplate.opsForValue().getOperations().delete(KEY_PREFIX+name);
    }
}
