package com.example.demo.infraestructure.db;

import com.example.demo.domain.PersonEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveObjectList(String key, List<PersonEntity> objectList) {
        redisTemplate.opsForList().leftPushAll(key, objectList.toArray());
    }

    public List<PersonEntity> getObjectList(String key) {
        var objects = redisTemplate.opsForList().range(key, 0, -1);
        var myObjectList = new ArrayList<PersonEntity>();
        for (Object obj : objects) {
            if (obj instanceof PersonEntity) {
                myObjectList.add((PersonEntity) obj);
            }
        }
        return myObjectList;
    }

    public void saveObject(String key, PersonEntity person) {
        redisTemplate.opsForValue().set(person.getId().toString(), person);
    }

}
