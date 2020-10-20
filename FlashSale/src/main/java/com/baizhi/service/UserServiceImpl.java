package com.baizhi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
@Slf4j
public class UserServiceImpl  implements UserService{

    @Autowired
    private StringRedisTemplate stringRedisTemplate; //string key value


    @Override
    public int saveUserCount(Integer userId) {


        String limitKey = "LIMIT" + "_" + userId;


        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        int limit =-1;
        if (limitNum == null) {

            stringRedisTemplate.opsForValue().set(limitKey, "0", 3600, TimeUnit.SECONDS);
        } else {

            limit = Integer.parseInt(limitNum) + 1;
            stringRedisTemplate.opsForValue().set(limitKey, String.valueOf(limit), 3600, TimeUnit.SECONDS);
        }
        return limit;
    }

    @Override
    public boolean getUserCount(Integer userId) {

        String limitKey = "LIMIT"+ "_" + userId;

        String limitNum = stringRedisTemplate.opsForValue().get(limitKey);
        if (limitNum == null) {

            log.error("This user has never registered, Error");
            return true;
        }
        return Integer.parseInt(limitNum) > 10;
    }
}